package com.example.helloworld.demo.Controller;

import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.dto.QuestionDTO;
import com.example.helloworld.demo.mapper.QuestionMapper;
import com.example.helloworld.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{questionId}")
    public String question(@PathVariable(name = "questionId") Integer questionId,
                           Model model){
        QuestionDTO questionDTO = questionService.GetQuestionById(questionId);
        questionService.incViewCount(questionDTO.getId());
        questionDTO.setViewCount(questionDTO.getViewCount() + 1);
        model.addAttribute("question", questionDTO);
        return "question";
    }
}
