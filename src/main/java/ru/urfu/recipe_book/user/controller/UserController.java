package ru.urfu.recipe_book.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.common.entities.CursorPageResponse;
import ru.urfu.recipe_book.security.CustomUserDetails;
import ru.urfu.recipe_book.user.dto.CreateUserDto;
import ru.urfu.recipe_book.user.dto.LoginUserDto;
import ru.urfu.recipe_book.user.dto.ResponseUserDto;
import ru.urfu.recipe_book.user.dto.UpdateUserDto;
import ru.urfu.recipe_book.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseUserDto register(@Valid @RequestBody CreateUserDto createDto) {
        return userService.createUser(createDto);
    }

    @PostMapping("/login")
    String login (@RequestBody LoginUserDto loginDto) {
        return userService.authenticate(loginDto);
    }

    @GetMapping("/search")
    CursorPageResponse<ResponseUserDto> searchUsers(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String username) {
        return userService.searchUsers(cursor, size, username);
    }



    @PutMapping()
    ResponseUserDto updateUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody UpdateUserDto updateDto) {
        return userService.updateUser(customUserDetails.getUserId(), updateDto);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.deleteUser(customUserDetails.getUserId());
    }
}
