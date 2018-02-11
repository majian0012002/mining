package com.miller.mining.mapper;

import com.miller.mining.model.MiningOverview;

public interface MiningOverviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MiningOverview record);

    int insertSelective(MiningOverview record);

    MiningOverview selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MiningOverview record);

    int updateByPrimaryKey(MiningOverview record);
}