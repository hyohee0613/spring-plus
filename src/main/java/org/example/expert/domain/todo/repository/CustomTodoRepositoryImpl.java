package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//커스텀 리포지토리 구현 클래스
@Repository
@RequiredArgsConstructor
public class CustomTodoRepositoryImpl implements CustomTodoRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(long todoId) {
        QTodo todo = QTodo.todo;  //QueryDSL로 사용할 Q타입 객체 선언
        QUser user = QUser.user;

        Todo result = queryFactory
                .selectFrom(todo)   // Todo 테이블에서 조회 시작, SELECT * FROM todo
                .leftJoin(todo.user, user).fetchJoin()  //Todo와 연관된 User 엔티티를 LEFT JOIN, fetchJoin()으로  USER는 EAGER로딩
                .where(todo.id.eq(todoId))  //WHERE todo.id = :todoId
                .fetchOne();  //결과를 1건만 조회하고 없으면 null

        return Optional.ofNullable(result);
    }
}

