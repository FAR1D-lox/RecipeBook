import type { DifficultyLevel, MealTime, PreferenceTag } from '../types';

export const PREFERENCE_TAGS: PreferenceTag[] = [
  'COLD',
  'HOT',
  'SPICY',
  'BLAND',
  'MEAT',
  'VEGETARIAN',
];

export const preferenceLabel: Record<PreferenceTag, string> = {
  COLD: 'Холодное',
  HOT: 'Горячее',
  SPICY: 'Острое',
  BLAND: 'Нейтральное',
  MEAT: 'Мясное',
  VEGETARIAN: 'Вегетарианское',
};

export const difficultyLabel: Record<DifficultyLevel, string> = {
  EASY: 'Легко',
  MEDIUM: 'Средне',
  HARD: 'Сложно',
};

export const mealTimeLabel: Record<MealTime, string> = {
  BREAKFAST: 'Завтрак',
  LUNCH: 'Обед',
  DINNER: 'Ужин',
};

export const DIFFICULTY_LEVELS: DifficultyLevel[] = ['EASY', 'MEDIUM', 'HARD'];
export const MEAL_TIMES: MealTime[] = ['BREAKFAST', 'LUNCH', 'DINNER'];
