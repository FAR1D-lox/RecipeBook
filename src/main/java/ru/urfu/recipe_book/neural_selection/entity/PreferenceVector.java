package ru.urfu.recipe_book.neural_selection.entity;

public record PreferenceVector(
        double temperature,
        double spiciness,
        double diet,
        double mealTime
) {
}
