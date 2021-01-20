package com.example.helloworld.demo.service;

import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.dto.PaginationDTO;
import com.example.helloworld.demo.dto.QuestionDTO;
import com.example.helloworld.demo.mapper.QuestionMapper;
import com.example.helloworld.demo.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        Integer totalPage;
        Integer totalCount = questionMapper.count();
        if (totalCount / size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offSet = size * (page - 1);
        List<Question> questions = questionMapper.list(offSet, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setQuestions(questionDTOList);
        paginationDTO.setPagination(totalPage, page, size);
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        Integer totalPage;
        Integer totalCount = questionMapper.countByUser(userId);
        if (totalCount / size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offSet = size * (page - 1);
        List<Question> questions = questionMapper.listByUser(userId, offSet, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setQuestions(questionDTOList);
        paginationDTO.setPagination(totalPage, page, size);
        return paginationDTO;
    }

    public QuestionDTO GetQuestionById(Integer questionId) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.GetQuestionById(questionId);
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
