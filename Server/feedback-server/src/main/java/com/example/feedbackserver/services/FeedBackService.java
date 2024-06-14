package com.example.feedbackserver.services;

import com.example.feedbackserver.common.PageResponse;
import com.example.feedbackserver.dto.FeedbackRequest;
import com.example.feedbackserver.dto.FeedbackResponse;

import java.util.List;

public interface FeedBackService {

    public Integer save(FeedbackRequest request);

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size);

    public List<Double> findAllNotesByBook(Integer bookId);
}
