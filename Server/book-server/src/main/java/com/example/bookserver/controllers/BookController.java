package com.example.bookserver.controllers;


import com.example.bookserver.common.PageResponse;
import com.example.bookserver.dto.BookRequest;
import com.example.bookserver.dto.BookResponse;
import com.example.bookserver.exception.ConnectedUserNotFoundException;
import com.example.bookserver.services.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Operations related to books")
public class BookController {

    private final BookService service;


    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable("book-id") Integer bookId
    ) {
        return ResponseEntity.ok(service.findById(bookId));
    }

    @PostMapping("/addBook")
    public ResponseEntity<Integer> saveBook(@RequestBody BookRequest request) {
        // Call the save method in the BookService
        try {
            Integer bookId = service.save(request);
            return ResponseEntity.ok(bookId);
        } catch (ConnectedUserNotFoundException ex) {
            // Handle the exception for missing connected user information
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size

    ) {
        return ResponseEntity.ok(service.findAllBooks(page, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(service.findAllBooksByOwner(page, size));
    }

    @GetMapping("/{bookId}/owner")
    public ResponseEntity<Integer> getOwnerByBookId(@PathVariable("bookId") Integer bookId) {
        Integer ownerId = service.getOwnerByBookId(bookId);
        if (ownerId != null) {
            return ResponseEntity.ok(ownerId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("book-id") Integer bookId

    ) {
        return ResponseEntity.ok(service.updateShareableStatus(bookId));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id") Integer bookId

    ) {
        return ResponseEntity.ok(service.updateArchivedStatus(bookId));
    }


    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Integer bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file

    ) {
        service.uploadBookCoverPicture(file,  bookId);
        return ResponseEntity.accepted().build();
    }






}
