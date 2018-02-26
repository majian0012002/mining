package com.miller.mining.mapper;

import java.util.List;

import com.miller.mining.model.MiningInfo;

public interface MiningInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MiningInfo record);

    int insertSelective(MiningInfo record);

    MiningInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MiningInfo record);

    int updateByPrimaryKey(MiningInfo record);
    
    /**
     * 根据用户名查询正在挖掘的活动
     * @param username
     * @return
     */
    List<MiningInfo> selectActiveByUsername(String username);
}