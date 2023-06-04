package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager manager;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("Name");
        user.setEmail("email@email.com");
        userRepository.save(user);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Description");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void findAllByRequester_Id() {
        Sort sort = Sort.by("created").descending();
        List<ItemRequest> allByRequester_id = itemRequestRepository.findAllByRequester_Id(user.getId(), sort);

        assertAll(
                () -> assertNotNull(allByRequester_id),
                () -> assertThat(allByRequester_id.size(), equalTo(1))
        );
    }

    @Test
    void findAllByRequester_IdNot() {
        Page<ItemRequest> allByRequester_idNot = itemRequestRepository
                .findAllByRequester_IdNot(user.getId(), PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(allByRequester_idNot),
                () -> assertTrue(allByRequester_idNot.isEmpty())
        );
    }
}