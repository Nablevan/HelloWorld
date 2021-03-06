package com.example.helloworld.demo.service;

import com.example.helloworld.demo.Model.Question;
import com.example.helloworld.demo.Model.QuestionExample;
import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.dto.PaginationDTO;
import com.example.helloworld.demo.dto.QuestionDTO;
import com.example.helloworld.demo.dto.QuestionQueryDTO;
import com.example.helloworld.demo.exception.CustomizeErrorCode;
import com.example.helloworld.demo.exception.CustomizeException;
import com.example.helloworld.demo.mapper.QuestionExtMapper;
import com.example.helloworld.demo.mapper.QuestionMapper;
import com.example.helloworld.demo.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO<QuestionDTO> list(Integer page, Integer size, String search) {
        if (page < 1) {
            page = 1;
        }
        Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();

        if (!StringUtils.isEmpty(search)) {//如果search有值
            if (search.toCharArray().length > 20) {
                search = search.substring(0, 20);
            }
            String[] searchs = StringUtils.split(search, " ");//无法分割时，会返回null
            if (searchs == null) {
                searchs = new String[]{search};
            }
            String searchRegexp = Arrays.stream(searchs).collect(Collectors.joining("|"));
            questionQueryDTO.setSearch(searchRegexp);
        }

        Integer targetCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (targetCount % size == 0) {
            totalPage = targetCount / size;
        } else {
            totalPage = targetCount / size + 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offSet = size * (page - 1);

//        QuestionExample questionExample = new QuestionExample();
//        questionExample.setOrderByClause("gmt_create desc");
//        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offSet, size));

        questionQueryDTO.setPage(offSet);
        questionQueryDTO.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<QuestionDTO>();
        paginationDTO.setTargetDTO(questionDTOList);
        paginationDTO.setPagination(totalPage, page);
        paginationDTO.setTargetCount(targetCount);
        return paginationDTO;
    }

    public PaginationDTO<QuestionDTO> list(Long userId, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        Integer totalPage;
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer targetCount = (int) questionMapper.countByExample(example);
        if (targetCount % size == 0) {
            totalPage = targetCount / size;
        } else {
            totalPage = targetCount / size + 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offSet = size * (page - 1);
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offSet, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<QuestionDTO>();
        paginationDTO.setTargetDTO(questionDTOList);
        paginationDTO.setPagination(totalPage, page);
        paginationDTO.setTargetCount(targetCount);

        return paginationDTO;
    }

    public QuestionDTO GetQuestionById(Long questionId) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        Question dbquestion = questionMapper.selectByPrimaryKey(question.getId());
        if (dbquestion != null) {
            if (dbquestion.getCreator().equals(question.getCreator())) {  //防止修改别人的问题
                // 更新
                question.setGmtModified(System.currentTimeMillis());
                QuestionExample example = new QuestionExample();
                example.createCriteria().andIdEqualTo(question.getId());
                int update = questionMapper.updateByExampleSelective(question, example);
                if (update != 1) {
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
            }
        } else if (question.getId() == null) {   // 新建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);  //不赋初值会存为null
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }
    }

    public void incViewCount(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        questionExtMapper.incViewCount(updateQuestion);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isEmpty(questionDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");//只有一个tag时，会返回null
        if (tags == null) {
            tags = new String[]{questionDTO.getTag()};
        }
        String tagRegexp = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(tagRegexp);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q, dto);
            return dto;
        }).collect(Collectors.toList());

        if (questionDTOS.size() > 5) {
            questionDTOS = questionDTOS.subList(0, 10);
        }
        return questionDTOS;
    }
}
