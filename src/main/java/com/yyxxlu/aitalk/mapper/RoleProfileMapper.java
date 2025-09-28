package com.yyxxlu.aitalk.mapper;



import org.apache.ibatis.annotations.Mapper;
import com.yyxxlu.aitalk.entity.po.RoleProfile;

import java.util.List;

@Mapper
public interface RoleProfileMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleProfile record);

    int insertSelective(RoleProfile record);

    RoleProfile selectByPrimaryKey(String id);

    List<RoleProfile> selectByRoleList();

    int updateByPrimaryKeySelective(RoleProfile record);

    int updateByPrimaryKey(RoleProfile record);
}