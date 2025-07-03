package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomTodoRepository {

    Optional<Todo> findByIdWithUser(long todoId);

    //추가 검색기능
    Page<TodoSearchResponse> search(TodoSearchRequest request, Pageable pageable);
}
