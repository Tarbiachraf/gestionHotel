package com.system.hotel.booking.serviceimplementation;

import com.system.hotel.booking.entity.HotelRoomType;
import com.system.hotel.booking.repository.HotelRoomTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelRoomTypeServiceImplTest {

    @Mock
    private HotelRoomTypeRepository hotelRoomTypeRepository;

    @InjectMocks
    private HotelRoomTypeServiceImpl hotelRoomTypeService;

    @Test
     void testCreateHotelRoomType() {
        // Arrange
        HotelRoomType roomType = new HotelRoomType();
        roomType.setId(1L);
        roomType.setName("Deluxe");

        when(hotelRoomTypeRepository.save(any(HotelRoomType.class))).thenReturn(roomType);

        // Act
        HotelRoomType result = hotelRoomTypeService.createHotelRoomType(roomType);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Deluxe", result.getName());
        verify(hotelRoomTypeRepository, times(1)).save(roomType);
    }

    @Test
     void testGetHotelRoomTypesByHotelId() {
        // Arrange
        Long hotelId = 1L;
        List<HotelRoomType> roomTypes = Arrays.asList(new HotelRoomType(), new HotelRoomType());

        when(hotelRoomTypeRepository.findByHotelId(hotelId)).thenReturn(roomTypes);

        // Act
        List<HotelRoomType> result = hotelRoomTypeService.getHotelRoomTypesByHotelId(hotelId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(hotelRoomTypeRepository, times(1)).findByHotelId(hotelId);
    }

    @Test
     void testGetHotelRoomTypeById_Found() {
        // Arrange
        Long roomTypeId = 1L;
        HotelRoomType roomType = new HotelRoomType();
        roomType.setId(roomTypeId);

        when(hotelRoomTypeRepository.findById(roomTypeId)).thenReturn(Optional.of(roomType));

        // Act
        HotelRoomType result = hotelRoomTypeService.getHotelRoomTypeById(roomTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(roomTypeId, result.getId());
        verify(hotelRoomTypeRepository, times(1)).findById(roomTypeId);
    }

    @Test
     void testGetHotelRoomTypeById_NotFound() {
        // Arrange
        Long roomTypeId = 1L;

        when(hotelRoomTypeRepository.findById(roomTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            hotelRoomTypeService.getHotelRoomTypeById(roomTypeId);
        });

        assertEquals("Room Type not found", exception.getMessage());
        verify(hotelRoomTypeRepository, times(1)).findById(roomTypeId);
    }

    @Test
    void testGetRoomTypesByPage() {
        // Arrange
        Long hotelId = 1L;
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        List<HotelRoomType> roomTypes = Arrays.asList(new HotelRoomType(), new HotelRoomType());

        when(hotelRoomTypeRepository.findHotelRoomTypesByHotelId(pageable, hotelId)).thenReturn(roomTypes);

        // Act
        List<HotelRoomType> result = hotelRoomTypeService.getRoomTypesByPage(pageNumber, hotelId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(hotelRoomTypeRepository, times(1)).findHotelRoomTypesByHotelId(pageable, hotelId);
    }

    @Test
     void testDeleteRoomTypeById_Found() {
        // Arrange
        Long roomTypeId = 1L;
        HotelRoomType roomType = new HotelRoomType();
        roomType.setId(roomTypeId);

        when(hotelRoomTypeRepository.findById(roomTypeId)).thenReturn(Optional.of(roomType));

        // Act
        hotelRoomTypeService.deleteRoomTypeById(roomTypeId);

        // Assert
        verify(hotelRoomTypeRepository, times(1)).findById(roomTypeId);
        verify(hotelRoomTypeRepository, times(1)).deleteById(roomTypeId);
    }

    @Test
    void testDeleteRoomTypeById_NotFound() {
        // Arrange
        Long roomTypeId = 1L;

        when(hotelRoomTypeRepository.findById(roomTypeId)).thenReturn(Optional.empty());

        // Act
        hotelRoomTypeService.deleteRoomTypeById(roomTypeId);

        // Assert
        verify(hotelRoomTypeRepository, times(1)).findById(roomTypeId);
        verify(hotelRoomTypeRepository, never()).deleteById(roomTypeId);
    }
}
