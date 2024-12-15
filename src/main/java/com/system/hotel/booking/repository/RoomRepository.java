package com.system.hotel.booking.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.system.hotel.booking.entity.Room;

import javax.persistence.LockModeType;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	
	Optional<Room> findByName(String name);
	
	List<Room> findByHotelRoomTypeId(Long roomId);
	
	List<Room> findByHotelRoomTypeIdAndAvailabilityTrue(Long roomTypeId);
	
	@Query("SELECT r FROM Room r WHERE r.hotelRoomType.id IN :roomTypeIds ORDER BY r.id DESC")
	List<Room> findRoomsByRoomTypeId(Pageable pageable, List<Long> roomTypeIds);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT r FROM Room r WHERE r.availability = TRUE AND r.hotelRoomType.id = :hotelRoomTypeId " +
			"AND r.id NOT IN (SELECT r2.id FROM Room r2 JOIN r2.bookings b " +
			"WHERE b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate)")
	List<Room> findAvailableRoomsForBooking(@Param("hotelRoomTypeId") Long hotelRoomTypeId,
											@Param("checkInDate") Date checkInDate,
											@Param("checkOutDate") Date checkOutDate);

}
