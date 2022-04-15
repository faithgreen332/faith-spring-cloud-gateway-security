package com.faith.mygateway.security.dao;

import com.faith.mygateway.security.dto.TUserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TUserRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(TUserRole record);

    int insertSelective(TUserRole record);

    TUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TUserRole record);

    int updateByPrimaryKey(TUserRole record);
}