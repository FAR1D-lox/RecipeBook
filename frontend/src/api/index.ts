import { apiFetch, apiFetchText } from './client';
import type {
  Comment,
  CreateRecipePayload,
  CreateUserPayload,
  CursorPage,
  Favorite,
  ReactionStats,
  Recipe,
  UpdateUserPayload,
  User,
} from '../types';

export const authApi = {
  register: (body: CreateUserPayload) =>
    apiFetch<User>('/users/register', {
      method: 'POST',
      body: JSON.stringify(body),
      auth: false,
    }),

  login: (username: string, password: string) =>
    apiFetchText('/users/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
      auth: false,
    }),
};

export const usersApi = {
  search: (username: string, cursor?: number | null, size = 10) => {
    const params = new URLSearchParams({ username, size: String(size) });
    if (cursor != null) params.set('cursor', String(cursor));
    return apiFetch<CursorPage<User>>(`/users/search?${params}`, { auth: false });
  },

  update: (body: UpdateUserPayload) =>
    apiFetch<User>('/users', { method: 'PUT', body: JSON.stringify(body) }),

  remove: () => apiFetch<void>('/users', { method: 'DELETE' }),

  /** GET /users/{id} */
  getById: (id: number) =>
    apiFetch<User>(`/users/${id}`, { auth: false }),

  me: () => apiFetch<User>('/users/me'),
};

export const recipesApi = {
  list: (cursor?: number | null, size = 10) => {
    const params = new URLSearchParams({ size: String(size) });
    if (cursor != null) params.set('cursor', String(cursor));
    return apiFetch<CursorPage<Recipe>>(`/recipes?${params}`, { auth: false });
  },

  getById: (id: number) =>
    apiFetch<Recipe>(`/recipes/${id}`, { auth: false }),

  search: (query: string) =>
    apiFetch<Recipe[]>(`/recipes/search?${new URLSearchParams({ query })}`, {
      auth: false,
    }),

  byAuthor: (authorId: number) =>
    apiFetch<Recipe[]>(`/recipes/author/${authorId}`, { auth: false }),

  create: (body: CreateRecipePayload) =>
    apiFetch<Recipe>('/recipes', { method: 'POST', body: JSON.stringify(body) }),

  remove: (id: number) => apiFetch<void>(`/recipes/${id}`, { method: 'DELETE' }),

  /** PUT /recipes/{id} */
  update: (id: number, body: CreateRecipePayload) =>
    apiFetch<Recipe>(`/recipes/${id}`, {
      method: 'PUT',
      body: JSON.stringify(body),
    }),
};

export const reactionsApi = {
  stats: (recipeId: number) =>
    apiFetch<ReactionStats>(`/recipes/${recipeId}/likes/stats`, { auth: false }),

  set: (recipeId: number, liked: boolean) =>
    apiFetch(`/recipes/${recipeId}/likes`, {
      method: 'POST',
      body: JSON.stringify({ liked }),
    }),

  remove: (recipeId: number) =>
    apiFetch<void>(`/recipes/${recipeId}/likes`, { method: 'DELETE' }),
};

export const commentsApi = {
  list: (recipeId: number) =>
    apiFetch<Comment[]>(`/recipes/${recipeId}/comments`, { auth: false }),

  add: (recipeId: number, text: string) =>
    apiFetch<Comment>(`/recipes/${recipeId}/comments`, {
      method: 'POST',
      body: JSON.stringify({ text }),
    }),

  remove: (recipeId: number, commentId: number) =>
    apiFetch<void>(`/recipes/${recipeId}/comments/${commentId}`, {
      method: 'DELETE',
    }),
};

export const favoritesApi = {
  list: () => apiFetch<Favorite[]>('/users/favorites'),

  add: (recipeId: number) =>
    apiFetch<Favorite>('/users/favorites', {
      method: 'POST',
      body: JSON.stringify({ recipeId }),
    }),

  remove: (favoriteId: number) =>
    apiFetch<void>(`/users/favorites/${favoriteId}`, { method: 'DELETE' }),
};

export const recommendationsApi = {
  list: () => apiFetch<Recipe[]>('/recommendations'),
};

export const uploadApi = {
  image: (file: File, bucketName = 'recipes') => {
    const form = new FormData();
    form.append('file', file);
    form.append('bucketName', bucketName);
    form.append('compressedImageWidth', '1200');
    form.append('compressedImageHeight', '800');
    return apiFetchText('/upload', { method: 'POST', body: form });
  },
};

export async function resolveUserById(id: number): Promise<User | null> {
  try {
    return await usersApi.getById(id);
  } catch {
    return null;
  }
}

export async function resolveUserByUsername(username: string): Promise<User | null> {
  const page = await usersApi.search(username, null, 20);
  return page.data.find((u) => u.username === username) ?? page.data[0] ?? null;
}
