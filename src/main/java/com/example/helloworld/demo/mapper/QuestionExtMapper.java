package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.Model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incViewCount(Question record);
}