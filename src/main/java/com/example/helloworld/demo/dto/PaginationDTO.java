package com.example.helloworld.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showLastPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        Integer totalPage;
        if (totalCount / size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        pages.add(page);
        for (int i = 1; i < 4; i++) {
            if (page - i > 0) {
                pages.add(0,page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        //是否展示上一页
        showPrevious = page != 1;
        //是否展示下一页
        showNext = !page.equals(totalPage);
        //是否展示第一页
        showFirstPage = !pages.contains(1);
        //是否展示尾页
        showLastPage = !pages.contains(totalPage);
    }
}
