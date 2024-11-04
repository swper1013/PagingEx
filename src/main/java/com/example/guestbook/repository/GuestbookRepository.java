package com.example.guestbook.repository;

import com.example.guestbook.entity.Guestbook;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook> {
    // 기본 제공되는 findAll(Predicate, Pageable)을 사용할 수 있습니다.
}