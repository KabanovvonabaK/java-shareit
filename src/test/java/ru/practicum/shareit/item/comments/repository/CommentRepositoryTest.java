package ru.practicum.shareit.item.comments.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.comments.dto.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    private Item item;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setName("User Name");
        user.setEmail("user@email.com");
        userRepository.save(user);
        item = new Item();
        item.setName("Item Name");
        item.setDescription("Description");
        item.setAvailable(Boolean.TRUE);
        item.setOwner(user);
        itemRepository.save(item);
        Comment comment = new Comment();
        comment.setText("Text");
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        commentRepository.save(comment);
    }

    @Test
    void findAllByItemId() {
        List<Comment> allByItemId = commentRepository.findAllByItemId(item.getId());

        assertAll(
                () -> assertNotNull(allByItemId),
                () -> assertEquals(1, allByItemId.size())
        );
    }
}