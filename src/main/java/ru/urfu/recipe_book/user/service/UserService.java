package ru.urfu.recipe_book.user.service;

import ru.urfu.recipe_book.common.entities.CursorPageResponse;
import ru.urfu.recipe_book.user.dto.CreateUserDto;
import ru.urfu.recipe_book.user.dto.LoginUserDto;
import ru.urfu.recipe_book.user.dto.ResponseUserDto;
import ru.urfu.recipe_book.user.dto.UpdateUserDto;

import java.util.List;

public interface UserService {

    String authenticate(LoginUserDto loginDto);

    ResponseUserDto createUser(CreateUserDto createDto);

    CursorPageResponse<ResponseUserDto> searchUsers(Long cursor, int size, String username);

    ResponseUserDto updateUser(Long userId, UpdateUserDto updateDto);

    ResponseUserDto getUserById(Long id);

    void deleteUser(Long userId);
}
