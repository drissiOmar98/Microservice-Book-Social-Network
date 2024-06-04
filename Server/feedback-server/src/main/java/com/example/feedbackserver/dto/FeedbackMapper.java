package com.example.feedbackserver.dto;

import com.example.feedbackserver.client.BookClient;
import com.example.feedbackserver.entities.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackMapper {

    private final BookClient bookClient;

    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .bookId(request.bookId())
                .build();
    }


    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {

        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }

}
