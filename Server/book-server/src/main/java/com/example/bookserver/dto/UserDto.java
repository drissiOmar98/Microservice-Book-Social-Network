package com.example.bookserver.dto;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;
    private String firstname;
    private String lastname;

}
