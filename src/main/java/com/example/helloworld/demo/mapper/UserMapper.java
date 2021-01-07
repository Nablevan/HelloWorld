package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO USER ( name, account_id, token, gmt_create, gmt_modified, avatar_url)" +
            " VALUES ( #{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
        //#{token}表示引用形参
    User findByToken(@Param("token") String token);             //形参不是类时，要加@Param注解
}

