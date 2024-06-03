package com.example.bookserver.repositories;

import com.example.bookserver.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {
    @Query("""
                 SELECT book
                 FROM Book book
                 WHERE book.archived= false
                 AND book.shareable = true
                 AND book.ownerId = :userId
           """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);


    @Query("SELECT b.ownerId FROM Book b WHERE b.id = :bookId")
    Integer findOwnerIdByBookId(@Param("bookId") Integer bookId);

}
