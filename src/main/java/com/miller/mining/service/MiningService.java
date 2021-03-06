package com.miller.mining.service;

import java.text.ParseException;
import java.util.List;

import com.miller.mining.exception.MiningException;
import com.miller.mining.exception.VerifyException;
import com.miller.mining.model.MiningInfo;
import com.miller.mining.model.MiningOverview;
import com.miller.mining.vo.MiningSingleVo;
import com.miller.mining.vo.OrderListVo;
import com.miller.mining.vo.UserMiningHistoryResponse;

public interface MiningService {

	/**
	 * 根据用户名查询当前正在运行的挖掘
	 * @param username
	 * @return
	 * @throws MiningException 
	 */
	public List<MiningInfo> getActiveMiningInfoByUsername(String username) throws MiningException;
	
	public MiningInfo getMiningInfoById(int id);
	
	/**
	 * 开始挖掘
	 * @param miningInfo
	 * @param string 
	 * @return 
	 */
	public int startMining(MiningInfo miningInfo, String username);
	
	/**
	 * 结束挖掘
	 * @param miningInfo
	 * @param username 
	 * @throws VerifyException 
	 * @throws ParseException 
	 */
	public void endMining(MiningInfo miningInfo, String username) throws ParseException, VerifyException;
	
	/**
	 * 计算普通模式的金额
	 * @return
	 */
	public String computeMiningAmoutInOrdinaryMode(MiningInfo mingInfo,MiningSingleVo vo);
	
	/**
	 * 计算运动模式的挖矿金额
	 * @return
	 */
	public String computeMiningAmoutInSportsMode(MiningInfo mingInfo,MiningSingleVo vo);

    List<OrderListVo> queryListByMoenyOrder();

	public List<UserMiningHistoryResponse> queryListByUser(String username) throws MiningException;

	public String queryUserAccount(String username) throws VerifyException;
}
