package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("INSERT INTO QUESTION ( TITLE, DESCRIPTION, TAG, gmt_create, gmt_modified, CREATOR)" +
            " VALUES ( #{title}, #{description}, #{tag}, #{gmtCreate}, #{gmtModified}, #{creator})")
    void create(Question questionModel);

    @Select("select * from question")
    List<Question> list();
}
