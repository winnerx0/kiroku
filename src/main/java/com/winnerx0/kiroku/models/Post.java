package com.winnerx0.kiroku.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private String image;

    private LocalDate createdAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties("name")
    private User user;
}
