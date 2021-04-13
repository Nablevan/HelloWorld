package com.example.helloworld.demo.Controller;

import com.example.helloworld.demo.Model.Comment;
import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.dto.CommentCreateDTO;
import com.example.helloworld.demo.dto.CommentDTO;
import com.example.helloworld.demo.dto.ResultDTO;
import com.example.helloworld.demo.enums.CommentTypeEnum;
import com.example.helloworld.demo.exception.CustomizeErrorCode;
import com.example.helloworld.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }
        Comment comment = new Comment();
        comment.setCommentator(user.getId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setLikeCount(0L);
        comment.setCommentCount(0L);
        commentService.insert(comment);

        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comment(@PathVariable(name = "commentId") Long commentId){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(commentId, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
