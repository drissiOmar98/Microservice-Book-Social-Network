package com.example.bookserver.services.Impl;

import com.example.bookserver.client.TransactionHistoryClient;
import com.example.bookserver.client.UserClient;
import com.example.bookserver.common.PageResponse;
import com.example.bookserver.dto.*;
import com.example.bookserver.entities.Book;
import com.example.bookserver.exception.AuthenticationException;
import com.example.bookserver.exception.ConnectedUserNotFoundException;
import com.example.bookserver.exception.OperationNotPermittedException;
import com.example.bookserver.file.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

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

    private final FileStorageService fileStorageService;

    private final TransactionHistoryClient transactionHistoryClient;

    private final BookMapper bookMapper;

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


    @Override
    public Integer save(BookRequest request) {
        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = getConnectedUser();

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
        UserDto connectedUser = getConnectedUser();

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
        UserDto connectedUser = getConnectedUser();
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
    public Integer updateShareableStatus(Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));

        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = getConnectedUser();

        if (!Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }

        book.setShareable(!book.isShareable());
        bookRepository.save(book);

        return bookId;
    }

    @Override
    public Integer updateArchivedStatus(Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));

        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = getConnectedUser();

        if (!Objects.equals(book.getOwnerId(), connectedUser.getId())) {
            throw new OperationNotPermittedException("You cannot update others books archived status");
        }

        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    @Override
    public void uploadBookCoverPicture(MultipartFile file, Integer bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));

        // Call the user-server API using the Feign client to get the connected user information
        UserDto connectedUser = getConnectedUser();

        var profilePicture = fileStorageService.saveFile(file, bookId, connectedUser.getId());

        book.setBookCover(profilePicture);

        bookRepository.save(book);


    }


}
