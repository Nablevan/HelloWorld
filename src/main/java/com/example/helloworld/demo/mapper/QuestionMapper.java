package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.dto.QuestionDTO;
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

    @Select("select * from question where id = #{questionId}")
    Question GetQuestionById(@Param(value = "questionId") Integer questionId);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUser(@Param(value = "userId") Integer userId);

    @Select("select * from question where creator = #{userId} limit #{offSet}, #{size}")
    List<Question> listByUser(@Param(value = "userId") Integer userId,
                              @Param(value = "offSet") Integer offSet,
                              @Param(value = "size") Integer size);
}
