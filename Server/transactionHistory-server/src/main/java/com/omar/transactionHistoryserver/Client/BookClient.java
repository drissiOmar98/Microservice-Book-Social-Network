package com.omar.transactionHistoryserver.Client;


import com.omar.transactionHistoryserver.common.PageResponse;
import com.omar.transactionHistoryserver.config.FeignClientConfig;
import com.omar.transactionHistoryserver.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "book-server", url = "${application.config.book-server-url}",configuration = FeignClientConfig.class)
public interface BookClient {

    @GetMapping("/api/books/{bookId}/owner")
    Integer getOwnerByBookId(@PathVariable("bookId") Integer bookId);

    //  find all books owned by a particular user.
    @GetMapping("/books/owner/{userId}")
    List<BookDto> getBooksByOwner(@PathVariable("userId") Integer userId);


    @GetMapping("/{bookId}")
    BookDto getBookById(@PathVariable("bookId") Integer bookId);

    //  find all books owned by a particular user.
    @GetMapping("/owner")
    PageResponse<BookDto> findAllBooksByOwner(
            @RequestParam(name = "page" , defaultValue = "0", required = false) int page,
            @RequestParam(name = "size" ,defaultValue = "10", required = false) int size
    );
}
