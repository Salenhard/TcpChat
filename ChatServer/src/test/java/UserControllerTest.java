import org.example.controller.UserController;
import org.example.dto.UserDto;
import org.example.repository.UserRepository;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.service.UserService;
import org.example.service.impl.JWTService;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private final JWTService jwtService = new JWTService();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);
    private final UserController userController = new UserController(userService, jwtService);

    @BeforeEach
    void setUp() {
        userController.deleteAll();
        userController.add(new UserDto("test"));
    }

    @Test
    void add() {
        userController.add(new UserDto("test2"));
        userController.add(new UserDto("test3"));
        userController.add(new UserDto("test4"));
        userController.add(new UserDto("test5"));
        userController.add(new UserDto("test6"));
        userController.add(new UserDto("test7"));
        userController.add(new UserDto("test8"));
        userController.add(new UserDto("test9"));
        userController.add(new UserDto("test10"));
        userController.add(new UserDto("test11"));
        assertEquals(11, userController.getAll().size());
    }

    @Test
    void delete() {
        userController.delete("test");
        assertEquals(0, userController.getAll().size());
    }

}
