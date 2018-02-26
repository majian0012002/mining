package com.miller.mining.service;

import java.text.ParseException;
import java.util.List;

import com.miller.mining.exception.VerifyException;
import com.miller.mining.model.MiningInfo;

public interface MiningService {

	/**
	 * 根据用户名查询当前正在运行的挖掘
	 * @param username
	 * @return
	 */
	public List<MiningInfo> getActiveMiningInfoByUsername(String username);
	
	/**
	 * 开始挖掘
	 * @param miningInfo
	 * @param string 
	 */
	public void startMining(MiningInfo miningInfo, String username);
	
	/**
	 * 结束挖掘
	 * @param miningInfo
	 * @param username 
	 * @throws VerifyException 
	 * @throws ParseException 
	 */
	public void endMining(MiningInfo miningInfo, String username) throws ParseException, VerifyException; 
}
