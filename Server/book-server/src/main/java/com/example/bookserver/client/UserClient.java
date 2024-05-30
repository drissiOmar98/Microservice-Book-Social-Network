package com.example.bookserver.client;


import com.example.bookserver.config.FeignClientConfig;
import com.example.bookserver.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${application.config.auth-url}", configuration = FeignClientConfig.class)
public  interface UserClient {

    @GetMapping("/users/{userId}")
    UserDto getUser(@PathVariable("userId") Integer userId);

    @GetMapping("/me")
    ResponseEntity<UserDto> getCurrentUser();

    @GetMapping("/{ownerId}")
    UserDto getOwner(@PathVariable("ownerId") Integer ownerId);

}
