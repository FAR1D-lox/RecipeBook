package ru.urfu.recipe_book.neural_selection.entity;

import ru.urfu.recipe_book.recipe.entity.Recipe;

public record ScoredRecipe(Recipe recipe, double score) {
}
