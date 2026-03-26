package ru.urfu.RecipeBook.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.urfu.RecipeBook.user.dto.CreateUserDto;
import ru.urfu.RecipeBook.user.dto.ResponseUserDto;
import ru.urfu.RecipeBook.user.dto.UpdateUserDto;
import ru.urfu.RecipeBook.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @GetMapping("/{userId}")
    ResponseUserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseUserDto register(@Valid @RequestBody CreateUserDto createDto) {
        return userService.createUser(createDto);
    }

    @GetMapping
    List<ResponseUserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/search")
    List<ResponseUserDto> searchUsers(@RequestParam String username) {
        return userService.searchUsers(username);
    }

    @PutMapping("/{userId}")
    ResponseUserDto updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserDto updateDto) {
        return userService.updateUser(userId, updateDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
