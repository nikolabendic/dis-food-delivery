package rs.ftn.dis.userservice.controller;

import rs.ftn.dis.userservice.model.User;
import rs.ftn.dis.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_shouldPersistUserInDatabase() {
        User newUser = new User("Petar", "petar@test.com");

        ResponseEntity<User> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/users", newUser, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void getAllUsers_shouldReturnUsersFromDatabase() {
        userRepository.save(new User("Jovana", "jovana@test.com"));
        userRepository.save(new User("Stefan", "stefan@test.com"));

        ResponseEntity<User[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/users", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void getUserById_whenNotExists_shouldReturn404() {
        ResponseEntity<User> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/users/999", User.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}