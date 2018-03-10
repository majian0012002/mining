package com.miller.mining.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import com.miller.mining.vo.OrderListVo;
import com.miller.mining.vo.UserMiningHistoryResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miller.mining.comm.MiningRuleConstant;
import com.miller.mining.exception.MiningException;
import com.miller.mining.exception.VerifyException;
import com.miller.mining.mapper.MiningInfoMapper;
import com.miller.mining.mapper.MiningOverviewMapper;
import com.miller.mining.mapper.UserMapper;
import com.miller.mining.model.MiningInfo;
import com.miller.mining.model.MiningOverview;
import com.miller.mining.model.User;
import com.miller.mining.service.MiningService;
import com.miller.mining.utils.DateUtil;
import com.miller.mining.vo.MiningSingleVo;

@Service
public class MiningServiceImpl implements MiningService {

	private Logger logger = LoggerFactory.getLogger(MiningServiceImpl.class);
	
	@Autowired
	private MiningInfoMapper miningInfoMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private MiningOverviewMapper overviewMapper;
	private MiningOverview overview;

	@Override
	public List<MiningInfo> getActiveMiningInfoByUsername(String username) throws MiningException {
		// TODO Auto-generated method stub
		User user = userMapper.selectUserByUsername(username);
		if(null == user) {
			throw new MiningException("根据user[" + username + "]获取不到用户");
		}
		
		List<MiningInfo> miningList = miningInfoMapper.selectActiveByUserID(user.getId());
		return miningList;
	}
	
	@Override
	public int startMining(MiningInfo miningInfo, String username) {
		// TODO Auto-generated method stub
		
		User user = userMapper.selectUserByUsername(username);
		logger.info("获取到的用户id: " + user.getId());
		miningInfo.setUserId(user.getId());
		miningInfoMapper.insertSelective(miningInfo);
		return miningInfo.getId();
	}

	@Override
	public void endMining(MiningInfo miningInfo, String username) throws ParseException, VerifyException {
		// TODO Auto-generated method stub
		User user = userMapper.selectUserByUsername(username);
		logger.info("获取到的用户id: " + user.getId());
		miningInfo.setUserId(user.getId());
		miningInfoMapper.updateByPrimaryKey(miningInfo);
		//结束本次挖掘后需要更新汇总信息
		MiningOverview overview = overviewMapper.selectByUser(user.getId());
		
		//如果查询结果为空，就添加新的
		if(null == overview) {
			MiningOverview newOverview = new MiningOverview();
			newOverview.setTotalAmount(miningInfo.getMiningAmount());
			newOverview.setTotalMile(miningInfo.getRunningMile());
			newOverview.setTotalTime(new BigDecimal(DateUtil.getDistanceOfDate(miningInfo.getEndTime(), miningInfo.getStartTime(), 
					"yyyy-MM-ddHH:mm:ss")));
			newOverview.setUserId(user.getId());
			overviewMapper.insert(newOverview);
		} else {
			overview.setTotalAmount(overview.getTotalAmount().add(miningInfo.getMiningAmount()));
			overview.setTotalMile(overview.getTotalMile().add(miningInfo.getRunningMile()));
			overview.setTotalTime(overview.getTotalTime().add(new BigDecimal(DateUtil.getDistanceOfDate(miningInfo.getEndTime(), miningInfo.getStartTime(), 
					"yyyy-MM-ddHH:mm:ss"))));
			overviewMapper.updateByPrimaryKey(overview);
		}
	}

	@Override
	public String computeMiningAmoutInOrdinaryMode(MiningInfo mingInfo, MiningSingleVo vo) {
		// TODO Auto-generated method stub
		BigDecimal duringTime = new BigDecimal((vo.getDuringTime() == null || vo.getDuringTime().equals("")) ? 
				"30" : vo.getDuringTime());
		BigDecimal amout = duringTime.divide(new BigDecimal(30)).multiply(new BigDecimal(MiningRuleConstant.COIN_OF_PER_HALF_MINUTES));
		amout.setScale(5, BigDecimal.ROUND_HALF_UP);
		
		//更新overview
		return amout.toString();
	}

