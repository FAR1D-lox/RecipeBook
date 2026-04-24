package ru.urfu.recipe_book.recipe.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.urfu.recipe_book.recipe.entity.Recipe;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "likesCount", ignore = true)
    @Mapping(target = "dislikesCount", ignore = true)
    @Mapping(target = "commentsCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Recipe toEntity(CreateRecipeDto createDto);

    @Mapping(target = "authorId", source = "author.id")
    RecipeResponseDto toRecipeResponse(Recipe recipe);

    List<RecipeResponseDto> toRecipeResponseList(List<Recipe> recipeList);

}
