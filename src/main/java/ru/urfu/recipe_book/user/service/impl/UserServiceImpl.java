package ru.urfu.recipe_book.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.urfu.recipe_book.comment.repository.CommentRepository;
import ru.urfu.recipe_book.common.enums.Role;
import ru.urfu.recipe_book.favorite.repository.FavoriteRepository;
import ru.urfu.recipe_book.reaction.repository.ReactionRepository;
import ru.urfu.recipe_book.recipe.repository.RecipeRepository;
import ru.urfu.recipe_book.security.JwtService;
import ru.urfu.recipe_book.user.dto.*;
import ru.urfu.recipe_book.user.entity.User;
import ru.urfu.recipe_book.user.repository.UserRepository;
import ru.urfu.recipe_book.user.service.UserService;
import ru.urfu.recipe_book.common.entities.CursorPageResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public ResponseUserDto createUser(CreateUserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new RuntimeException("This email already used");
        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new RuntimeException("This username already used");

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        User saved = userRepository.save(user);
        return userMapper.toResponseUser(saved);
    }

    @Override
    public String authenticate(LoginUserDto loginDto) {
        User user = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username"));
        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );

        return jwtService.generateToken(userDetails);
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
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        if (updateDto.getAvatarUrl() != null) {
            user.setAvatarUrl(updateDto.getAvatarUrl());
        }
        if (updateDto.getPreferences() != null) {
            user.setPreferences(updateDto.getPreferences());
        }

        User updated = userRepository.save(user);
        return userMapper.toResponseUser(updated);

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found with id: " + userId);

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        reactionRepository.updateCountersReactionsForRecipes(userId);
        reactionRepository.deleteByUser(user);

        commentRepository.updateCountersCommentsForRecipes(userId);
        commentRepository.deleteByAuthor(user);


        recipeRepository.deleteByAuthor(user);
        favoriteRepository.deleteByUser(user);

        userRepository.deleteById(userId);
    }
}
