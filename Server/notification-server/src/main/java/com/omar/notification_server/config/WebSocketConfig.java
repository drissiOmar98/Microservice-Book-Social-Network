package com.omar.notification_server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    /**
     * Configures the message broker. This sets up an in-memory broker for
     * managing WebSocket messages and defines prefixes for routing.
     *
     * @param registry the message broker registry to configure
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enables a simple, in-memory message broker for sending messages to clients.
        registry.enableSimpleBroker("/user"); // Prefix for client-specific messages

        // Prefix for client messages to be routed to server-side controllers
        registry.setApplicationDestinationPrefixes("/app");

        // Prefix for messages sent directly to specific users
        registry.setUserDestinationPrefix("/user");
    }


    /**
     * Registers the WebSocket endpoints that clients will use to connect to the server.
     * This includes configuration for SockJS as a fallback option.
     *
     * @param registry the STOMP endpoint registry to configure
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws") // Defines the main WebSocket endpoint for client connections
                .setAllowedOrigins("http://localhost:4200") // Allows requests from the Angular app's origin
                .withSockJS(); // Enables SockJS fallback for browsers that do not support WebSockets
    }

    /**
     * Adds custom argument resolvers for handler methods.
     * This configuration supports resolving authenticated user information.
     *
     * @param argumentResolvers the list of argument resolvers to add to
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // Adds resolver to handle @AuthenticationPrincipal annotation for accessing user details
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    /**
     * Configures message converters for serializing and deserializing WebSocket messages.
     * This setup enables JSON message handling with a custom ObjectMapper.
     *
     * @param messageConverters the list of message converters to configure
     * @return false to indicate that the default converters should not be added
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        // Sets up a JSON converter with a specific content type resolver
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);

        // Defines a converter that uses Jackson for JSON message conversion
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);

        messageConverters.add(converter); // Adds the converter to the list
        return false; // Indicates that no default converters should be added
    }
}
