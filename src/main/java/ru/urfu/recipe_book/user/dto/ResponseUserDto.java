package ru.urfu.recipe_book.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.recipe_book.common.enums.PreferenceTag;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {
    private Long id;
    private String username;
    private String avatarUrl;
    private Set<PreferenceTag> preferences;
}
