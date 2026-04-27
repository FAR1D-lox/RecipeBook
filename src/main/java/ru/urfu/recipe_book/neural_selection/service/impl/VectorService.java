package ru.urfu.recipe_book.neural_selection.service.impl;

import org.springframework.stereotype.Service;
import ru.urfu.recipe_book.common.enums.PreferenceTag;
import ru.urfu.recipe_book.neural_selection.entity.PreferenceVector;
import ru.urfu.recipe_book.recipe.entity.Recipe;
import ru.urfu.recipe_book.user.entity.User;

import java.time.LocalTime;
import java.util.Set;

@Service
public class VectorService {

    private double getCurrentMealTimeVector() {
        LocalTime now = LocalTime.now();

        if (now.isAfter(LocalTime.of(4, 0))
                && now.isBefore(LocalTime.of(11,0)))
            return -1;
        else if (now.isAfter(LocalTime.of(11, 0))
                && now.isBefore(LocalTime.of(17, 0)))
            return 0;
        else
            return 1;
    }

    public PreferenceVector userToVector(User user) {
        Set<PreferenceTag> prefs = user.getPreferences();

        double temperature = 0;
        if (prefs.contains(PreferenceTag.COLD)) temperature -= 1;
        if (prefs.contains(PreferenceTag.HOT)) temperature += 1;

        double spiciness = 0;
        if (prefs.contains(PreferenceTag.BLAND)) spiciness -= 1;
        if (prefs.contains(PreferenceTag.SPICY)) spiciness += 1;

        double diet = 0;
        if (prefs.contains(PreferenceTag.VEGETARIAN)) diet -= 1;
        if (prefs.contains(PreferenceTag.MEAT)) diet += 1;

        return new PreferenceVector(temperature, spiciness, diet, getCurrentMealTimeVector());
    }

    public PreferenceVector recipeToVector(Recipe recipe) {
        Set<PreferenceTag> tags = recipe.getTags();

        double temperature = 0;
        if (tags.contains(PreferenceTag.COLD)) temperature -= 1;
        if (tags.contains(PreferenceTag.HOT)) temperature += 1;

        double spiciness = 0;
        if (tags.contains(PreferenceTag.BLAND)) spiciness -= 1;
        if (tags.contains(PreferenceTag.SPICY)) spiciness += 1;

        double diet = 0;
        if (tags.contains(PreferenceTag.VEGETARIAN)) diet -= 1;
        if (tags.contains(PreferenceTag.MEAT)) diet += 1;

        double mealTime = switch (recipe.getMealTime()) {
            case BREAKFAST -> -1;
            case LUNCH -> 0;
            case DINNER -> 1;
        };

        return new PreferenceVector(temperature, spiciness, diet, mealTime);
    }

    public double cosSimilarity(PreferenceVector a, PreferenceVector b) {
        double dotProduct = a.temperature() * b.temperature()
                            + a.spiciness() * b.spiciness()
                            + a.diet() * b.diet()
                            + a.mealTime() * b.mealTime();
        double lenVectorA = Math.sqrt(a.temperature() * a.temperature()
                            + a.spiciness() * a.spiciness()
                            + a.diet() * a.diet()
                            + a.mealTime() * a.mealTime());
        double lenVectorB = Math.sqrt(b.temperature() * b.temperature()
                            + b.spiciness() * b.spiciness()
                            + b.diet() * b.diet()
                            + b.mealTime() * b.mealTime());
        if (lenVectorA * lenVectorB == 0) return 0;
        return dotProduct / (lenVectorA * lenVectorB);
    }
}