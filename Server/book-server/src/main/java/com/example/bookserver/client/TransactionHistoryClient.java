package com.example.bookserver.client;

import com.example.bookserver.dto.BookTransactionHistoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "book-transaction-history-server", url = "${application.config.transaction-history-service-url}")
public interface TransactionHistoryClient {

    /*@GetMapping("/transactions/borrowed")
    Page<BookTransactionHistoryDto> getAllBorrowedBooks(@RequestParam("page") int page,
                                                        @RequestParam("size") int size,
                                                        @RequestParam("userId") Integer userId);*/

    @GetMapping("/transactions/borrowed")
    Page<BookTransactionHistoryDto> getAllBorrowedBooks(Pageable pageable, @RequestParam("userId") Integer userId);

    @GetMapping("/transactions/returned")
    Page<BookTransactionHistoryDto> findAllReturnedBooks(Pageable pageable,@RequestParam("userId") Integer userId);
}
