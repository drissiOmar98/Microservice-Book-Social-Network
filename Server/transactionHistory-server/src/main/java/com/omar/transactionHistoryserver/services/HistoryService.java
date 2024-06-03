package com.omar.transactionHistoryserver.services;

import com.omar.transactionHistoryserver.common.PageResponse;
import com.omar.transactionHistoryserver.dto.BorrowedBookResponse;
import com.omar.transactionHistoryserver.entities.BookTransactionHistory;

import java.util.Optional;

public interface HistoryService {

    public Integer borrowBook(Integer bookId);

    public Integer returnBorrowedBook(Integer bookId);

    public Integer approveReturnBorrowedBook(Integer bookId);

    boolean isAlreadyBorrowedByUser( Integer bookId, Integer userId);

    public boolean isAlreadyBorrowed(Integer bookId);

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size);

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size);

    public Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId);


}
