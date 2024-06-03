package com.omar.transactionHistoryserver.services.Impl;

import com.omar.transactionHistoryserver.Client.BookClient;
import com.omar.transactionHistoryserver.Client.UserClient;
import com.omar.transactionHistoryserver.common.PageResponse;
import com.omar.transactionHistoryserver.dto.BookDto;
import com.omar.transactionHistoryserver.dto.BorrowedBookResponse;
import com.omar.transactionHistoryserver.dto.UserDto;
import com.omar.transactionHistoryserver.dto.bookMapper;
import com.omar.transactionHistoryserver.entities.BookTransactionHistory;
import com.omar.transactionHistoryserver.exception.AuthenticationException;
import com.omar.transactionHistoryserver.exception.ConnectedUserNotFoundException;
import com.omar.transactionHistoryserver.exception.OperationNotPermittedException;
import com.omar.transactionHistoryserver.repositories.BookTransactionHistoryRepository;
import com.omar.transactionHistoryserver.services.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    private final UserClient userClient;

    private final bookMapper bookMapper;

    private final BookClient bookClient;


    @Override
    public Integer borrowBook(Integer bookId) {
        BookDto book = bookClient.getBookById(bookId);
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        // Call the user-server API using the Feign client to get the connected user information
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        if (Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        long countIsAlreadyBorrowedByUser = bookTransactionHistoryRepository.countAlreadyBorrowedByUser(bookId, connectedUser.getId());
        if (countIsAlreadyBorrowedByUser > 0) {
            throw new OperationNotPermittedException("You have already borrowed this book and it is still not returned or the return is not approved by the owner");
        }

        long countIsAlreadyBorrowedByOtherUser = bookTransactionHistoryRepository.countBorrowedBooks(bookId);
        if(countIsAlreadyBorrowedByOtherUser > 0){
            throw  new OperationNotPermittedException("The requested book is already borrowed");

        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .userId(connectedUser.getId())
                .bookId(bookId)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    @Override
    public Integer returnBorrowedBook(Integer bookId) {
        BookDto book = bookClient.getBookById(bookId);
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book is archived or not shareable");
        }

        // Call the user-server API using the Feign client to get the connected user information
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        if (Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, connectedUser.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    @Override
    public Integer approveReturnBorrowedBook(Integer bookId) {
        BookDto book = bookClient.getBookById(bookId);
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book is archived or not shareable");
        }


        // Call the user-server API using the Feign client to get the connected user information
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        if (!Objects.equals(book.getOwnerId(),connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot approve the return of a book you do not own");
        }

        // Check if the book's return has already been approved
        Optional<BookTransactionHistory> approvedTransaction = bookTransactionHistoryRepository.findApprovedTransactionByBookId(bookId);
        if (approvedTransaction.isPresent()) {
            throw new OperationNotPermittedException("The return of this book has already been approved");
        }

        // Find the transaction history for the specified book using orElseThrow
        BookTransactionHistory transaction = bookTransactionHistoryRepository.findByBookId(bookId)
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));

        // Approve return
        transaction.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(transaction).getId();
    }

    public Integer getBookOwnerId(Integer bookId) {
        // Fetch book details using the Feign client
        BookDto bookDto = bookClient.getBookById(bookId);
        if (bookDto != null) {
            return bookDto.getOwnerId();
        }
        return null; // or throw an exception if bookDto is null
    }



    @Override
    public boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId) {
        long count= bookTransactionHistoryRepository.countAlreadyBorrowedByUser(bookId,userId);
        return count > 0;
    }

    @Override
    public boolean isAlreadyBorrowed(Integer bookId) {
        long count = bookTransactionHistoryRepository.countBorrowedBooks(bookId);
        return count > 0;
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size) {
        // Call the user-server API using the Feign client to get the connected user information
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());


        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, connectedUser.getId());
        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );

    }

    /*@Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size) {

        // Call the user-server API using the Feign client to get the connected user information
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // Fetch books owned by the user
        List<BookDto> ownedBooks = bookClient.getBooksByOwner(connectedUser.getId());
        if (ownedBooks == null || ownedBooks.isEmpty()) {
            // Return an empty response if no books are found
            return new PageResponse<>(Collections.emptyList(), page, size, 0, 0, true, true);
        }
        List<Integer> bookIds = ownedBooks.stream().map(BookDto::getId).collect(Collectors.toList());

        // Fetch transaction histories for these books
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllByBookIdInAndReturnedTrue(pageable, bookIds);

        // Map to response DTOs
        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );

    }*/

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size) {
        // Call the user-server API using the Feign client to get the connected user information
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // Fetch books owned by the user using the updated bookClient method
        PageResponse<BookDto> ownedBooksResponse = bookClient.findAllBooksByOwner(page, size);
        List<BookDto> ownedBooks = ownedBooksResponse.getContent();
        if (ownedBooks == null || ownedBooks.isEmpty()) {
            // Return an empty response if no books are found
            return new PageResponse<>(Collections.emptyList(), page, size, 0, 0, true, true);
        }
        List<Integer> bookIds = ownedBooks.stream().map(BookDto::getId).collect(Collectors.toList());

        // Fetch transaction histories for these books
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllByBookIdInAndReturnedTrue(pageable, bookIds);

        // Map to response DTOs
        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    @Override
    public Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId) {
        // Call the user-service API using the Feign client to get the connected user information
        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        Integer userId = connectedUser.getId();

        // Fetch book details using the Feign client
        BookDto bookDto = bookClient.getBookById(bookId);
        if (bookDto == null || !bookDto.getOwnerId().equals(userId)) {
            return Optional.empty();
        }

        // Fetch the transaction history
        return bookTransactionHistoryRepository.findByBookId(bookId);

    }


}
