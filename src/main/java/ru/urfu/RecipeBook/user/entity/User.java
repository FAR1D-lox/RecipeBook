package ru.urfu.RecipeBook.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.RecipeBook.common.entities.BaseEntity;
import ru.urfu.RecipeBook.common.enums.Role;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User extends BaseEntity {
    private String username;

    private String email;

    private String password;

    private String avatarUrl;

    private boolean isActive;

    private Role role;
}
