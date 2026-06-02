import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { favoritesApi } from '../api';
import { HttpError } from '../api/client';
import type { Favorite } from '../types';

export function FavoritesPage() {
  const [items, setItems] = useState<Favorite[]>([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  const load = () => {
    setLoading(true);
    favoritesApi
      .list()
      .then(setItems)
      .catch((err) =>
        setError(err instanceof HttpError ? err.message : 'Ошибка загрузки'),
      )
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const remove = async (favoriteId: number) => {
    try {
      await favoritesApi.remove(favoriteId);
      setItems((prev) => prev.filter((f) => f.favoriteId !== favoriteId));
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Не удалось удалить');
    }
  };

  return (
    <div className="page">
      <h1>Избранное</h1>
      {loading && <p className="muted">Загрузка…</p>}
      {error && <p className="form-error">{error}</p>}
      {!loading && items.length === 0 && <p className="muted">Список пуст</p>}
      <ul className="favorite-list">
        {items.map((f) => (
          <li key={f.favoriteId} className="card favorite-item">
            {f.recipeImageUrl ? (
              <img src={f.recipeImageUrl} alt="" className="favorite-thumb" />
            ) : (
              <div className="favorite-thumb favorite-thumb-placeholder">🍽</div>
            )}
            <div className="favorite-info">
              <Link to={`/recipes/${f.recipeId}`}>
                <strong>{f.recipeTitle}</strong>
              </Link>
            </div>
            <button
              type="button"
              className="btn btn-sm btn-danger"
              onClick={() => remove(f.favoriteId)}
            >
              Убрать
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
