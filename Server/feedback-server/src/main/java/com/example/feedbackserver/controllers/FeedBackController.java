package com.example.feedbackserver.controllers;

import com.example.feedbackserver.common.PageResponse;
import com.example.feedbackserver.dto.FeedbackRequest;
import com.example.feedbackserver.dto.FeedbackResponse;
import com.example.feedbackserver.services.FeedBackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Operations related to feedbacks")
public class FeedBackController {

    private final FeedBackService feedBackService;


    @PostMapping("/addFeedBack")
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest request

    ) {
        return ResponseEntity.ok(feedBackService.save(request));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size

    ) {
        return ResponseEntity.ok(feedBackService.findAllFeedbacksByBook(bookId, page, size));
    }


}
