package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO USER ( name, account_id, token, gmt_create, gmt_modified, avatar_url)" +
            " VALUES ( #{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);             //#{token}表示引用形参,形参不是类时，要加@Param注解

    @Select("select * from user where ID = #{ID}")
    User findById(@Param("ID") Integer ID);

    @Select("select * from user where ACCOUNT_ID = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name=#{name}, token=#{token}, gmt_modified=#{gmtModified}, avatar_url=#{avatarUrl} WHERE ID = #{id}")
    void update(User user);
}

