package com.example.helloworld.demo.mapper;

import com.example.helloworld.demo.Model.Comment;
import com.example.helloworld.demo.Model.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}