package com.miller.mining.mapper;

import com.miller.mining.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询用户名为username的数量
     * @param username
     * @return
     */
    int countByUsername(String username);

    /**
     * 根据传参查询用户
     * @param user
     * @return
     */
    User selectUser(User user);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User selectUserByUsername(String username);
}