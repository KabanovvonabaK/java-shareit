package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS", schema = "PUBLIC")
public class ItemRequest {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID", referencedColumnName = "ID")
    private User requester;
    @Column(name = "CREATED")
    private LocalDateTime created;
}