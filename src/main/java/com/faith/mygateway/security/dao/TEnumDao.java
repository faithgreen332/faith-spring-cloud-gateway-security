package com.faith.mygateway.security.dao;

import com.faith.mygateway.security.dto.TEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TEnumDao {
    int deleteByPrimaryKey(Long id);

    int insert(TEnum record);

    int insertSelective(TEnum record);

    TEnum selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TEnum record);

    int updateByPrimaryKey(TEnum record);

    List<TEnum> selectAll();
}