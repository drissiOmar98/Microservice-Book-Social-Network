package com.example.feedbackserver.repositories;

import com.example.feedbackserver.entities.Feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback, Integer> {

    @Query("""
          SELECT feedback
          FROM Feedback  feedback
          WHERE feedback.bookId = :bookId

          """)
    Page<Feedback> findAllByBookId(@Param("bookId")Integer bookId, Pageable pageable);


    @Query("SELECT f FROM Feedback f WHERE f.bookId = :bookId")
    List<Feedback> getAllByBookId(@Param("bookId") Integer bookId);
}
