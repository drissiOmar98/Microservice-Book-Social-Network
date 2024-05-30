package com.example.bookserver.dto;

import com.example.bookserver.entities.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("ownerId"), ownerId);
    }
}
