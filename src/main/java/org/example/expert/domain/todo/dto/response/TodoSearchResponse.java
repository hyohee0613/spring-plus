package org.example.expert.domain.todo.dto.response;

public class TodoSearchResponse {

    private String title;
    private long managerCount;
    private long commentCount;

    public TodoSearchResponse(String title, long managerCount, long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }

}
