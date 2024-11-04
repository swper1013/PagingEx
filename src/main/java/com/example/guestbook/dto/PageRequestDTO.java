package com.example.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {//두 파라미터 수집(화면에 전달되는)
    //JPA 에서 사용하는 pageable타입 객체 생성 단, JPA 이용시 번호 0번부터 시작
    private int page;
    private int size;
    //검색처리
    private String type;
    private String keyword;


    public PageRequestDTO(){
        this.page=1;//파라미터
        this.size=10;//파라미터
    }

    public Pageable getPageable(Sort sort){

        return PageRequest.of(page -1,size,sort);
    }
}
