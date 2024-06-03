package com.example.bookserver.services.Impl;

import com.example.bookserver.client.TransactionHistoryClient;
import com.example.bookserver.client.UserClient;
import com.example.bookserver.common.PageResponse;
import com.example.bookserver.dto.*;
import com.example.bookserver.entities.Book;
import com.example.bookserver.exception.AuthenticationException;
import com.example.bookserver.exception.ConnectedUserNotFoundException;
import com.example.bookserver.exception.OperationNotPermittedException;
import com.example.bookserver.repositories.BookRepository;
import com.example.bookserver.services.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.bookserver.dto.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserClient userClient;

    private final TransactionHistoryClient transactionHistoryClient;

    private final BookMapper bookMapper;


    @Override
    public Integer save(BookRequest request) {

        ResponseEntity<UserDto> responseEntity = userClient.getCurrentUser();

        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle authentication error
            throw new AuthenticationException("Failed to fetch connected user information");
        }

        UserDto connectedUser = responseEntity.getBody();
        if (connectedUser == null) {
            throw new ConnectedUserNotFoundException("Connected user information not found");
        }

        // Proceed with saving the book with the connected user's ID
        Book book = bookMapper.toBook(request);
        book.setOwnerId(connectedUser.getId());
        return bookRepository.save(book).getId();
    }



    @Override
    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
    }

    @Override
    public PageResponse<BookResponse> findAllBooks(int page, int size) {
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
        //UserDto connectedUser = userClient.getUser(connectedUserId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, connectedUser.getId());
        List<BookResponse> booksResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size) {
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
        Page<Book> books = bookRepository.findAll(withOwnerId(connectedUser.getId()), pageable);
        List<BookResponse> booksResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public Integer getOwnerByBookId(Integer bookId) {
        return bookRepository.findOwnerIdByBookId(bookId);
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Integer connectedUserId) {
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

      // Create a PageRequest object
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // Call the BookTransactionHistory-Server to fetch all borrowed books for the user
        Page<BookTransactionHistoryDto> allBorrowedBooks = transactionHistoryClient.getAllBorrowedBooks(pageable, connectedUser.getId());

        // Map BookTransactionHistoryDto to BorrowedBookResponse
        List<BorrowedBookResponse> borrowedBooksResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();


        return new PageResponse<>(
                borrowedBooksResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Integer connectedUserId) {
        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = userClient.getUser(connectedUserId);

        // Create a PageRequest object
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // Call the BookTransactionHistory-Server to fetch all returned books for the user
        Page<BookTransactionHistoryDto> allReturnedBooks= transactionHistoryClient.findAllReturnedBooks(pageable, connectedUser.getId());

        List<BorrowedBookResponse> booksResponse = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()
        );
    }

    @Override
    public Integer updateShareableStatus(Integer bookId, Integer connectedUserId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));

        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = userClient.getUser(connectedUserId);

        if (!Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }

        book.setShareable(!book.isShareable());
        bookRepository.save(book);

        return bookId;
    }

    @Override
    public Integer updateArchivedStatus(Integer bookId, Integer connectedUserId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));

        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = userClient.getUser(connectedUserId);

        if (!Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot update others books archived status");
        }

        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    @Override
    public Integer borrowBook(Integer bookId, Integer connectedUserId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since" +
                    " it is archived or not shareable");
        }

        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = userClient.getUser(connectedUserId);
        if (Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        /*final boolean isAlreadyBorrowedByUser = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, connectedUser.getId());
        if (isAlreadyBorrowedByUser) {
            throw new OperationNotPermittedException("You already borrowed this book " +
                    "and it is still not returned or the return is not approved by the owner");
        }*/




        return null;
    }


}
