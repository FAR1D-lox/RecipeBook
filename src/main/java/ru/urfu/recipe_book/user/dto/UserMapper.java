package ru.urfu.recipe_book.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.urfu.recipe_book.user.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(CreateUserDto createDto);

    ResponseUserDto toResponseUser(User user);

    List<ResponseUserDto> toResponseUserList(List<User> usersList);
}
