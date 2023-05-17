package ru.practicum.shareit.item.comments;

import ru.practicum.shareit.item.comments.dto.Comment;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentInputDto;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment toComment(CommentInputDto commentInputDto, Comment comment) {
        comment.setText(commentInputDto.getText());
        comment.setCreated(LocalDateTime.now());

        return comment;
    }
}
