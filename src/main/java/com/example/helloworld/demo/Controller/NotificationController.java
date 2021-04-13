package com.example.helloworld.demo.Controller;

import com.example.helloworld.demo.Model.Comment;
import com.example.helloworld.demo.Model.Notification;
import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.dto.PaginationDTO;
import com.example.helloworld.demo.dto.QuestionDTO;
import com.example.helloworld.demo.enums.CommentTypeEnum;
import com.example.helloworld.demo.enums.NotificationStatusEnum;
import com.example.helloworld.demo.enums.NotificationTypeEnum;
import com.example.helloworld.demo.exception.CustomizeErrorCode;
import com.example.helloworld.demo.exception.CustomizeException;
import com.example.helloworld.demo.mapper.CommentMapper;
import com.example.helloworld.demo.mapper.NotificationMapper;
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
public class NotificationController {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/notification/{notificationId}")
    public String notification(@PathVariable(name = "notificationId") Long notificationId,
                               HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        //验证合法性
        Notification dbNotification = notificationMapper.selectByPrimaryKey(notificationId);
        if (dbNotification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (user.getId() != dbNotification.getReceiver()){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_RECEIVER_ERROR);
        }

        //设置为已读
        dbNotification.setStatus(NotificationStatusEnum.HAVE_READ.getStatus());
        notificationMapper.updateByPrimaryKey(dbNotification);

        //跳转问题页面
        Integer notificationType = dbNotification.getType();
        if (notificationType == NotificationTypeEnum.REPLY_QUESTION.getType() ||
        notificationType == NotificationTypeEnum.LIKE_QUESTION.getType()){
            //问题相关
            Question dbQuestion = questionMapper.selectByPrimaryKey(dbNotification.getOuterid());
            return "redirect:/question/" + dbQuestion.getId();
        }else if (notificationType == NotificationTypeEnum.REPLY_COMMENT.getType() ||
                notificationType == NotificationTypeEnum.LIKE_COMMENT.getType()){
            //评论相关
            Comment dbComment = commentMapper.selectByPrimaryKey(dbNotification.getOuterid());
            if (dbComment.getType() == CommentTypeEnum.QUESTION.getType()){
                //评论问题
                Question dbQuestion = questionMapper.selectByPrimaryKey(dbComment.getParentId());
                return "redirect:/question/" + dbQuestion.getId();
            }else {
                //评论其他评论
                Comment parentcomment = commentMapper.selectByPrimaryKey(dbComment.getParentId());
                Question dbQuestion = questionMapper.selectByPrimaryKey(parentcomment.getParentId());
                return "redirect:/question/" + dbQuestion.getId();
            }
        }

        return "index";
    }
}
