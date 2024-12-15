package com.system.hotel.booking.serviceimplementation;

import com.system.hotel.booking.entity.HotelRoomType;
import com.system.hotel.booking.entity.Room;
import com.system.hotel.booking.repository.HotelRoomTypeRepository;
import com.system.hotel.booking.repository.RoomRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;

import javax.persistence.EntityNotFoundException;

class RoomServiceImplementationTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRoomTypeRepository hotelRoomTypeRepository;

    @InjectMocks
    private RoomServiceImplementation roomService;

    private Room room;
    private HotelRoomType hotelRoomType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialisation des objets nécessaires
        hotelRoomType = new HotelRoomType();
        hotelRoomType.setId(1L);
        hotelRoomType.setName("Deluxe Room");
        hotelRoomType.setTotalRooms(10);
        hotelRoomType.setAvailableRooms(5);

        room = new Room();
        room.setId(1L);
        room.setName("Room 101");
        room.setLocation("1st floor");
        room.setAvailability(true);
        room.setHotelRoomType(hotelRoomType);
    }

    @Test
    void testSaveRoom_ShouldSaveRoom_WhenRoomIsValid() {
        // Arrangements
        Long hotelRoomTypeId = 1L;
        when(hotelRoomTypeRepository.findById(hotelRoomTypeId)).thenReturn(Optional.of(hotelRoomType));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.empty());
        when(roomRepository.save(room)).thenReturn(room);

        // Act
        Room savedRoom = roomService.saveRoom(room, hotelRoomTypeId);

        // Assert
        assertNotNull(savedRoom);
        assertEquals(room.getName(), savedRoom.getName());
        assertEquals(hotelRoomType.getId(), savedRoom.getHotelRoomType().getId());
        assertEquals(11, hotelRoomType.getTotalRooms());  // Le total de chambres doit être mis à jour
    }

    @Test
    void testSaveRoom_ShouldThrowException_WhenRoomAlreadyExists() {
        // Arrangements
        Long hotelRoomTypeId = 1L;
        when(hotelRoomTypeRepository.findById(hotelRoomTypeId)).thenReturn(Optional.of(hotelRoomType));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));  // Room déjà existante

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.saveRoom(room, hotelRoomTypeId);
        });
        assertEquals("Room with name 'Room 101' already exists", exception.getMessage());
    }

    @Test
    void testGetRoomsByHotelRoomTypeId_ShouldReturnRoomMap_WhenRoomsExist() {
        // Arrangements
        when(roomRepository.findByHotelRoomTypeId(hotelRoomType.getId())).thenReturn(List.of(room));

        // Act
        Map<Long, String> result = roomService.getRoomsByHotelRoomTypeId(hotelRoomType.getId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(room.getId()));
        assertEquals(room.getName(), result.get(room.getId()));
    }

    @Test
    void testDeleteRoomById_ShouldDeleteRoom_WhenRoomExists() {
        // Arrangements
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(hotelRoomTypeRepository.findById(hotelRoomType.getId())).thenReturn(Optional.of(hotelRoomType));

        // Act
        roomService.deleteRoomById(room.getId());

        // Assert
        verify(roomRepository, times(1)).delete(room);
        assertEquals(9, hotelRoomType.getTotalRooms());  // Le total de chambres doit être mis à jour
    }

    @Test
    void testDeleteRoomById_ShouldThrowException_WhenRoomNotFound() {
        // Arrangements
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            roomService.deleteRoomById(room.getId());
        });
    }
}