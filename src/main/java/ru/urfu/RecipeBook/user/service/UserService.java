package ru.urfu.RecipeBook.user.service;

import ru.urfu.RecipeBook.user.dto.CreateUserDto;
import ru.urfu.RecipeBook.user.dto.ResponseUserDto;
import ru.urfu.RecipeBook.user.dto.UpdateUserDto;
import ru.urfu.RecipeBook.user.entity.User;

import java.util.List;

public interface UserService {

    ResponseUserDto getUserById(Long userId);

    ResponseUserDto createUser(CreateUserDto createDto);

    List<ResponseUserDto> getAllUsers();

    List<ResponseUserDto> searchUsers(String username);

    ResponseUserDto updateUser(Long userId, UpdateUserDto updateDto);

    void deleteUser(Long userId);
}
