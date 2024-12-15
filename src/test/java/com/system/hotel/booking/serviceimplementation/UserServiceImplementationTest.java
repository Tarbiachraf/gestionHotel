package com.system.hotel.booking.serviceimplementation;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.system.hotel.booking.entity.User;
import com.system.hotel.booking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.List;

class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialisation des objets nécessaires
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Test User");
    }

    @Test
    void testCreateUser_ShouldSaveUser_WhenUserIsValid() {
        // Arrangements
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.createUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    void testAuthenticateUser_ShouldReturnUser_WhenCredentialsAreValid() {
        // Arrangements
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getEmail()
                , user.getPassword());

        // Assert
        assertNotNull(authenticatedUser);
        assertEquals(user.getEmail(), authenticatedUser.getEmail());
    }

    @Test
    void testAuthenticateUser_ShouldReturnNull_WhenEmailNotFound() {
        // Arrangements
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getEmail()
                , user.getPassword());

        // Assert
        assertNull(authenticatedUser);
    }

    @Test
    void testAuthenticateUser_ShouldReturnNull_WhenPasswordIsIncorrect() {
        // Arrangements
        User wrongPasswordUser = new User();
        wrongPasswordUser.setEmail(user.getEmail());
        wrongPasswordUser.setPassword("wrongPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getEmail(),
                "wrongPassword");

        // Assert
        assertNull(authenticatedUser);
    }

    @Test
    void testGetUserById_ShouldReturnUser_WhenUserExists() {
        // Arrangements
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.getUserById(user.getId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void testGetUserById_ShouldThrowException_WhenUserNotFound() {
        // Arrangements
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(user.getId());
        });
    }

    @Test
    void testUpdateUser_ShouldUpdateUser_WhenUserExists() {
        // Arrangements
        User updatedUser = new User();
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newPassword123");
        updatedUser.setName("Updated User");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        // Act
        User savedUser = userService.updateUser(user.getId(), updatedUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals(updatedUser.getEmail(), savedUser.getEmail());
    }

    @Test
    void testUpdateUser_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        Long nonExistentUserId = 1L;
        User updatedUser = new User();
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newPassword123");
        updatedUser.setName("Updated User");

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUser(nonExistentUserId, updatedUser)
        );

        // Assert exception message
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testDeleteUser_ShouldDeleteUser_WhenUserExists() {
        // Arrangements
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(user.getId());

        // Assert
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    /*@Test
    void testDeleteUser_ShouldThrowException_WhenUserNotFound() {
        // Arrangements
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(user.getId());
        });
    }*/

    @Test
    void testGetAllUsers_ShouldReturnPagedUsers() {
        // Arrangements
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(user);
        when(userRepository.findAllUsersByPage(pageable)).thenReturn(users);

        // Act
        List<User> usersList = userService.getAllUsers(1);

        // Assert
        assertNotNull(usersList);
        assertEquals(1, usersList.size());
        assertEquals(user.getEmail(), usersList.get(0).getEmail());
    }
}
