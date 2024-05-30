package com.example.bookserver.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTransactionHistoryDto {

    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private boolean returned;
    private boolean returnApproved;
    private Integer bookId;
    private Integer userId;


}
