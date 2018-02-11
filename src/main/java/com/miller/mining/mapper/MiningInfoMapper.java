package com.miller.mining.mapper;

import com.miller.mining.model.MiningInfo;

public interface MiningInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MiningInfo record);

    int insertSelective(MiningInfo record);

    MiningInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MiningInfo record);

    int updateByPrimaryKey(MiningInfo record);
}