	@Override
	public String computeMiningAmoutInSportsMode(MiningInfo mingInfo, MiningSingleVo vo) {
		//根据时间计算挖掘数量
		BigDecimal duringTime = new BigDecimal((vo.getDuringTime() == null || vo.getDuringTime().equals("")) ? 
				"30" : vo.getDuringTime());
		BigDecimal timeAmout = duringTime.divide(new BigDecimal(30)).multiply(new BigDecimal(MiningRuleConstant.COIN_OF_PER_HALF_MINUTES));
		//根据里程计算挖掘数量
		int mileDistance = Integer.parseInt(vo.getCurrentMile()) - Integer.parseInt(vo.getLastMile());
		mileDistance = mileDistance > 833 ? 833 : mileDistance;
		BigDecimal mileAmout = new BigDecimal((double)mileDistance / 1000).multiply(new BigDecimal(MiningRuleConstant.COIN_OF_PER_MILE));
		mileAmout = mileAmout.max(new BigDecimal(4.615));
		
		BigDecimal total = timeAmout.add(mileAmout);
		total.setScale(5, BigDecimal.ROUND_HALF_UP);
		return total.toString();
	}

	@Override
	public List<OrderListVo> queryListByMoenyOrder() {
		List<OrderListVo> resultList = new ArrayList<OrderListVo>();
		List<MiningOverview> miningOverviewList = new ArrayList<MiningOverview>();
		miningOverviewList = overviewMapper.queryListByMoneyOrder();
		if(null != miningOverviewList && miningOverviewList.size() > 0) {
			for (MiningOverview miningOverview : miningOverviewList) {
				OrderListVo order = new OrderListVo();
				User user = userMapper.selectByPrimaryKey(miningOverview.getUserId());
				if(null == user) {
					continue;
				}
				order.setUsername(user.getUsername());
				order.setMoney(miningOverview.getTotalAmount().toString());
				resultList.add(order);
			}
			return resultList;
		}
		return null;
	}

	@Override
	public MiningInfo getMiningInfoById(int id) {
		// TODO Auto-generated method stub
		MiningInfo info = miningInfoMapper.selectByPrimaryKey(id);
		return info;
	}

	@Override
	public List<UserMiningHistoryResponse> queryListByUser(String username) throws MiningException {
		// TODO Auto-generated method stub
		User user = userMapper.selectUserByUsername(username);
		if(null == user) {
			throw new MiningException("根据user[" + username + "]获取不到用户");
		}
		
		List<MiningInfo> miningInfoList = new ArrayList<MiningInfo>();
		miningInfoList = miningInfoMapper.selectDeactiveByUserID(user.getId());
		if(miningInfoList.size() > 0) {
			List<UserMiningHistoryResponse> respList = new ArrayList<UserMiningHistoryResponse>();
			for(MiningInfo info : miningInfoList) {
				UserMiningHistoryResponse resp = new UserMiningHistoryResponse();
				resp.setStartTime(info.getStartTime());
				resp.setEndTime(info.getEndTime());
				resp.setTotalTime(info.getRunningTime().setScale(5, BigDecimal.ROUND_HALF_UP).toString());
				resp.setTotalMoney(info.getMiningAmount().setScale(5, BigDecimal.ROUND_HALF_UP).toString());
				resp.setTotalMiles(info.getRunningMile().setScale(5, BigDecimal.ROUND_HALF_UP).toString());
				respList.add(resp);
			}
			Collections.sort(respList, new Comparator<UserMiningHistoryResponse>() {
				@Override
				public int compare(UserMiningHistoryResponse o1, UserMiningHistoryResponse o2) {
					try {
						Date date1 = DateUtil.getDateFromDateStr("yyyy-MM-ddHH:mm:ss",o1.getEndTime());
						Date date2 = DateUtil.getDateFromDateStr("yyyy-MM-ddHH:mm:ss",o2.getEndTime());
						return date1.getTime() > date2.getTime() ? 1 : -1;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return 0;
				}
			});
			return respList;
		}
		return null;
	}

	@Override
	public String queryUserAccount(String username) throws VerifyException {
		User user = userMapper.selectUserByUsername(username);

		if (null == user) {
			throw new VerifyException("无法根据用户名【" + username +"】查询用户");

		}

		MiningOverview miningOverview = overviewMapper.selectByUser(user.getId());
		String account = miningOverview.getTotalAmount().toString();
		return account;
	}

}
