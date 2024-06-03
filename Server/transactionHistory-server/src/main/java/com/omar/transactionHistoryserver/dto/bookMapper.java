package com.omar.transactionHistoryserver.dto;

import com.omar.transactionHistoryserver.Client.BookClient;
import com.omar.transactionHistoryserver.entities.BookTransactionHistory;
import com.omar.transactionHistoryserver.repositories.BookTransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class bookMapper {


    @Autowired
    private BookClient bookClient;


    @Autowired
    BookTransactionHistoryRepository bookTransactionHistoryRepository;


    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        BookDto book = bookClient.getBookById(history.getBookId());
        return BorrowedBookResponse.builder()
                .id(history.getBookId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                //.rate(book.getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }


}
