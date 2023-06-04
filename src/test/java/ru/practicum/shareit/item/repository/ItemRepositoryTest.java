package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
class ItemRepositoryTest {

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


}