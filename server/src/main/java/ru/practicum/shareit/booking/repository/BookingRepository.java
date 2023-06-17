package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdOrderByStartDesc(int ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdOrderByStartDesc(int ownerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStateCurrentOrderByStartDesc(int ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStateCurrentOrderByStartDesc(int ownerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatePastOrderByStartDesc(int ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatePastOrderByStartDesc(int ownerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStateFutureOrderByStartDesc(int ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStateFutureOrderByStartDesc(int ownerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus bookingStatus);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus bookingStatus,
                                                            PageRequest pageRequest);

    List<Booking> findAllByBookerIdOrderByStartDesc(int bookerId);

    List<Booking> findAllByBookerIdOrderByStartDesc(int bookerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStateCurrentOrderByStartDesc(int bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStateCurrentOrderByStartDesc(int bookerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStatePastOrderByStartDesc(int brokerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStatePastOrderByStartDesc(int brokerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStateFutureOrderByStartDesc(int bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStateFutureOrderByStartDesc(int bookerId, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int bookerId, BookingStatus bookingStatus);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int bookerId, BookingStatus bookingStatus,
                                                             PageRequest pageRequest);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(int itemId,
                                                                             LocalDateTime localDateTime,
                                                                             BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(int itemId,
                                                                           LocalDateTime localDateTime,
                                                                           BookingStatus bookingStatus);

    Boolean existsByBookerIdAndItemIdAndEndBefore(int bookerId, int itemId, LocalDateTime localDateTime);
}