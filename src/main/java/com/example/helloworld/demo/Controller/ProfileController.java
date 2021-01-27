package com.example.helloworld.demo.Controller;

import com.example.helloworld.demo.Model.QuestionExample;
import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.dto.PaginationDTO;
import com.example.helloworld.demo.mapper.QuestionMapper;
import com.example.helloworld.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/profile/{action}")
    public String profile(
            HttpServletRequest request,
            @PathVariable(name = "action") String action, Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if (action == null) {
            action = "questions";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(user.getId());
            Integer totalCount = (int) questionMapper.countByExample(questionExample);
//                    Integer totalCount = questionMapper.countByUser(user.getId());
            if (totalCount == 0) {
                model.addAttribute("totalCount", 0);
            } else {
                model.addAttribute("totalCount", 1);
            }
            PaginationDTO pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", pagination);
        }
        if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        return "profile";
    }
}
