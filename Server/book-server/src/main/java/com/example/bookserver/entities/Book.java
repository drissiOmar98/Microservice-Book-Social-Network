package com.example.bookserver.entities;



import com.example.bookserver.client.FeedbackClient;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private Integer id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;


    @Column(name = "owner_id") // This is a denormalized column to store the user ID
    private Integer ownerId;


    @Transient
    private FeedbackClient feedbackClient;

    public void setFeedbackClient(FeedbackClient feedbackClient) {
        this.feedbackClient = feedbackClient;
    }

    @Transient
    public double getRate() {
        if (feedbackClient == null) {
            throw new IllegalStateException("FeedbackClient has not been initialized");
        }

        List<Double> notes = feedbackClient.getNotesForBook(id);

        if (notes == null || notes.isEmpty()) {
            return 0.0;
        }

        double rate = notes.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        return Math.round(rate * 10.0) / 10.0;
    }






}
