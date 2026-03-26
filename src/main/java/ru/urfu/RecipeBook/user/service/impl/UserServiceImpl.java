package ru.urfu.RecipeBook.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.user.dto.CreateUserDto;
import ru.urfu.RecipeBook.user.dto.ResponseUserDto;
import ru.urfu.RecipeBook.user.dto.UpdateUserDto;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;
import ru.urfu.RecipeBook.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseUserDto createUser(CreateUserDto createDto) {

        if (userRepository.existsByEmail(createDto.getEmail()))
            throw new RuntimeException("This email already used");
        if (userRepository.existsByUsername(createDto.getUsername()))
            throw new RuntimeException("This username already used");

        User user = new User();
        user.setUsername(createDto.getUsername());
        user.setEmail(createDto.getEmail());
        user.setPassword(createDto.getPassword());

        User saved = userRepository.save(user);
        return mapToResponseDto(saved);
    }

    public ResponseUserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        return mapToResponseDto(user);
    }

    @Override
    public List<ResponseUserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseUserDto> searchUsers(String username) {
        return userRepository
                .findByUsernameContainingIgnoreCase(username)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
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
        return mapToResponseDto(updated);

    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found with id: " + userId);
        userRepository.deleteById(userId);
    }

    private ResponseUserDto mapToResponseDto(User user) {
        ResponseUserDto dto = new ResponseUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setAvatarUrl(user.getAvatarUrl());

        return dto;
    }

}
