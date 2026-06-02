import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { recipesApi } from '../api';
import { RecipeCard } from '../components/RecipeCard';
import { UserAvatar } from '../components/UserAvatar';
import { preferenceLabel } from '../constants/labels';
import { useAuth } from '../context/AuthContext';
import { HttpError } from '../api/client';
import type { Recipe } from '../types';

export function MyProfilePage() {
  const { user, refreshUser } = useAuth();
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    if (!user?.id) return;
    setLoading(true);
    setError('');
    try {
      await refreshUser();
      const list = await recipesApi.byAuthor(user.id);
      setRecipes(list);
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Ошибка загрузки');
    } finally {
      setLoading(false);
    }
  }, [user?.id, refreshUser]);

  useEffect(() => {
    load();
  }, [load]);

  const handleDeleteRecipe = async (recipeId: number) => {
    if (!confirm('Удалить этот рецепт?')) return;
    try {
      await recipesApi.remove(recipeId);
      setRecipes((prev) => prev.filter((r) => r.id !== recipeId));
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Не удалось удалить');
    }
  };

  if (!user) {
    return (
      <div className="page-center">
        <p className="muted">Загрузка…</p>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="profile-header card">
        <UserAvatar username={user.username} avatarUrl={user.avatarUrl} size="lg" />
        <div>
          <h1>Мой профиль</h1>
          <p className="muted">@{user.username}</p>
          <Link to="/profile/edit" className="btn btn-primary">
            Редактировать профиль
          </Link>
          {user.preferences && user.preferences.length > 0 && (
            <div className="tag-row">
              {user.preferences.map((t) => (
                <span key={t} className="tag">
                  {preferenceLabel[t]}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>

      {error && <p className="form-error">{error}</p>}
      <h2>Мои рецепты</h2>
      {loading ? (
        <p className="muted">Загрузка…</p>
      ) : recipes.length === 0 ? (
        <p className="muted">
          Вы ещё не добавили рецепты.{' '}
          <Link to="/recipes/new">Создать первый</Link>
        </p>
      ) : (
        <div className="recipe-grid">
          {recipes.map((r) => (
            <RecipeCard key={r.id} recipe={r} onDelete={handleDeleteRecipe} />
          ))}
        </div>
      )}
    </div>
  );
}
