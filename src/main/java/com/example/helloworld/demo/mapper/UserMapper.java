package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO USER ( name, account_id, token, gmt_create, gmt_modified )" +
            " VALUES ( #{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified} )")
    void insert(User user);
}

