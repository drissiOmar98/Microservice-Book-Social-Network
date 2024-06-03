package com.omar.transactionHistoryserver.repositories;

import com.omar.transactionHistoryserver.entities.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {

    @Query("""
            SELECT  history
            FROM BookTransactionHistory history
            WHERE history.userId = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, @Param("userId") Integer userId);




    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.bookId IN :bookIds
            """)
    Page<BookTransactionHistory> findByBookIdIn(Pageable pageable, @Param("bookIds") List<Integer> bookIds);




    @Query("""
        SELECT COUNT(bookTransactionHistory)  
        FROM BookTransactionHistory bookTransactionHistory
        WHERE bookTransactionHistory.bookId = :bookId
        AND bookTransactionHistory.returnApproved = false
        """)
    long countBorrowedBooks(@Param("bookId") Integer bookId);


    @Query("""
            SELECT
            COUNT (bookTransactionHistory) 
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.userId = :userId
            AND bookTransactionHistory.bookId = :bookId
            AND bookTransactionHistory.returnApproved = false
            """)
    long countAlreadyBorrowedByUser(@Param("bookId") Integer bookId, @Param("userId") Integer userId);





    @Query("""
            SELECT transaction
            FROM BookTransactionHistory  transaction
            WHERE transaction.userId = :userId
            AND transaction.bookId = :bookId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(@Param("bookId") Integer bookId, @Param("userId") Integer userId);



    /*@Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);*/


    @Query("""
        SELECT history
        FROM BookTransactionHistory history
        WHERE history.bookId IN :bookIds AND history.returned = true
    """)
    Page<BookTransactionHistory> findAllByBookIdInAndReturnedTrue(Pageable pageable, @Param("bookIds") List<Integer> bookIds);


    @Query("""
        SELECT transaction
        FROM BookTransactionHistory transaction
        WHERE transaction.bookId = :bookId
        AND transaction.returned = true
        AND transaction.returnApproved = false
    """)
    Optional<BookTransactionHistory> findByBookId(@Param("bookId") Integer bookId);

    @Query("""
    SELECT transaction
    FROM BookTransactionHistory transaction
    WHERE transaction.bookId = :bookId
    AND transaction.returned = true
    AND transaction.returnApproved = true
    """)
    Optional<BookTransactionHistory> findApprovedTransactionByBookId(@Param("bookId") Integer bookId);


















}
