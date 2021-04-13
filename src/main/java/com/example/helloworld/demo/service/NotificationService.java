package com.example.helloworld.demo.service;

import com.example.helloworld.demo.Model.*;
import com.example.helloworld.demo.dto.NotificationDTO;
import com.example.helloworld.demo.dto.PaginationDTO;
import com.example.helloworld.demo.enums.NotificationStatusEnum;
import com.example.helloworld.demo.enums.NotificationTypeEnum;
import com.example.helloworld.demo.mapper.CommentMapper;
import com.example.helloworld.demo.mapper.NotificationMapper;
import com.example.helloworld.demo.mapper.QuestionMapper;
import com.example.helloworld.demo.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    public long unReadNotificationCount(Long userId){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        long notificationCount = notificationMapper.countByExample(notificationExample);
        return notificationCount;
    }

    public PaginationDTO<NotificationDTO> list(Long userId, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        Integer totalPage;
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        Integer targetCount = (int) notificationMapper.countByExample(example);
        if (targetCount % size == 0) {
            totalPage = targetCount / size;
        } else {
            totalPage = targetCount / size + 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offSet = size * (page - 1);

        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offSet,size));

        //获取通知发起者map（去重）
        List<Long> dbUserIds = notifications.stream().map(notification -> notification.getNotifier()).distinct().collect(Collectors.toList());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(dbUserIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //构造DTO
        List<NotificationDTO> notificationDTOS = notifications.stream().map(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);

            notificationDTO.setNotifier(userMap.get(notification.getNotifier()));

            String type = NotificationTypeEnum.getNameByType(notification.getType());
            notificationDTO.setType(type);//通知类型（文字）

            //写入父元素标题 todo 限制字数
            if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType() ||
                    notification.getType() == NotificationTypeEnum.LIKE_QUESTION.getType()){
                //问题相关
                Question dbQuestion = questionMapper.selectByPrimaryKey(notification.getOuterid());
                notificationDTO.setOuterTittle(dbQuestion.getTitle());
            }else if (notification.getType() == NotificationTypeEnum.REPLY_COMMENT.getType()||
                    notification.getType() == NotificationTypeEnum.LIKE_COMMENT.getType()){
                //评论相关
                Comment dbComment = commentMapper.selectByPrimaryKey(notification.getOuterid());
                notificationDTO.setOuterTittle(dbComment.getContent());
            }

            return notificationDTO;
        }).collect(Collectors.toList());

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setTargetDTO(notificationDTOS);
        paginationDTO.setPagination(totalPage, page);
        paginationDTO.setTargetCount(targetCount);
        return paginationDTO;
    }
}
