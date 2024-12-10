import org.example.controller.MessageController;
import org.example.dto.MessageDto;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.repository.ChatRepository;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import org.example.repository.impl.ChatRepositoryImpl;
import org.example.repository.impl.MessageRepositoryImpl;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.service.ChatService;
import org.example.service.MessageService;
import org.example.service.UserService;
import org.example.service.impl.ChatServiceImpl;
import org.example.service.impl.MessageServiceImpl;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageControllerTest {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ChatRepository chatRepository = new ChatRepositoryImpl();
    private final MessageRepository messageRepository = new MessageRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);
    private final ChatService chatService = new ChatServiceImpl(chatRepository, userService);
    private final MessageService messageService = new MessageServiceImpl(messageRepository, chatService, userService);
    private final MessageController messageController = new MessageController(messageService);

    @BeforeEach
    void setUp() {
        chatRepository.deleteAll();
        userRepository.deleteAll();
        Chat chat = new Chat();
        chat.setName("test");
        User user = new User("test");
        userService.save(user);
        chatService.save(chat, "test");
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(1L);
        messageDto.setText("test");
        messageController.addMessage(messageDto, messageDto.getChatId(), "test");
    }


    @Test
    void addMessageSuccessTest() {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(1L);
        messageDto.setText("test");
        messageController.addMessage(messageDto, messageDto.getChatId(), "test");
        assertEquals(2, messageController.getMessages(null, null, null, null, 1L, "test").size());
    }

    @Test
    void addMessageFailTest() {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(1L);
        messageDto.setText("test");
        User user = new User("test1");
        userService.save(user);
        Assertions.assertThrows(RuntimeException.class, () -> messageController.addMessage(messageDto, messageDto.getChatId(), "test1"));
    }

    @Test
    void editMessageSuccessTest() {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(1L);
        messageDto.setText("test");
        messageController.updateMessage(1L, messageDto, 1L, "test");
        assertEquals("test", messageController.getMessageById(1L, 1L).getText());
    }

    @Test
    void editMessageFailTest() {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(1L);
        messageDto.setText("test");
        User user = new User("test1");
        userService.save(user);
        Assertions.assertThrows(RuntimeException.class, () -> messageController.updateMessage(1L, messageDto, 1L, "test1"));
    }

    @Test
    void deleteMessageSuccessTest() {
        messageController.deleteMessage(1L, 1L, "test");
        assertEquals(0, messageController.getMessages(null, null, null, null, 1L, "test").size());
    }

    @Test
    void deleteMessageFailTest() {
        User user = new User("test1");
        userService.save(user);
        Assertions.assertThrows(RuntimeException.class, () -> messageController.deleteMessage(1L, 1L, "test1"));
    }
}
