package com.miller.mining.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miller.mining.callback.ControllerCallbackHandler;
import com.miller.mining.comm.ResponseCodeEnum;
import com.miller.mining.exception.LoginFailedException;
import com.miller.mining.exception.TransforVoFaildedException;
import com.miller.mining.exception.VerifyException;
import com.miller.mining.model.MiningInfo;
import com.miller.mining.model.User;
import com.miller.mining.service.JedisService;
import com.miller.mining.service.MiningService;
import com.miller.mining.service.UserService;
import com.miller.mining.utils.DateUtil;
import com.miller.mining.utils.JsonUtil;
import com.miller.mining.vo.MiningInfoVo;
import com.miller.mining.vo.MiningSingleVo;
import com.miller.mining.vo.RequestVo;

@Controller
@RequestMapping(value="mining")
public class MiningController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(MiningController.class);
	
	@Autowired
	private MiningService miningService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisService jedisService;
	
	/**
	 * 挖掘活动开始或结束请求
	 * @param request
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "active")
    @ResponseBody
	public Map<String,String> miningActiveOrDeactive(final HttpServletRequest request,
            @RequestBody RequestVo requestVo) {
		Map<String,String> map = new HashMap<String,String>();
		logger.info("进入挖掘活动开始结束请求=======");
		
		try {
			this.executeWithLogin(request, requestVo, new ControllerCallbackHandler() {

				@Override
				public void doMVC(HttpServletRequest request, String requestContent)
						throws VerifyException, TransforVoFaildedException, LoginFailedException {
					// TODO Auto-generated method stub
					//解密后的报文转化为vo
					MiningInfoVo miningVo = JsonUtil.transforJsonToVO(requestContent, MiningInfoVo.class);
					//转化失败抛异常
					if(null == miningVo) {
						throw new VerifyException("解密后的报文信息转化vo异常");
					}
					
					//开始挖掘
					if(miningVo.getMingState() == 0) {
						//首先进行参数校验
						checkParam(0,miningVo);
						//查询当前是否存在正在进行的挖掘
						List<MiningInfo> activeMiningInfoList = miningService.getActiveMiningInfoByUsername(miningVo.getUsername());
						//列表中数量大于0个，说明在存在已经存在进行中的挖掘，需要先把进行中的挖掘结束
						if(activeMiningInfoList.size() > 0) {
							//遍历并结束所有进行中的挖掘
							for(MiningInfo info : activeMiningInfoList) {
								try {
									info.setEndTime(DateUtil.getDateStrWithFormat("yyyy-MM-dd HH:mm:ss", 
											new Date()));
									info.setState(1);
									miningService.endMining(info,miningVo.getUsername());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							//创建本次挖掘
							MiningInfo startInfo = new MiningInfo();
							startInfo.setStartTime(miningVo.getStartTime());
							startInfo.setType(miningVo.getMingType());
							startInfo.setState(0);
							miningService.startMining(startInfo,miningVo.getUsername());
						}
						//列表数量为0，说明不存在进行中的挖掘，直接开始
						else {
							//创建本次挖掘
							MiningInfo startInfo = new MiningInfo();
							startInfo.setStartTime(miningVo.getStartTime());
							startInfo.setType(miningVo.getMingType());
							startInfo.setState(0);
							miningService.startMining(startInfo,miningVo.getUsername());
						}
					}
					//结束挖掘
					else if(miningVo.getMingState() == 1) {
						//首先进行参数校验
						checkParam(1,miningVo);
						//查询当前是否存在正在进行的挖掘
						List<MiningInfo> activeMiningInfoList = miningService.getActiveMiningInfoByUsername(miningVo.getUsername());
						
						//正常情况下应该只存在一个进行中的挖掘
						if(activeMiningInfoList.size() == 1) {
							MiningInfo miningInfo = activeMiningInfoList.get(0);
							miningInfo.setEndTime(miningVo.getEndTime());
							miningInfo.setMiningAmount(new BigDecimal(miningVo.getRunningAmount()));
							miningInfo.setRunningMile(new BigDecimal(miningVo.getRunningMile()));
							try {
								miningInfo.setRunningTime(new BigDecimal(DateUtil.getDistanceOfDate(miningInfo.getEndTime(), miningInfo.getStartTime(), 
										"yyyy-MM-dd HH:mm:ss")));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								map.put("resultCode", ResponseCodeEnum.DATA_INVALID.getCode());
								map.put("resultMessage", "日期转换失败");
							}
							miningInfo.setState(1);
							try {
								miningService.endMining(miningInfo, miningVo.getUsername());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								map.put("resultCode", ResponseCodeEnum.DATA_INVALID.getCode());
								map.put("resultMessage", "更新数据异常");
							}
						}
					}
				}

				@Override
				public boolean check(HttpServletRequest request, String requestContent) throws VerifyException {
					// TODO Auto-generated method stub
					//解密后的报文转化为vo
					MiningInfoVo mingVo = JsonUtil.transforJsonToVO(requestContent, MiningInfoVo.class);
					//转化失败抛异常
					if(null == mingVo) {
						throw new VerifyException("解密后的报文信息转化vo异常");
					}
					
					//根据传入的用户名查询用户
					User user = userService.getUserByUsername(mingVo.getUsername());
					//查询失败时直接返回
					if(null == user) {
						return false;
					}
					
					//获取缓存中的当前用户的token信息
					String tokenInRedis = jedisService.get(user.getUsername());
					//传入的和缓存中的token不一致或者缓存不存在时返回
					if(null == tokenInRedis || tokenInRedis.equals("") ||
							!tokenInRedis.equals(mingVo.getUserToken())) {
						return false;
					}
					return true;
				}
				
			});
		} catch (VerifyException e) {
			// TODO Auto-generated catch block
			map.put("resultCode", ResponseCodeEnum.VERIFY_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		} catch (LoginFailedException e) {
			map.put("resultCode", ResponseCodeEnum.LOGIN_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		} catch (TransforVoFaildedException e) {
			// TODO Auto-generated catch block
			map.put("resultCode", ResponseCodeEnum.DATA_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		} catch (Exception e) {
			map.put("resultCode", ResponseCodeEnum.BAD_REQUEST.getCode());
			map.put("resultMessage", e.getMessage());
		}
		
		return map;
	}
	
	/**
	 * 单次挖掘活动计算
	 * @param request
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "compute")
    @ResponseBody
	public Map<String,String> computeInSingleMining(final HttpServletRequest request,
            @RequestBody RequestVo requestVo) {
		Map<String,String> map = new HashMap<String,String>();
		logger.info("单次挖掘计算开始=========");
		
		try {
			this.executeWithLogin(request, requestVo, new ControllerCallbackHandler() {
				
				@Override
				public void doMVC(HttpServletRequest request, String requestContent)
						throws VerifyException, TransforVoFaildedException, LoginFailedException {
					MiningSingleVo miningVo = JsonUtil.transforJsonToVO(requestContent, MiningSingleVo.class);
					//转化失败抛异常
					if(null == miningVo) {
						throw new VerifyException("解密后的报文信息转化vo异常");
					}
					
					List<MiningInfo> storeInfoList = miningService.getActiveMiningInfoByUsername(miningVo.getUsername());
					if(storeInfoList.size() == 1) {
						
					}
				}
				
				@Override
				public boolean check(HttpServletRequest request, String requestContent) throws VerifyException {
					//解密后的报文转化为vo
					MiningInfoVo mingVo = JsonUtil.transforJsonToVO(requestContent, MiningInfoVo.class);
					//转化失败抛异常
					if(null == mingVo) {
						throw new VerifyException("解密后的报文信息转化vo异常");
					}
					
					//根据传入的用户名查询用户
					User user = userService.getUserByUsername(mingVo.getUsername());
					//查询失败时直接返回
					if(null == user) {
						return false;
					}
					
					//获取缓存中的当前用户的token信息
					String tokenInRedis = jedisService.get(user.getUsername());
					//传入的和缓存中的token不一致或者缓存不存在时返回
					if(null == tokenInRedis || tokenInRedis.equals("") ||
							!tokenInRedis.equals(mingVo.getUserToken())) {
						return false;
					}
					return true;
				}
			});
		} catch (VerifyException e) {
			// TODO Auto-generated catch block
			map.put("resultCode", ResponseCodeEnum.VERIFY_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		} catch (LoginFailedException e) {
			map.put("resultCode", ResponseCodeEnum.LOGIN_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		} catch (TransforVoFaildedException e) {
			// TODO Auto-generated catch block
			map.put("resultCode", ResponseCodeEnum.DATA_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		} catch (Exception e) {
			map.put("resultCode", ResponseCodeEnum.BAD_REQUEST.getCode());
			map.put("resultMessage", e.getMessage());
		}
		return map;
	}
	
	private void checkParam(int checkMode, MiningInfoVo miningVo) throws VerifyException {
		// TODO Auto-generated method stub
		//开始挖掘的校验
		if(checkMode == 0) {
			if(null == miningVo.getStartTime() || miningVo.getStartTime().equals("")) {
				throw new VerifyException("开始时间不能为空");
			}
		} else if(checkMode == 1) {
			if(null == miningVo.getEndTime() || miningVo.getEndTime().equals("")) {
				throw new VerifyException("结束时间不能为空");
			}
			if(null == miningVo.getRunningAmount() || miningVo.getRunningAmount().equals("")) {
				throw new VerifyException("本次挖矿总金额不能为空");
			}
			if(null == miningVo.getRunningMile() || miningVo.getRunningMile().equals("")) {
				throw new VerifyException("本次挖矿总里程不能为空");
			}
		}
	}
}