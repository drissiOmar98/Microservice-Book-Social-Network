package com.omar.userserver.dto;


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
    private String email;

}
