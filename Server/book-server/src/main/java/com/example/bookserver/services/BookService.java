package com.example.bookserver.services;

import com.example.bookserver.common.PageResponse;
import com.example.bookserver.dto.BookRequest;
import com.example.bookserver.dto.BookResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {

    public Integer save(BookRequest request);

    public BookResponse findById(Integer bookId);

    public PageResponse<BookResponse> findAllBooks(int page, int size);

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size);

    public Integer getOwnerByBookId(Integer bookId);

    public Integer updateShareableStatus(Integer bookId);

    public Integer updateArchivedStatus(Integer bookId);

    public void uploadBookCoverPicture(MultipartFile file, Integer bookId);


}
