export type PreferenceTag =
  | 'COLD'
  | 'HOT'
  | 'SPICY'
  | 'BLAND'
  | 'MEAT'
  | 'VEGETARIAN';

export type DifficultyLevel = 'EASY' | 'MEDIUM' | 'HARD';
export type MealTime = 'BREAKFAST' | 'LUNCH' | 'DINNER';

export interface User {
  id: number;
  username: string;
  avatarUrl?: string | null;
  preferences?: PreferenceTag[];
}

export interface CursorPage<T> {
  data: T[];
  pageSize: number;
  cursor: number | null;
  hasNext: boolean;
}

export interface Recipe {
  id: number;
  authorId: number;
  title: string;
  description: string;
  preparationTime?: number;
  cookingTime?: number;
  difficultyLevel?: DifficultyLevel;
  imageUrl?: string | null;
  commentsCount?: number;
  tags?: PreferenceTag[];
  mealTime?: MealTime;
}

export interface Comment {
  commentId: number;
  authorId: number;
  authorUsername: string;
  text: string;
}

export interface ReactionStats {
  recipeId: number;
  likesCount: number;
  dislikesCount: number;
  currentUserReaction: number | null;
}

export interface Favorite {
  favoriteId: number;
  userId: number;
  username: string;
  recipeId: number;
  recipeTitle: string;
  recipeImageUrl?: string | null;
}

export interface ApiError {
  status: number;
  message: string;
}

export interface CreateUserPayload {
  username: string;
  email: string;
  password: string;
  preferences?: PreferenceTag[];
}

export interface UpdateUserPayload {
  username?: string;
  email?: string;
  password?: string;
  avatarUrl?: string;
  preferences?: PreferenceTag[];
}

export interface CreateRecipePayload {
  title: string;
  description: string;
  imageUrl?: string;
  difficultyLevel?: DifficultyLevel;
  preparationTime?: number;
  cookingTime?: number;
  tags?: PreferenceTag[];
  mealTime?: MealTime;
}
