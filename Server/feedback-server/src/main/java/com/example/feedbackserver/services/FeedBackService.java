package com.example.feedbackserver.services;

import com.example.feedbackserver.common.PageResponse;
import com.example.feedbackserver.dto.FeedbackRequest;
import com.example.feedbackserver.dto.FeedbackResponse;

public interface FeedBackService {

    public Integer save(FeedbackRequest request);

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size);
}
