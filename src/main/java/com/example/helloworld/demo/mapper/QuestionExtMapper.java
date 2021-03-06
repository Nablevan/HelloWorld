package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.Model.QuestionExample;
import com.example.helloworld.demo.dto.QuestionQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incViewCount(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question record);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}