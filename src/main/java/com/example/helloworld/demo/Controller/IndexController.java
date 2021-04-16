package com.example.helloworld.demo.Controller;

import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.dto.PaginationDTO;
import com.example.helloworld.demo.dto.QuestionDTO;
import com.example.helloworld.demo.mapper.QuestionMapper;
import com.example.helloworld.demo.mapper.UserMapper;
import com.example.helloworld.demo.service.NotificationService;
import com.example.helloworld.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search) {
        PaginationDTO<QuestionDTO> pagination = questionService.list(page, size, search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "index";
    }
}
