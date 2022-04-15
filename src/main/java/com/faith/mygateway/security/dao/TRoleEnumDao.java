package com.faith.mygateway.security.dao;

import com.faith.mygateway.security.dto.TRoleEnum;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TRoleEnumDao {
    int deleteByPrimaryKey(Long id);

    int insert(TRoleEnum record);

    int insertSelective(TRoleEnum record);

    TRoleEnum selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TRoleEnum record);

    int updateByPrimaryKey(TRoleEnum record);
}