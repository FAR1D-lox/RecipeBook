import { FormEvent, useCallback, useEffect, useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import {
  commentsApi,
  favoritesApi,
  reactionsApi,
  recipesApi,
  uploadApi,
} from '../api';
import {
  difficultyLabel,
  mealTimeLabel,
  preferenceLabel,
} from '../constants/labels';
import { useAuth } from '../context/AuthContext';
import { HttpError } from '../api/client';
import type { Comment, Favorite, ReactionStats, Recipe } from '../types';
import { stripHtmlTags } from '../utils/text';

export function RecipeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const recipeId = Number(id);
  const { isAuthenticated, user } = useAuth();

  const [recipe, setRecipe] = useState<Recipe | null>(null);
  const [stats, setStats] = useState<ReactionStats | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [favorites, setFavorites] = useState<Favorite[]>([]);
  const [commentText, setCommentText] = useState('');
  const [commentUploading, setCommentUploading] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [submittingComment, setSubmittingComment] = useState(false);

  const isOwner = user && recipe && user.id === recipe.authorId;
  const favoriteEntry = favorites.find((f) => f.recipeId === recipeId);

  const loadStats = useCallback(async () => {
    try {
      const s = await reactionsApi.stats(recipeId);
      setStats(s);
    } catch {
      setStats(null);
    }
  }, [recipeId]);

  const load = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const r = await recipesApi.getById(recipeId);
      setRecipe(r);
      const list = await commentsApi.list(recipeId);
      setComments(list);
      await loadStats();
      if (isAuthenticated) {
        const favs = await favoritesApi.list();
        setFavorites(favs);
      }
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Рецепт не найден');
    } finally {
      setLoading(false);
    }
  }, [recipeId, isAuthenticated, loadStats]);

  useEffect(() => {
    load();
  }, [load]);

  const setReaction = async (liked: boolean) => {
    if (!isAuthenticated) return;
    try {
      const current = stats?.currentUserReaction;
      if (current === (liked ? 1 : 0)) {
        await reactionsApi.remove(recipeId);
      } else {
        await reactionsApi.set(recipeId, liked);
      }
      await loadStats();
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Ошибка реакции');
    }
  };

  const toggleFavorite = async () => {
    if (!isAuthenticated) return;
    try {
      if (favoriteEntry) {
        await favoritesApi.remove(favoriteEntry.favoriteId);
        setFavorites((prev) => prev.filter((f) => f.favoriteId !== favoriteEntry.favoriteId));
      } else {
        const created = await favoritesApi.add(recipeId);
        setFavorites((prev) => [...prev, created]);
      }
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Ошибка избранного');
    }
  };

  const submitComment = async (e: FormEvent) => {
    e.preventDefault();
    if (!commentText.trim()) return;
    setSubmittingComment(true);
    try {
      const safeText = stripHtmlTags(commentText.trim());
      const created = await commentsApi.add(recipeId, safeText);
      setComments((prev) => [...prev, created]);
      setCommentText('');
      if (recipe) {
        setRecipe({
          ...recipe,
          commentsCount: (recipe.commentsCount ?? 0) + 1,
        });
      }
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Не удалось отправить');
    } finally {
      setSubmittingComment(false);
    }
  };

  const canUploadCommentImage = useMemo(
    () => isAuthenticated && !commentUploading && !submittingComment,
    [isAuthenticated, commentUploading, submittingComment],
  );

  const uploadCommentImage = async (file: File | null) => {
    if (!file) return;
    setCommentUploading(true);
    try {
      const url = await uploadApi.image(file, 'comments');
      setCommentText((prev) => (prev ? `${prev}\n\n![](${url})\n` : `![](${url})\n`));
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Ошибка загрузки');
    } finally {
      setCommentUploading(false);
    }
  };

  const deleteComment = async (commentId: number) => {
    if (!confirm('Удалить комментарий?')) return;
    try {
      await commentsApi.remove(recipeId, commentId);
      setComments((prev) => prev.filter((c) => c.commentId !== commentId));
      if (recipe) {
        setRecipe({
          ...recipe,
          commentsCount: Math.max(0, (recipe.commentsCount ?? 1) - 1),
        });
      }
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Не удалось удалить');
    }
  };

  if (loading) {
    return (
      <div className="page-center">
        <p className="muted">Загрузка рецепта…</p>
      </div>
    );
  }

  if (error || !recipe) {
    return (
      <div className="page">
        <p className="form-error">{error || 'Рецепт не найден'}</p>
        <Link to="/recipes/search" className="btn btn-secondary">
          К поиску
        </Link>
      </div>
    );
  }

  const userLiked = stats?.currentUserReaction === 1;
  const userDisliked = stats?.currentUserReaction === 0;

  return (
    <article className="page recipe-detail">
      {recipe.imageUrl && (
        <img src={recipe.imageUrl} alt="" className="recipe-hero-img" />
      )}
      <header className="recipe-detail-header">
        <h1>{recipe.title}</h1>
        <p className="meta">
          <Link to={`/users/${recipe.authorId}`}>Автор #{recipe.authorId}</Link>
          {recipe.difficultyLevel && ` · ${difficultyLabel[recipe.difficultyLevel]}`}
          {recipe.mealTime && ` · ${mealTimeLabel[recipe.mealTime]}`}
          {(recipe.preparationTime || recipe.cookingTime) && (
            <>
              {' '}
              ·{' '}
              {recipe.preparationTime ? `подготовка ${recipe.preparationTime} мин` : ''}
              {recipe.cookingTime ? ` готовка ${recipe.cookingTime} мин` : ''}
            </>
          )}
        </p>
        {recipe.tags && recipe.tags.length > 0 && (
          <div className="tag-row">
            {recipe.tags.map((t) => (
              <span key={t} className="tag">
                {preferenceLabel[t]}
              </span>
            ))}
          </div>
        )}
      </header>

      <div className="reaction-bar card">
        {stats ? (
          <p>
            👍 {stats.likesCount} · 👎 {stats.dislikesCount}
          </p>
        ) : (
          <p className="muted">
            {isAuthenticated
              ? 'Статистика лайков недоступна'
              : 'Войдите, чтобы видеть и ставить оценки'}
          </p>
        )}
        {isAuthenticated && (
          <div className="reaction-actions">
            <button
              type="button"
              className={`btn btn-sm ${userLiked ? 'btn-primary' : 'btn-secondary'}`}
              onClick={() => setReaction(true)}
            >
              Нравится
            </button>
            <button
              type="button"
              className={`btn btn-sm ${userDisliked ? 'btn-primary' : 'btn-secondary'}`}
              onClick={() => setReaction(false)}
            >
              Не нравится
            </button>
            <button type="button" className="btn btn-sm btn-ghost" onClick={toggleFavorite}>
              {favoriteEntry ? '★ В избранном' : '☆ В избранное'}
            </button>
          </div>
        )}
        {!isAuthenticated && stats && (
          <p className="muted small">Оценки видны всем; ставить их могут только авторизованные</p>
        )}
      </div>

      {isOwner && (
        <div className="owner-actions">
          <Link to={`/recipes/${recipeId}/edit`} className="btn btn-secondary">
            Редактировать
          </Link>
          <button
            type="button"
            className="btn btn-danger"
            onClick={async () => {
              if (!confirm('Удалить рецепт?')) return;
              try {
                await recipesApi.remove(recipeId);
                window.location.href = '/profile';
              } catch (err) {
                alert(err instanceof HttpError ? err.message : 'Ошибка');
              }
            }}
          >
            Удалить
          </button>
        </div>
      )}

      <section
        className="recipe-body card"
        dangerouslySetInnerHTML={{ __html: recipe.description }}
      />

      <section className="comments-section">
        <h2>
          Комментарии ({recipe.commentsCount ?? comments.length})
        </h2>
        {isAuthenticated && (
          <form onSubmit={submitComment} className="comment-form">
            <textarea
              value={commentText}
              onChange={(e) => setCommentText(e.target.value)}
              placeholder="Ваш комментарий…"
              rows={3}
              required
            />
            <div className="form-actions">
              <label className="btn btn-sm btn-secondary" style={{ cursor: canUploadCommentImage ? 'pointer' : 'not-allowed' }}>
                Фото
                <input
                  type="file"
                  accept="image/*"
                  disabled={!canUploadCommentImage}
                  style={{ display: 'none' }}
                  onChange={(e) => uploadCommentImage(e.target.files?.[0] ?? null)}
                />
              </label>
              <button
                type="submit"
                className="btn btn-primary"
                disabled={submittingComment}
              >
                Отправить
              </button>
            </div>
          </form>
        )}
        {!isAuthenticated && (
          <p className="muted">
            <Link to="/login">Войдите</Link>, чтобы оставить комментарий
          </p>
        )}
        <ul className="comment-list">
          {comments.map((c) => (
            <li key={c.commentId} className="card comment-item">
              <div className="comment-meta">
                <strong>{c.authorUsername}</strong>
                {user?.id === c.authorId && (
                  <button
                    type="button"
                    className="btn btn-sm btn-ghost"
                    onClick={() => deleteComment(c.commentId)}
                  >
                    Удалить
                  </button>
                )}
              </div>
              <p>{stripHtmlTags(c.text)}</p>
            </li>
          ))}
        </ul>
      </section>
    </article>
  );
}
