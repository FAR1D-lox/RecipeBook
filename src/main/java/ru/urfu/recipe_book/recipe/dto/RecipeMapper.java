package ru.urfu.recipe_book.recipe.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.urfu.recipe_book.recipe.entity.Recipe;


@Mapper(componentModel = "spring")
public interface RecipeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "likesCount", ignore = true)
    @Mapping(target = "dislikesCount", ignore = true)
    @Mapping(target = "commentsCount", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    Recipe toEntity(CreateRecipeDto createDto);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "description", ignore = true)
    RecipeResponseDto toRecipeResponse(Recipe recipe);
}
