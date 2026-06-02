import { useEffect, useState } from 'react';
import { recommendationsApi } from '../api';
import { RecipeCard } from '../components/RecipeCard';
import { HttpError } from '../api/client';
import type { Recipe } from '../types';

export function RecommendationsPage() {
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    recommendationsApi
      .list()
      .then(setRecipes)
      .catch((err) =>
        setError(err instanceof HttpError ? err.message : 'Не удалось загрузить подборку'),
      )
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="page">
      <h1>Нейро-подборка</h1>
      <p className="muted">Рецепты, подобранные по вашим предпочтениям и активности</p>
      {loading && <p className="muted">Загрузка…</p>}
      {error && <p className="form-error">{error}</p>}
      {!loading && recipes.length === 0 && !error && (
        <p className="muted">Пока нет рекомендаций — оцените несколько рецептов</p>
      )}
      <div className="recipe-grid">
        {recipes.map((r) => (
          <RecipeCard key={r.id} recipe={r} showAuthorLink />
        ))}
      </div>
    </div>
  );
}
