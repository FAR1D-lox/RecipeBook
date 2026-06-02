import { FormEvent, useState } from 'react';
import { recipesApi } from '../api';
import { RecipeCard } from '../components/RecipeCard';
import { HttpError } from '../api/client';
import type { Recipe } from '../types';

export function RecipeSearchPage() {
  const [query, setQuery] = useState('');
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);

  const search = async (e: FormEvent) => {
    e.preventDefault();
    if (!query.trim()) return;
    setLoading(true);
    setError('');
    setSearched(true);
    try {
      const list = await recipesApi.search(query.trim());
      setRecipes(list);
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Ошибка поиска');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <h1>Поиск рецептов</h1>
      <form onSubmit={search} className="search-bar">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Название блюда…"
        />
        <button type="submit" className="btn btn-primary" disabled={loading}>
          Найти
        </button>
      </form>
      {error && <p className="form-error">{error}</p>}
      {searched && recipes.length === 0 && !loading && (
        <p className="muted">Ничего не найдено</p>
      )}
      <div className="recipe-grid">
        {recipes.map((r) => (
          <RecipeCard key={r.id} recipe={r} showAuthorLink />
        ))}
      </div>
    </div>
  );
}
