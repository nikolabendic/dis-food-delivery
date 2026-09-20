package rs.ftn.dis.userservice.controller;

import rs.ftn.dis.userservice.model.User;
import rs.ftn.dis.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_shouldReturnSavedUser() {
        User inputUser = new User("Marko", "marko@test.com");
        User savedUser = new User("Marko", "marko@test.com");
        savedUser.setId(1L);

        when(userRepository.save(inputUser)).thenReturn(savedUser);

        ResponseEntity<User> response = userController.createUser(inputUser);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Marko", response.getBody().getName());
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUser() {
        User user = new User("Ana", "ana@test.com");
        user.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Ana", response.getBody().getName());
    }

    @Test
    void getUserById_whenUserDoesNotExist_shouldReturn404() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(99L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user1 = new User("Marko", "marko@test.com");
        User user2 = new User("Ana", "ana@test.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }
}