package com.yyxxlu.aitalk.mapper;



import org.apache.ibatis.annotations.Mapper;
import com.yyxxlu.aitalk.po.RoleProfile;

@Mapper
public interface RoleProfileMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleProfile record);

    int insertSelective(RoleProfile record);

    RoleProfile selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RoleProfile record);

    int updateByPrimaryKey(RoleProfile record);
}