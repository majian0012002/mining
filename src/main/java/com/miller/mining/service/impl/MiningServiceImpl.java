package com.miller.mining.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miller.mining.exception.VerifyException;
import com.miller.mining.mapper.MiningInfoMapper;
import com.miller.mining.mapper.MiningOverviewMapper;
import com.miller.mining.mapper.UserMapper;
import com.miller.mining.model.MiningInfo;
import com.miller.mining.model.MiningOverview;
import com.miller.mining.model.User;
import com.miller.mining.service.MiningService;
import com.miller.mining.utils.DateUtil;

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
	public void startMining(MiningInfo miningInfo, String username) {
		// TODO Auto-generated method stub
		
		User user = userMapper.selectUserByUsername(username);
		logger.info("获取到的用户id: " + user.getId());
		miningInfo.setUserId(user.getId());
		miningInfoMapper.insert(miningInfo);
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
}
