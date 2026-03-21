package ru.urfu.RecipeBook.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;
import ru.urfu.RecipeBook.user.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
    }

    public User createUser(String name, String email, String password) {
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void updateUsername(Long userId, String newName) {
        User user = findById(userId);
        user.setUsername(newName);
    }

    public void updateEmail(Long userId, String newEmail) {
        User user = findById(userId);
        user.setEmail(newEmail);
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = findById(userId);
        user.setPassword(newPassword);
    }

    public void updateAvatarUrl(Long userId, String newAvatarUrl) {
        User user = findById(userId);
        user.setAvatarUrl(newAvatarUrl);
    }

}
