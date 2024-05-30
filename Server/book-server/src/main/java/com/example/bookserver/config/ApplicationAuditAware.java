package com.example.bookserver.config;

import com.example.bookserver.client.UserClient;
import com.example.bookserver.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


@Slf4j
public class ApplicationAuditAware  implements AuditorAware<Integer> {


    private final UserClient userClient;

    @Autowired
    public ApplicationAuditAware(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public Optional<Integer> getCurrentAuditor() {
        try {
            ResponseEntity<UserDto> response = userClient.getCurrentUser();
            UserDto userDto = response.getBody();
            if (userDto != null) {
                log.info("Current auditor ID: " + userDto.getId());
            }
            return Optional.ofNullable(userDto != null ? userDto.getId() : null);
        } catch (Exception e) {
            // Handle the exception, e.g., log it
            e.printStackTrace();
            return Optional.empty();
        }
    }
}


