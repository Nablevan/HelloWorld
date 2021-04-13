package com.example.helloworld.demo.service;

import com.example.helloworld.demo.Model.*;
import com.example.helloworld.demo.dto.CommentDTO;
import com.example.helloworld.demo.enums.CommentTypeEnum;
import com.example.helloworld.demo.enums.NotificationStatusEnum;
import com.example.helloworld.demo.enums.NotificationTypeEnum;
import com.example.helloworld.demo.exception.CustomizeErrorCode;
import com.example.helloworld.demo.exception.CustomizeException;
import com.example.helloworld.demo.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional               //将方法体定义为事务
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

            //更新父评论的评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1L);
            commentExtMapper.incCommentCount(parentComment);

            Notification notification = createNotify(comment, dbComment.getCommentCount(), NotificationTypeEnum.REPLY_COMMENT);
            notificationMapper.insert(notification);//写入通知

        } else {
            //回复问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (dbQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);//写入评论
            dbQuestion.setCommentCount(1);
            questionExtMapper.incCommentCount(dbQuestion);

            Notification notification = createNotify(comment, dbQuestion.getCreator(), NotificationTypeEnum.REPLY_QUESTION);
            notificationMapper.insert(notification);//写入通知
        }

    }

    private Notification createNotify(Comment comment, Long receiver, NotificationTypeEnum notificationType) {
        Notification notification = new Notification();
        notification.setNotifier(comment.getCommentator());
        notification.setOuterid(comment.getParentId());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setType(notificationType.getType());
        return notification;
    }

    public List<CommentDTO> listByTargetId(Long questionId, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(questionId)
                .andTypeEqualTo(type.getType());
        List<Comment> comments = commentMapper.selectByExampleWithBLOBs(commentExample);  //当检索包含大字段时，用WithBLOBs
        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<Long>();
        userIds.addAll(commentators);

        //生成id--user的map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);

        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //装入DTO返回
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(commentDTO.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
