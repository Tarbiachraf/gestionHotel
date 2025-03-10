package com.system.hotel.booking.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hotel_room_types")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HotelRoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private String image;

    private Double cost;
    
    private int numOfGuest;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @JsonIgnore
    @OneToMany(mappedBy = "hotelRoomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> room;
    
    private int availableRooms;
    
    private int totalRooms;
    
    private int bookedCount;



}
