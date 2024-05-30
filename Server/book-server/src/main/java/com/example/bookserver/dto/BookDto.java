package com.example.bookserver.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;
    private Integer ownerId;

}
