package org.example.dto.mapper;

import org.example.dto.ChatDto;
import org.example.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface ChatMapper extends DefaultMapper<ChatDto, Chat> {
    ChatMapper INSTANCE = Mappers.getMapper( ChatMapper.class );
}
