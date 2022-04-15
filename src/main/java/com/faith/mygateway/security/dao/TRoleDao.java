package com.faith.mygateway.security.dao;

import com.faith.mygateway.security.dto.TRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(TRole record);

    int insertSelective(TRole record);

    TRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);
}