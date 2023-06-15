package ru.practicum.shareit.item.serice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.errorHandler.exception.UserIsNotAnOwnerException;
import ru.practicum.shareit.errorHandler.exception.UserNotBookedException;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentRequestDto;
import ru.practicum.shareit.item.comments.mapper.CommentMapper;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto create(ItemDto itemDto, int userId) {
        log.info("Attempt to create item {}", itemDto);
        User user = UserMapper.toUser(userService.getUserById(userId));
        Item item = new Item();
        item.setOwner(user);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository
                    .findById(itemDto.getRequestId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Request with id %s not found",
                            itemDto.getRequestId())));
            item.setRequest(itemRequest);
        }
        return Optional.of(itemRepository.save(ItemMapper.toItem(itemDto, item))).map(ItemMapper::toItemDto).orElseThrow();
    }

    @Override
    public ItemDto updateItem(int itemId, int userId, ItemDto itemDto) {
        log.info("Attempt to update item {}", itemDto);
        Item item = ItemMapper.toItem(getByItemId(userId, itemId));

        if (item.getOwner().getId() != userId) {
            throw new UserIsNotAnOwnerException(
                    String.format("User with id %s is not an owner of item with id %s", userId, itemId));
        }

        return Optional.of(itemRepository.save(ItemMapper.toItem(itemDto, item)))
                .map(ItemMapper::toItemDto)
                .orElseThrow();
    }

    @Override
    public ItemDto getByItemId(int userId, int itemId) {
        log.info("Attempt to get item by itemId {}", itemId);
        return itemRepository
                .findById(itemId)
                .map(item -> addData(userId, item))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item with id %s not exist", itemId)));
    }

    @Override
    public List<ItemDto> getByOwnerId(int userId) {
        log.info("Attempt to get item by userId {}", userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(item -> addData(userId, item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getByOwnerId(int userId, int from, int size) {
        log.info("Attempt to get item by userId {} with pagination", userId);
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        userService.getUserById(userId);
        return itemRepository.findAllByOwnerId(userId, pageRequest)
                .stream()
                .map(item -> addData(userId, item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        log.info("Attempt to search by \"{}\"", text);
        return itemRepository
                .search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(int userId, String text, int from, int size) {
        log.info("Attempt to search by \"{}\" with pagination", text);
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        userService.getUserById(userId);
        return itemRepository
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text,
                        text, pageRequest)
                .map(ItemMapper::toItemDto)
                .getContent();
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentRequestDto commentRequestDto) {
        log.info("Adding comment to item with id {}", itemId);
        User user = UserMapper.toUser(userService.getUserById(userId));
        Item item = ItemMapper.toItem(getByItemId(userId, itemId));

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(user.getId(), item.getId(), LocalDateTime.now())) {
            throw new UserNotBookedException("The user not booked this item");
        }

        return Optional.of(commentRepository.save(CommentMapper.toComment(commentRequestDto, item, user)))
                .map(CommentMapper::toCommentDto)
                .orElseThrow();
    }

    @Override
    public List<ItemDto> findAllByRequestIdIn(List<Integer> listRequestIds) {
        return itemRepository.findAllByRequestIdIn(listRequestIds)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAllByRequestId(int requestId) {
        return itemRepository.findAllByRequestId(requestId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private ItemDto addData(int userId, Item item) {
        ItemDto itemDto = ItemMapper.toItemDto(item);

        if (itemDto.getOwner().getId() == userId) {
            itemDto.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(itemDto.getId(),
                            LocalDateTime.now(),
                            BookingStatus.APPROVED)
                    .map(BookingMapper::toShortBookingDto)
                    .orElse(null));

            itemDto.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(itemDto.getId(),
                            LocalDateTime.now(),
                            BookingStatus.APPROVED)
                    .map(BookingMapper::toShortBookingDto)
                    .orElse(null));
        }

        itemDto.setComments(commentRepository.findAllByItemId(itemDto.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

        return itemDto;
    }
}