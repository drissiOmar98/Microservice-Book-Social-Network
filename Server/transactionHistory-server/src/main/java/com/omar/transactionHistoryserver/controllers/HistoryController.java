package com.omar.transactionHistoryserver.controllers;


import com.omar.transactionHistoryserver.common.PageResponse;
import com.omar.transactionHistoryserver.dto.BorrowedBookResponse;
import com.omar.transactionHistoryserver.entities.BookTransactionHistory;
import com.omar.transactionHistoryserver.services.HistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor
@Tag(name = "Book Transaction History", description = "Operations related to Book Transaction History")
public class HistoryController {

    private  final HistoryService historyService;

    @PostMapping("borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id") Integer bookId

    ) {
        return ResponseEntity.ok(historyService.borrowBook(bookId));
    }

    @PatchMapping("borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowBook(
            @PathVariable("book-id") Integer bookId
    ) {
        return ResponseEntity.ok(historyService.returnBorrowedBook(bookId));
    }

    @PatchMapping("borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBorrowBook(
            @PathVariable("book-id") Integer bookId

    ) {
        return ResponseEntity.ok(historyService.approveReturnBorrowedBook(bookId));
    }



    @GetMapping("/{bookId}/users/{userId}/is-borrowed")
    public boolean isBookBorrowedByUser(@PathVariable Integer bookId, @PathVariable Integer userId) {
        return historyService.isAlreadyBorrowedByUser(bookId, userId);
    }

    @GetMapping("/{bookId}/is-borrowed")
    public boolean isBookBorrowed(@PathVariable Integer bookId) {
        return historyService.isAlreadyBorrowed(bookId);
    }


    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size

    ) {
        return ResponseEntity.ok(historyService.findAllBorrowedBooks(page, size));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size

    ) {
        return ResponseEntity.ok(historyService.findAllReturnedBooks(page, size));
    }

    @GetMapping("/{bookId}/owner")
    public ResponseEntity<BookTransactionHistory> findByBookIdAndOwnerId(
            @PathVariable Integer bookId) {
        Optional<BookTransactionHistory> transactionHistory = historyService.findByBookIdAndOwnerId(bookId);
        if (transactionHistory.isPresent()) {
            return new ResponseEntity<>(transactionHistory.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }








}
