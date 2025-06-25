package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.enums.WeatherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    //전체조회
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    //ID 조회
    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    //검색, 조회
    @Query("SELECT t FROM Todo t LEFT JOIN t.user u " +  //사용자 정보 조인해서 전체 todo 조회
            "WHERE (:weather IS NULL OR t.weather = :weather)" +  //weather가 null이면 전체 조회 OR 파라미터로 들어온 ENUM값 weather 조회
            "AND (:start IS NULL OR t.modifiedAt >= :start) " +
            "AND (:end IS NULL OR t.modifiedAt <= :end) " +
            "ORDER BY t.modifiedAt DESC")  //수정일 기준 최신 순 정렬
    Page<Todo> searchTodos(
            @Param("weather") WeatherType weather,
            @Param("Start") LocalDateTime modifiedAtStart,
            @Param("end") LocalDateTime modifiedAtEnd,
            Pageable pageable);
}
