package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.dto.mapper.MessageMapper;
import org.example.service.MessageService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MessageController {
    private final MessageService service;
    private final MessageMapper mapper = MessageMapper.INSTANCE;

    public MessageDto addMessage(@Valid MessageDto dto, Long chatId, String username) {
        return mapper.toDto(service.save(mapper.toEntity(dto), chatId, username));
    }

    public MessageDto updateMessage(Long id, @Valid MessageDto dto, Long chatId, String username) {
        return mapper.toDto(service.update(id, mapper.toEntity(dto), chatId, username));
    }

    public void deleteMessage(Long id, Long chatId, String username) {
        service.deleteByIdAndChatId(id, chatId, username);
    }

    public MessageDto getMessageById(Long id, Long chatId) {
        return mapper.toDto(service.findByIdAndChatId(id, chatId).orElseThrow(() -> new RuntimeException("Message not found")));
    }

    public List<MessageDto> getMessages(String username, String text, LocalDateTime from, LocalDateTime till, Long chatId, String usernameCaller) {
        return service.findAll(username, text, from, till, chatId, usernameCaller).stream().map(mapper::toDto).toList();
    }

}
