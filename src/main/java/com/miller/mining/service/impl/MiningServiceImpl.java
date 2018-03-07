package com.miller.mining.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.miller.mining.vo.OrderListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miller.mining.comm.MiningRuleConstant;
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

	@Override
	public List<MiningInfo> getActiveMiningInfoByUsername(String username) {
		// TODO Auto-generated method stub
		List<MiningInfo> miningList = miningInfoMapper.selectActiveByUsername(username);
		return miningList;
	}
	
	@Override
	public int startMining(MiningInfo miningInfo, String username) {
		// TODO Auto-generated method stub
		
		User user = userMapper.selectUserByUsername(username);
		logger.info("获取到的用户id: " + user.getId());
		miningInfo.setUserId(user.getId());
		int id = miningInfoMapper.insertSelective(miningInfo);
		return id;
	}

	@Override
	public void endMining(MiningInfo miningInfo, String username) throws ParseException, VerifyException {
		// TODO Auto-generated method stub
		User user = userMapper.selectUserByUsername(username);
		logger.info("获取到的用户id: " + user.getId());
		miningInfo.setUserId(user.getId());
		//结束本次挖掘后需要更新汇总信息
		MiningOverview overview = overviewMapper.selectByUser(user.getId());
		
		//如果查询结果为空，就添加新的
		if(null == overview) {
			MiningOverview newOverview = new MiningOverview();
			newOverview.setTotalAmount(miningInfo.getMiningAmount());
			newOverview.setTotalMile(miningInfo.getRunningMile());
			newOverview.setTotalTime(new BigDecimal(DateUtil.getDistanceOfDate(miningInfo.getEndTime(), miningInfo.getStartTime(), 
					"yyyy-MM-dd HH:mm:ss")));
			newOverview.setUserId(user.getId());
			overviewMapper.insert(newOverview);
		} else {
			overview.setTotalAmount(overview.getTotalAmount().add(miningInfo.getMiningAmount()));
			overview.setTotalMile(overview.getTotalMile().add(miningInfo.getRunningMile()));
			overview.setTotalTime(overview.getTotalTime().add(new BigDecimal(DateUtil.getDistanceOfDate(miningInfo.getEndTime(), miningInfo.getStartTime(), 
					"yyyy-MM-dd HH:mm:ss"))));
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
		int mileDistance = vo.getCurrentMile() - vo.getLastMile();
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

}
