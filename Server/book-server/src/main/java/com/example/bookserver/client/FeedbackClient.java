package com.example.bookserver.client;


import com.example.bookserver.dto.FeedbackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "feedback-server", url = "${application.config.feedback-service-url}")
public interface FeedbackClient {

    @GetMapping("/book/{bookId}")
    List<FeedbackDto> getFeedbacksForBook(@PathVariable("bookId") Integer bookId);
}
