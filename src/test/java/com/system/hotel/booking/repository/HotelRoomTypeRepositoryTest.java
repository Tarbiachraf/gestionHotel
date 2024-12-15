package com.system.hotel.booking.repository;

import com.system.hotel.booking.entity.Hotel;
import com.system.hotel.booking.entity.HotelRoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class HotelRoomTypeRepositoryTest {
    @Autowired
    private HotelRoomTypeRepository hotelRoomTypeRepository;
    @Autowired
    private HotelRepository hotelRepository;

    private Hotel hotel;
    @BeforeEach
    void setUp() {
        // Créer et enregistrer un hôtel
        hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel = hotelRepository.save(hotel);

        // Créer et enregistrer des types de chambres pour cet hôtel
        for (int i = 1; i <= 5; i++) {
            HotelRoomType roomType = new HotelRoomType();
            roomType.setName("Room Type " + i);
            roomType.setHotel(hotel);
            hotelRoomTypeRepository.save(roomType);
        }
    }

 /*   @Test
    void testFindHotelRoomTypesByHotelId() {
        // Créer un Pageable pour la pagination
        Pageable pageable = PageRequest.of(0, 3); // Page 0, 3 résultats par page

        // Appeler la méthode testée
        List<HotelRoomType> roomTypes = hotelRoomTypeRepository.findHotelRoomTypesByHotelId(pageable, hotel.getId());

        // Vérifier les résultats
        assertThat(roomTypes).hasSize(3); // Doit renvoyer 3 résultats (en fonction du Pageable)
        assertThat(roomTypes.get(0).getName()).isEqualTo("Room Type 5"); // Vérifie l'ordre DESC
        assertThat(roomTypes.get(1).getName()).isEqualTo("Room Type 4");
        assertThat(roomTypes.get(2).getName()).isEqualTo("Room Type 3");
    }*/
}