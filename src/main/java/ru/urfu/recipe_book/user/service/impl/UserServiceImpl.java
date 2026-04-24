package ru.urfu.recipe_book.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import ru.urfu.recipe_book.user.dto.CreateUserDto;
import ru.urfu.recipe_book.user.dto.ResponseUserDto;
import ru.urfu.recipe_book.user.dto.UpdateUserDto;
import ru.urfu.recipe_book.user.dto.UserMapper;
import ru.urfu.recipe_book.user.entity.User;
import ru.urfu.recipe_book.user.repository.UserRepository;
import ru.urfu.recipe_book.user.service.UserService;
import ru.urfu.recipe_book.common.entities.CursorPageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ResponseUserDto createUser(CreateUserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new RuntimeException("This email already used");
        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new RuntimeException("This username already used");

        User user = userMapper.toEntity(userDto);
        User saved = userRepository.save(user);
        return userMapper.toResponseUser(user);
    }

    public ResponseUserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        return userMapper.toResponseUser(user);
    }

    @Override
    public CursorPageResponse<ResponseUserDto> searchUsers(Long cursor, int size, String username) {
        if (size < 1 || size > 50)
            throw new IllegalArgumentException("Size must be between 1 and 50");
        if (cursor != null && cursor < 0)
            throw new IllegalArgumentException("Cursor must be positive");

        List<User> users = userRepository.
                fetchNextPageFiltered(cursor, username, Limit.of(size + 1));

        boolean hasNext = users.size() > size;

        List<User> page = hasNext
                ? users.subList(0, size)
                : users;

        Long nextCursor = hasNext
                ? page.get(size - 1).getId()
                : null;

        return new CursorPageResponse<>(
                userMapper.toResponseUserList(page),
                size,
                nextCursor,
                hasNext
        );
    }

    @Override
    public ResponseUserDto updateUser(Long userId, UpdateUserDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (updateDto.getUsername() != null) {
            if (!updateDto.getUsername().equals(user.getUsername()) &&
            userRepository.existsByUsername(updateDto.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(updateDto.getUsername());
        }

        if (updateDto.getEmail() != null) {
            if (!updateDto.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmail(updateDto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(updateDto.getEmail());
        }

        if (updateDto.getPassword() != null) {
            user.setPassword(updateDto.getPassword());
        }

        if (updateDto.getAvatarUrl() != null) {
            user.setAvatarUrl(updateDto.getAvatarUrl());
        }
        User updated = userRepository.save(user);
        return userMapper.toResponseUser(updated);

    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found with id: " + userId);
        userRepository.deleteById(userId);
    }
}
