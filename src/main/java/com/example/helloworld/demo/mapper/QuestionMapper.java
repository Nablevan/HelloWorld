package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("INSERT INTO QUESTION ( TITLE, DESCRIPTION, TAG, gmt_create, gmt_modified, CREATOR)" +
            " VALUES ( #{title}, #{description}, #{tag}, #{gmtCreate}, #{gmtModified}, #{creator})")
    void create(Question questionModel);

    @Select("select * from question limit #{offSet}, #{size}")
    List<Question> list(@Param(value = "offSet") Integer offSet, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();
}
