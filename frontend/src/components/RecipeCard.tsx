import { Link } from 'react-router-dom';
import { difficultyLabel, mealTimeLabel, preferenceLabel } from '../constants/labels';
import type { Recipe } from '../types';

interface RecipeCardProps {
  recipe: Recipe;
  onDelete?: (id: number) => void;
  showAuthorLink?: boolean;
}

export function RecipeCard({ recipe, onDelete, showAuthorLink }: RecipeCardProps) {
  return (
    <article className="recipe-card">
      {recipe.imageUrl ? (
        <img src={recipe.imageUrl} alt="" className="recipe-card-img" />
      ) : (
        <div className="recipe-card-img recipe-card-placeholder">🍽</div>
      )}
      <div className="recipe-card-body">
        <h3>
          <Link to={`/recipes/${recipe.id}`}>{recipe.title}</Link>
        </h3>
        {recipe.difficultyLevel && (
          <p className="meta">{difficultyLabel[recipe.difficultyLevel]}</p>
        )}
        {recipe.mealTime && (
          <p className="meta">{mealTimeLabel[recipe.mealTime]}</p>
        )}
        {recipe.commentsCount != null && (
          <p className="meta">Комментариев: {recipe.commentsCount}</p>
        )}
        {recipe.tags && recipe.tags.length > 0 && (
          <div className="tag-row">
            {recipe.tags.map((t) => (
              <span key={t} className="tag">
                {preferenceLabel[t]}
              </span>
            ))}
          </div>
        )}
        <div className="recipe-card-actions">
          <Link to={`/recipes/${recipe.id}`} className="btn btn-sm btn-secondary">
            Открыть
          </Link>
          {showAuthorLink && (
            <Link to={`/users/${recipe.authorId}`} className="btn btn-sm btn-ghost">
              Автор
            </Link>
          )}
          {onDelete && (
            <button
              type="button"
              className="btn btn-sm btn-danger"
              onClick={() => onDelete(recipe.id)}
            >
              Удалить
            </button>
          )}
        </div>
      </div>
    </article>
  );
}
