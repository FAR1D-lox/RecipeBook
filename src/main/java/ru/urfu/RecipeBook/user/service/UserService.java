package ru.urfu.RecipeBook.user.service;

import ru.urfu.RecipeBook.user.entity.User;

public interface UserService {

    User createUser(String name, String email, String password);

    void deleteUser(Long userId);

    void updateUsername(Long userId, String newName);

    void updateEmail(Long userId, String newEmail);

    void updatePassword(Long userId, String newPassword);

    void updateAvatarUrl(Long userId, String newAvatarUrl);
}
