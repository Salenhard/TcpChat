import org.example.controller.ChatController;
import org.example.dto.ChatDto;
import org.example.dto.UserDto;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.repository.ChatRepository;
import org.example.repository.UserRepository;
import org.example.repository.impl.ChatRepositoryImpl;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.service.ChatService;
import org.example.service.UserService;
import org.example.service.impl.ChatServiceImpl;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ChatControllerTest {

    private final ChatRepository chatRepository = new ChatRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);
    private final ChatService chatService = new ChatServiceImpl(chatRepository, userService);
    private final ChatController chatController = new ChatController(chatService);

    @BeforeEach
    void setUp() {
        chatRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User("test");
        userService.save(user);
        ChatDto chat = new ChatDto();
        chat.setIsPrivate(false);
        chat.setName("test");
        chatController.save(chat, user.getUsername());
    }

    @Test
    void getAllChatsTest() {
        List<ChatDto> chats = chatController.getAll("", List.of(new UserDto("test")));
        assertEquals(1, chats.size());
    }

    @Test
    void createChatTest() {
        ChatDto chat = new ChatDto();
        chat.setIsPrivate(false);
        chat.setName("test1");
        chatController.save(chat, "test");
        assertEquals(2, chatController.getAll("", null).size());
    }

    @Test
    void deleteChatSuccessTest() {
        chatController.delete(chatController.getAll("", null).get(0).getId(), "test");
        assertEquals(0, chatController.getAll("", null).size());
    }

    @Test
    void deleteChatFailTest() {
        User user = new User("test1");
        userService.save(user);
        Assertions.assertThrows(RuntimeException.class, () -> chatController.delete(chatController.getAll("", null).get(0).getId(), "test1"));
    }

    @Test
    void updateChatSuccessTest() {
        ChatDto chat = chatController.getAll("", null).get(0);
        chat.setName("test1");
        chatController.update(chat.getId(), chat, "test");
        assertEquals("test1", chatController.getAll("", null).get(0).getName());
    }

    @Test
    void updateChatFailTest() {
        User user = new User("test1");
        userService.save(user);
        ChatDto chat = chatController.getAll("", null).get(0);
        chat.setName("test1");
        Assertions.assertThrows(RuntimeException.class, () -> chatController.update(chat.getId(), chat, "test1"));
    }

    @Test
    void addUserSuccessTest() {
        userService.save(new User("test1"));
        chatController.addUser(chatController.getAll("", null).get(0).getId(), "test1", "test");
        assertEquals(2, chatController.getAll("", null).get(0).getUsers().size());
    }

    @Test
    void addUserFailTest() {
        userService.save(new User("test1"));
        ChatDto chat = chatController.getAll("", null).get(0);
        chat.setIsPrivate(true);
        chatController.update(chat.getId(), chat, "test");
        Assertions.assertThrows(RuntimeException.class,
                () -> chatController.addUser(chatController.getAll("", null).get(0).getId(), "test1", "test1"));

    }

    @Test
    void removeCreatorFailTest() throws RuntimeException {
        userService.save(new User("test1"));
        Assertions.assertThrows(RuntimeException.class,
                () -> chatController.removeUser(chatController.getAll("", null).get(0).getId(), "test", "test"));
    }

    @Test
    void removeUserFailTest() throws RuntimeException {
        userService.save(new User("test1"));
        userService.save(new User("test2"));
        Assertions.assertThrows(RuntimeException.class,
                () -> chatController.removeUser(chatController.getAll("", null).get(0).getId(), "test1", "test2"));
    }

    @Test
    void removeUserSuccessTest() throws RuntimeException {
        userService.save(new User("test1"));
        chatController.removeUser(chatController.getAll("", null).get(0).getId(), "test1", "test1");
        assertEquals(1, chatController.getAll("", null).get(0).getUsers().size());
    }
}
