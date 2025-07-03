package org.example.expert.domain.todo.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoSearchRequest {

    private String title;             // 제목 키워드
    private String managerNickname;  // 담당자 닉네임 키워드
    private LocalDate startDate;     // 생성일 시작
    private LocalDate endDate;

}
