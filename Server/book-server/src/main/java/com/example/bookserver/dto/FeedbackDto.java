package com.example.bookserver.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDto {

    private Integer id;
    private Double note;
    private String comment;
}
