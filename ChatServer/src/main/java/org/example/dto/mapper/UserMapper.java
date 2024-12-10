package org.example.dto.mapper;

import org.example.dto.UserDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends DefaultMapper<UserDto, User> {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
}
