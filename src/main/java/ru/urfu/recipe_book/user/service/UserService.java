package ru.urfu.recipe_book.user.service;

import ru.urfu.recipe_book.user.dto.CreateUserDto;
import ru.urfu.recipe_book.user.dto.ResponseUserDto;
import ru.urfu.recipe_book.user.dto.UpdateUserDto;

import java.util.List;

public interface UserService {

    ResponseUserDto getUserById(Long userId);

    ResponseUserDto createUser(CreateUserDto createDto);

    List<ResponseUserDto> getAllUsers();

    List<ResponseUserDto> searchUsers(String username);

    ResponseUserDto updateUser(Long userId, UpdateUserDto updateDto);

    void deleteUser(Long userId);
}
