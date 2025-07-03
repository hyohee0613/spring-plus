package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public Page<TodoSearchResponse> search(TodoSearchRequest request, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QComment comment = QComment.comment;
        QManager manager = QManager.manager;
        QUser user = QUser.user;

        List<TodoSearchResponse> results = queryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        manager.id.countDistinct(),   //중복없는 담당자 수
                        comment.id.countDistinct()    //중복없는 댓글 수
                ))
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))  //해당 일정의 담당자 정보
                .leftJoin(user).on(manager.user.eq(user))  //담당자의 닉네임을 검색
                .leftJoin(comment).on(comment.todo.eq(todo))  //해당 일정에 달린 댓글
                .where(
                        titleContains(request.getTitle()),
                        nicknameContains(request.getManagerNickname()),
                        createdBetween(request.getStartDate(), request.getEndDate())
                )
                .groupBy(todo.id)  //각 Todo 별로 담당자 수, 댓글 수를 세기 위함
                .orderBy(todo.createdAt.desc())  //생성일 기준 최신순 정렬
                .offset(pageable.getOffset())  //페이징, 시작 인덱스
                .limit(pageable.getPageSize()) //페이징, 한 페이지에 가져올 갯수
                .fetch();

        return new PageImpl<>(results, pageable, results.size());  //결과를 페이징하여 반환
    }

    //제목 검색 (부분 일치), 대소문자 구분x
    private BooleanExpression titleContains(String title) {
        return title != null ? QTodo.todo.title.containsIgnoreCase(title) : null;
        //삼항 연산자(조건 ? 참 : 거짓)
        //title != null: title 검색어가 입력되었는지 체크
        //QueryDSL에서는 .where(...) 안에 null이 있으면 자동으로 무시됨(null이면 이 조건은 무시하고 다른 조건만 적용하여 조회)
    }

    //담당자 닉네임 검색
    private BooleanExpression nicknameContains(String nickname) {
        return nickname != null ? QUser.user.nickName.containsIgnoreCase(nickname) : null;
    //null이면 이 조건은 무시하고 다른 조건만 적용하여 조회
    }

    //생성일 검색
    //일정(Todo)의 createdAt 필드가 시작일과 종료일 사이에 포함되는지 확인
    private BooleanExpression createdBetween(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return QTodo.todo.createdAt.between(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        }
        return null;
    }

}

