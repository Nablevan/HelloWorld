package com.example.helloworld.demo.Controller;

import com.example.helloworld.demo.Model.QuestionExample;
import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.dto.NotificationDTO;
import com.example.helloworld.demo.dto.PaginationDTO;
import com.example.helloworld.demo.dto.QuestionDTO;
import com.example.helloworld.demo.enums.NotificationTypeEnum;
import com.example.helloworld.demo.mapper.QuestionMapper;
import com.example.helloworld.demo.service.NotificationService;
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
    @Autowired
    private NotificationService notificationService;

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

        //未读通知数
        long notificationCount = notificationService.unReadNotificationCount(user.getId());
        model.addAttribute("notificationCount", notificationCount);

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");

            PaginationDTO<QuestionDTO> pagination = questionService.list(user.getId(), page, size);

            if (pagination.getTargetCount() == 0) {
                model.addAttribute("totalCount", 0);
            } else {
                model.addAttribute("totalCount", 1);
            }
            model.addAttribute("pagination", pagination);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");

            PaginationDTO<NotificationDTO> pagination = notificationService.list(user.getId(), page, size);

            if (pagination.getTargetCount() == 0) {
                model.addAttribute("totalCount", 0);
            } else {
                model.addAttribute("totalCount", 1);
            }

            model.addAttribute("pagination", pagination);
        }
        return "profile";
    }
}
