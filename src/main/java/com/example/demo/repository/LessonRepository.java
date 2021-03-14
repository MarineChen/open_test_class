package com.example.demo.repository;

import com.example.demo.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query(value = "SELECT l.id,l.name,IFNULL(sl.subcount,0) subcount,IFNULL(l.description,'') description FROM lesson l LEFT OUTER JOIN ( SELECT u.lesson_id, count( u.user_id ) subcount FROM user_lesson u GROUP BY u.lesson_id ) sl ON l.id = sl.lesson_id where l.name like ?1",nativeQuery = true)
    Page<Object> findByName(@Param("name") String name, Pageable pageRequest);

}
