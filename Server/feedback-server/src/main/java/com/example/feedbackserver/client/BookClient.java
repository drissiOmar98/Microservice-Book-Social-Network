package com.example.feedbackserver.client;


import com.example.feedbackserver.Config.FeignClientConfig;

import com.example.feedbackserver.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Optional;

@FeignClient(name = "book-server", url = "${application.config.book-server-url}",configuration = FeignClientConfig.class)
public interface BookClient {

    @GetMapping("/api/books/{bookId}/owner")
    Integer getOwnerByBookId(@PathVariable("bookId") Integer bookId);

    //  find all books owned by a particular user.
    @GetMapping("/books/owner/{userId}")
    List<BookDto> getBooksByOwner(@PathVariable("userId") Integer userId);


    @GetMapping("/{bookId}")
    Optional<BookDto> getBookById(@PathVariable("bookId") Integer bookId);


}
