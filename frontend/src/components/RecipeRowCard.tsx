import { Link } from 'react-router-dom';
import { difficultyLabel, mealTimeLabel, preferenceLabel } from '../constants/labels';
import type { Recipe } from '../types';

export function RecipeRowCard({ recipe }: { recipe: Recipe }) {
  return (
    <article className="recipe-row-card">
      <Link to={`/recipes/${recipe.id}`} className="recipe-row-media" aria-label={recipe.title}>
        {recipe.imageUrl ? (
          <img src={recipe.imageUrl} alt="" className="recipe-row-img" />
        ) : (
          <div className="recipe-row-img recipe-row-placeholder">🍽</div>
        )}
      </Link>

      <div className="recipe-row-body">
        <h3 className="recipe-row-title">
          <Link to={`/recipes/${recipe.id}`}>{recipe.title}</Link>
        </h3>

        <div className="recipe-row-meta">
          {recipe.difficultyLevel && <span>{difficultyLabel[recipe.difficultyLevel]}</span>}
          {recipe.mealTime && <span>{mealTimeLabel[recipe.mealTime]}</span>}
          {recipe.commentsCount != null && <span>Комментариев: {recipe.commentsCount}</span>}
        </div>

        {recipe.tags && recipe.tags.length > 0 && (
          <div className="tag-row">
            {recipe.tags.map((t) => (
              <span key={t} className="tag">
                {preferenceLabel[t]}
              </span>
            ))}
          </div>
        )}

        <div className="recipe-row-actions">
          <Link to={`/recipes/${recipe.id}`} className="btn btn-sm btn-secondary">
            Открыть
          </Link>
          <Link to={`/users/${recipe.authorId}`} className="btn btn-sm btn-ghost">
            Автор
          </Link>
        </div>
      </div>
    </article>
  );
}

