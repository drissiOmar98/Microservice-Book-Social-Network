package com.omar.transactionHistoryserver.dto;


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
