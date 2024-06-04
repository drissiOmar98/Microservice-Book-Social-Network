package com.example.feedbackserver.services.Impl;

import com.example.feedbackserver.client.BookClient;
import com.example.feedbackserver.client.UserClient;
import com.example.feedbackserver.common.PageResponse;
import com.example.feedbackserver.dto.*;
import com.example.feedbackserver.entities.Feedback;
import com.example.feedbackserver.exception.AuthenticationException;
import com.example.feedbackserver.exception.ConnectedUserNotFoundException;
import com.example.feedbackserver.exception.OperationNotPermittedException;
import com.example.feedbackserver.repositories.FeedBackRepository;
import com.example.feedbackserver.services.FeedBackService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedbackMapper feedbackMapper;

    private final UserClient userClient;

    private final BookClient bookClient;

    private final FeedBackRepository feedBackRepository;

    @Override
    public Integer save(FeedbackRequest request) {
        BookDto book = bookClient.getBookById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + request.bookId()));;

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give a feedback for and archived or not shareable book");
        }

        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = getConnectedUser();

        if (Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot give feedback to your own book");
        }

        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedBackRepository.save(feedback).getId();


    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = getConnectedUser();

        Page<Feedback> feedbacks = feedBackRepository.findAllByBookId(bookId, pageable);

        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, connectedUser.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );


    }


    private UserDto getConnectedUser() {
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        return connectedUser;
    }
}
