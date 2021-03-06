package com.example.helloworld.demo.Controller;

import com.example.helloworld.demo.dto.CommentDTO;
import com.example.helloworld.demo.dto.QuestionDTO;
import com.example.helloworld.demo.enums.CommentTypeEnum;
import com.example.helloworld.demo.service.CommentService;
import com.example.helloworld.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{questionId}")
    public String question(@PathVariable(name = "questionId") Long questionId,
                           Model model){
        QuestionDTO questionDTO = questionService.GetQuestionById(questionId);
        questionService.incViewCount(questionDTO.getId());
        questionDTO.setViewCount(questionDTO.getViewCount() + 1);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
