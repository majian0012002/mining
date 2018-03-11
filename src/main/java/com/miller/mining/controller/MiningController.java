package com.miller.mining.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.miller.mining.model.MiningOverview;
import com.miller.mining.vo.OrderListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miller.mining.callback.ControllerCallbackHandler;
import com.miller.mining.comm.MiningTypeEnum;
import com.miller.mining.comm.ResponseCodeEnum;
import com.miller.mining.exception.LoginFailedException;
import com.miller.mining.exception.MiningException;
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
import com.miller.mining.vo.UserMiningHistoryQuery;
import com.miller.mining.vo.UserMiningHistoryResponse;

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
						throws VerifyException, TransforVoFaildedException, LoginFailedException, MiningException {
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
									info.setEndTime(DateUtil.getDateStrWithFormat("yyyy-MM-ddHH:mm:ss",
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
							startInfo.setMiningAmount(new BigDecimal(0));
							startInfo.setRunningTime(new BigDecimal(0));
							startInfo.setRunningMile(new BigDecimal(0));
							int id = miningService.startMining(startInfo,miningVo.getUsername());
							map.put("miningId",String.valueOf(id));
						}
						//列表数量为0，说明不存在进行中的挖掘，直接开始
						else {
							//创建本次挖掘
							MiningInfo startInfo = new MiningInfo();
							startInfo.setStartTime(miningVo.getStartTime());
							startInfo.setType(miningVo.getMingType());
							startInfo.setState(0);
							startInfo.setMiningAmount(new BigDecimal(0));
							startInfo.setRunningTime(new BigDecimal(0));
							startInfo.setRunningMile(new BigDecimal(0));
							int id = miningService.startMining(startInfo,miningVo.getUsername());
							map.put("miningId",String.valueOf(id));
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
										"yyyy-MM-ddHH:mm:ss")));
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

					map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
					map.put("resultMessage", ResponseCodeEnum.SUCCESS.getDescription());
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
						throws VerifyException, TransforVoFaildedException, LoginFailedException, MiningException {
					MiningSingleVo miningVo = JsonUtil.transforJsonToVO(requestContent, MiningSingleVo.class);
					//转化失败抛异常
					if(null == miningVo) {
						throw new VerifyException("解密后的报文信息转化vo异常");
					}
					
					MiningInfo miningInfo = miningService.getMiningInfoById(Integer.parseInt(miningVo.getMiningId()));
					
					//查询不出来，抛异常
					if(null == miningInfo) {
						throw new MiningException("无法根据id查询出挖矿活动");
					}
					
					//查询出的活动不在进行中，抛异常
					if(miningInfo.getState() != 0) {
						throw new MiningException("当前挖矿已经结束，请开始下次挖矿");
					}
					
					//挖矿类型不一致抛出异常
					if(miningInfo.getType() != miningVo.getMingType()) {
						throw new MiningException("挖矿类型错误");
					}
					
					String miningAmoutOfThisTime = "";
					//根据不同挖矿类型进行计算
					switch(MiningTypeEnum.getByValue(miningVo.getMingType())) {
						case ORDINARY_MODE:
							miningAmoutOfThisTime = miningService.computeMiningAmoutInOrdinaryMode(miningInfo, miningVo);
							break;
						case SPORTS_MODE:
							miningAmoutOfThisTime = miningService.computeMiningAmoutInSportsMode(miningInfo, miningVo);
							break;
						default: break;
					}
					
					map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
					map.put("resultMessage","请求成功");
					map.put("increateCount", miningAmoutOfThisTime);
				}
				
				@Override
				public boolean check(HttpServletRequest request, String requestContent) throws VerifyException {
					//解密后的报文转化为vo
					MiningSingleVo mingVo = JsonUtil.transforJsonToVO(requestContent, MiningSingleVo.class);
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
		} catch (MiningException e) {
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

	@RequestMapping(value = "queryMiningList",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> queryMinigList(final HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("查询列表开始=========");

		List<OrderListVo> miningOverviewList = new ArrayList<OrderListVo>();
		miningOverviewList = miningService.queryListByMoenyOrder();

		map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
		map.put("resultMessage","请求成功");
		map.put("result",miningOverviewList);

		return map;
	}
	
	@RequestMapping(value = "queryUserMiningList")
    @ResponseBody
	public Map<String,Object> queryUserMiningList(final HttpServletRequest request,
            @RequestBody RequestVo requestVo) {
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("查询用户挖矿记录请求=======");
		
		try {
			this.executeWithLogin(request, requestVo, new ControllerCallbackHandler() {
				
				@Override
				public void doMVC(HttpServletRequest request, String requestContent)
						throws VerifyException, TransforVoFaildedException, LoginFailedException, MiningException {
					// TODO Auto-generated method stub
					//解密后的报文转化为vo
					UserMiningHistoryQuery userMiningHistory = JsonUtil.transforJsonToVO(requestContent, UserMiningHistoryQuery.class);
					//转化失败抛异常
					if(null == userMiningHistory) {
						throw new VerifyException("解密后的报文信息转化vo异常");
					}
					
					List<UserMiningHistoryResponse> respList = new ArrayList<UserMiningHistoryResponse>();
					respList = miningService.queryListByUser(userMiningHistory.getUsername());
					
					map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
					map.put("resultMessage","请求成功");
					map.put("result",respList);
				}
				
				@Override
				public boolean check(HttpServletRequest request, String requestContent) throws VerifyException {
					//解密后的报文转化为vo
					UserMiningHistoryQuery userMiningHistory = JsonUtil.transforJsonToVO(requestContent, UserMiningHistoryQuery.class);
					//转化失败抛异常
					if(null == userMiningHistory) {
						throw new VerifyException("解密后的报文信息转化vo异常");
					}
					
					//根据传入的用户名查询用户
					User user = userService.getUserByUsername(userMiningHistory.getUsername());
					//查询失败时直接返回
					if(null == user) {
						return false;
					}
					
					//获取缓存中的当前用户的token信息
					String tokenInRedis = jedisService.get(user.getUsername());
					//传入的和缓存中的token不一致或者缓存不存在时返回
					if(null == tokenInRedis || tokenInRedis.equals("") ||
							!tokenInRedis.equals(userMiningHistory.getUserToken())) {
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
		} catch (MiningException e) {
			map.put("resultCode", ResponseCodeEnum.DATA_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		} catch (Exception e) {
			map.put("resultCode", ResponseCodeEnum.BAD_REQUEST.getCode());
			map.put("resultMessage", e.getMessage());
		}
		
		return map;
	}

	@RequestMapping(value = "queryUserAccount",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> queryUserAccount(final HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("查询列表开始=========");

		String username = request.getParameter("username");
		logger.info("开始查询[" + username + "]的金币信息");

		try {
			String account = miningService.queryUserAccount(username);
			map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
			map.put("resultMessage","请求成功");
			map.put("totalAmount",account);
		} catch (VerifyException e) {
			map.put("resultCode", ResponseCodeEnum.VERIFY_INVALID.getCode());
			map.put("resultMessage", e.getMessage());
		}

		return map;
	}
}
