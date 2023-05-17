package ru.practicum.shareit.item.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comments.dto.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByItemId(int itemId);
}
