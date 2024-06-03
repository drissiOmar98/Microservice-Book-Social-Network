package com.example.bookserver.dto;

import com.example.bookserver.client.UserClient;
import com.example.bookserver.entities.Book;
import com.example.bookserver.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookMapper {

    @Autowired
    private UserClient userClient;

    @Autowired
    private BookRepository bookRepository;

    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .isbn(request.isbn())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        // Fetch the owner's details from the User-server
        UserDto owner = userClient.getOwner(book.getOwnerId());
        // Construct the owner's full name using the fetched details
        String ownerFullName = owner.getFirstname() + " " + owner.getLastname();

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                //.rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .ownerId(book.getOwnerId())
                .owner(ownerFullName)
                //.cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistoryDto history) {

        Book book = bookRepository.findById(history.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + history.getBookId() + " not found"));

        return BorrowedBookResponse.builder()
                .id(history.getBookId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .rate(book.getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
