package ru.urfu.recipe_book.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.recipe_book.common.entities.BaseEntity;
import ru.urfu.recipe_book.common.enums.PreferenceTag;
import ru.urfu.recipe_book.common.enums.Role;

import java.util.HashSet;
import java.util.Set;


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

    @Column(columnDefinition = "TEXT")
    private String avatarUrl;

    private boolean isActive;

    private Role role;

    @ElementCollection
    @CollectionTable(name = "user_preferences",
    joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "preference")
    private Set<PreferenceTag> preferences = new HashSet<>();
}
