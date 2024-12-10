package org.example.dto.mapper;

import org.example.dto.MessageDto;
import org.example.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper extends DefaultMapper<MessageDto, Message> {
    MessageMapper INSTANCE = Mappers.getMapper( MessageMapper.class );
}
