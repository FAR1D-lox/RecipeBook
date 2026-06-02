import { useEffect, useState } from 'react';
import { Link, useLocation, useParams } from 'react-router-dom';
import { recipesApi, resolveUserById } from '../api';
import { RecipeCard } from '../components/RecipeCard';
import { UserAvatar } from '../components/UserAvatar';
import { preferenceLabel } from '../constants/labels';
import { useAuth } from '../context/AuthContext';
import { HttpError } from '../api/client';
import type { Recipe, User } from '../types';

export function UserProfilePage() {
  const { id } = useParams<{ id: string }>();
  const userId = Number(id);
  const location = useLocation();
  const { user: currentUser } = useAuth();
  const stateUser = (location.state as { user?: User } | null)?.user;

  const [profile, setProfile] = useState<User | null>(stateUser ?? null);
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  const isOwnProfile = currentUser?.id === userId;

  useEffect(() => {
    let cancelled = false;
    (async () => {
      setLoading(true);
      setError('');
      try {
        let user = stateUser ?? null;
        if (!user) {
          user = await resolveUserById(userId);
        }
        if (!user) {
          throw new Error('Пользователь не найден');
        }
        const authorRecipes = await recipesApi.byAuthor(userId);
        if (!cancelled) {
          setProfile(user);
          setRecipes(authorRecipes);
        }
      } catch (err) {
        if (!cancelled) {
          setError(err instanceof HttpError ? err.message : (err as Error).message);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [userId, stateUser]);

  if (loading) {
    return (
      <div className="page-center">
        <p className="muted">Загрузка профиля…</p>
      </div>
    );
  }

  if (error || !profile) {
    return (
      <div className="page">
        <p className="form-error">{error || 'Профиль не найден'}</p>
        <Link to="/users/search" className="btn btn-secondary">
          К поиску
        </Link>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="profile-header card">
        <UserAvatar username={profile.username} avatarUrl={profile.avatarUrl} size="lg" />
        <div>
          <h1>{profile.username}</h1>
          {isOwnProfile && (
            <Link to="/profile/edit" className="btn btn-primary">
              Редактировать профиль
            </Link>
          )}
          {profile.preferences && profile.preferences.length > 0 && (
            <div className="tag-row">
              {profile.preferences.map((t) => (
                <span key={t} className="tag">
                  {preferenceLabel[t]}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>

      <h2>Рецепты ({recipes.length})</h2>
      {recipes.length === 0 ? (
        <p className="muted">Пока нет рецептов</p>
      ) : (
        <div className="recipe-grid">
          {recipes.map((r) => (
            <RecipeCard key={r.id} recipe={r} />
          ))}
        </div>
      )}
    </div>
  );
}
