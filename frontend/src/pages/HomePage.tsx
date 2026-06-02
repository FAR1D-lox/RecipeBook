import { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { recipesApi } from '../api';
import { HttpError } from '../api/client';
import { RecipeRowCard } from '../components/RecipeRowCard';
import { useAuth } from '../context/AuthContext';
import type { Recipe } from '../types';

export function HomePage() {
  const { isAuthenticated } = useAuth();

  const pageSize = 10;
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [requestCursor, setRequestCursor] = useState<number | null>(null);
  const [nextCursor, setNextCursor] = useState<number | null>(null);
  const [hasNext, setHasNext] = useState(false);
  const [cursorStack, setCursorStack] = useState<(number | null)[]>([]);

  const pageNumber = useMemo(() => cursorStack.length + 1, [cursorStack.length]);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      setLoading(true);
      setError('');
      try {
        const page = await recipesApi.list(requestCursor, pageSize);
        if (cancelled) return;
        setRecipes(page.data);
        setNextCursor(page.cursor);
        setHasNext(page.hasNext);
      } catch (err) {
        if (cancelled) return;
        setError(err instanceof HttpError ? err.message : 'Не удалось загрузить ленту');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [requestCursor, pageSize]);

  const goNext = () => {
    if (!hasNext || nextCursor == null) return;
    setCursorStack((prev) => [...prev, requestCursor]);
    setRequestCursor(nextCursor);
  };

  const goPrev = () => {
    setCursorStack((prev) => {
      if (prev.length === 0) return prev;
      const next = [...prev];
      const prevCursor = next.pop() ?? null;
      setRequestCursor(prevCursor);
      return next;
    });
  };

  return (
    <div className="page">
      <section className="hero">
        <div className="hero-content">
          <h1>Делитесь рецептами с друзьями</h1>
          <p className="lead">
            Ищите блюда, сохраняйте избранное, оценивайте чужие рецепты и получайте
            персональную нейро-подборку.
          </p>
          <div className="hero-actions">
            <Link to="/recipes/search" className="btn btn-primary">
              Найти рецепт
            </Link>
            <Link to="/users/search" className="btn btn-secondary">
              Найти человека
            </Link>
            {isAuthenticated ? (
              <Link to="/recommendations" className="btn btn-ghost">
                Нейро-подборка
              </Link>
            ) : (
              <Link to="/register" className="btn btn-ghost">
                Присоединиться
              </Link>
            )}
          </div>
        </div>
      </section>

      <section className="feed">
        <div className="feed-header">
          <h2>Лента рецептов</h2>
          <div className="feed-pagination">
            <button
              type="button"
              className="btn btn-sm btn-secondary"
              onClick={goPrev}
              disabled={loading || cursorStack.length === 0}
            >
              Назад
            </button>
            <span className="muted small">Страница {pageNumber}</span>
            <button
              type="button"
              className="btn btn-sm btn-primary"
              onClick={goNext}
              disabled={loading || !hasNext}
            >
              Вперёд
            </button>
          </div>
        </div>

        {error && <p className="form-error">{error}</p>}
        {loading ? (
          <p className="muted">Загрузка…</p>
        ) : recipes.length === 0 ? (
          <p className="muted">Рецептов пока нет</p>
        ) : (
          <div className="feed-list">
            {recipes.map((r) => (
              <RecipeRowCard key={r.id} recipe={r} />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
