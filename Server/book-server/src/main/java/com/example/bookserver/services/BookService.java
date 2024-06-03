package com.example.bookserver.services;

import com.example.bookserver.common.PageResponse;
import com.example.bookserver.dto.BookRequest;
import com.example.bookserver.dto.BookResponse;
import com.example.bookserver.dto.BorrowedBookResponse;

public interface BookService {

    public Integer save(BookRequest request);

    public BookResponse findById(Integer bookId);

    public PageResponse<BookResponse> findAllBooks(int page, int size);

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size);

    public Integer getOwnerByBookId(Integer bookId);

    public PageResponse<BorrowedBookResponse>  findAllBorrowedBooks(int page, int size, Integer connectedUserId);

    public PageResponse<BorrowedBookResponse>  findAllReturnedBooks(int page, int size, Integer connectedUserId);

    public Integer updateShareableStatus(Integer bookId, Integer connectedUserId);

    public Integer updateArchivedStatus(Integer bookId, Integer connectedUserId);

    public Integer borrowBook(Integer bookId, Integer connectedUserId);
}
