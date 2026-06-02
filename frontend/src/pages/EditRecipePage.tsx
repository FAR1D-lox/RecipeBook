import { FormEvent, useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { recipesApi, uploadApi } from '../api';
import { TagPicker } from '../components/TagPicker';
import {
  DIFFICULTY_LEVELS,
  MEAL_TIMES,
  difficultyLabel,
  mealTimeLabel,
} from '../constants/labels';
import { useAuth } from '../context/AuthContext';
import { HttpError } from '../api/client';
import type { DifficultyLevel, MealTime, PreferenceTag, Recipe } from '../types';

export function EditRecipePage() {
  const { id } = useParams<{ id: string }>();
  const recipeId = Number(id);
  const navigate = useNavigate();
  const { user } = useAuth();
  const descriptionRef = useRef<HTMLTextAreaElement | null>(null);

  const [recipe, setRecipe] = useState<Recipe | null>(null);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [difficultyLevel, setDifficultyLevel] = useState<DifficultyLevel>('EASY');
  const [mealTime, setMealTime] = useState<MealTime>('LUNCH');
  const [preparationTime, setPreparationTime] = useState('');
  const [cookingTime, setCookingTime] = useState('');
  const [tags, setTags] = useState<PreferenceTag[]>([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [insertingImage, setInsertingImage] = useState(false);

  const canInsertImage = useMemo(() => !insertingImage, [insertingImage]);

  const insertIntoDescription = (text: string) => {
    const el = descriptionRef.current;
    if (!el) {
      setDescription((prev) => prev + text);
      return;
    }
    const start = el.selectionStart ?? description.length;
    const end = el.selectionEnd ?? description.length;
    const next = description.slice(0, start) + text + description.slice(end);
    setDescription(next);
    requestAnimationFrame(() => {
      el.focus();
      const pos = start + text.length;
      el.setSelectionRange(pos, pos);
    });
  };

  useEffect(() => {
    recipesApi
      .getById(recipeId)
      .then((r) => {
        if (user && r.authorId !== user.id) {
          setError('Нет прав на редактирование');
          return;
        }
        setRecipe(r);
        setTitle(r.title);
        setDescription(r.description);
        setImageUrl(r.imageUrl ?? '');
        if (r.difficultyLevel) setDifficultyLevel(r.difficultyLevel);
        if (r.mealTime) setMealTime(r.mealTime);
        setPreparationTime(r.preparationTime?.toString() ?? '');
        setCookingTime(r.cookingTime?.toString() ?? '');
        setTags(r.tags ?? []);
      })
      .catch((err) =>
        setError(err instanceof HttpError ? err.message : 'Ошибка загрузки'),
      )
      .finally(() => setLoading(false));
  }, [recipeId, user]);

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await recipesApi.update(recipeId, {
        title: title.trim(),
        description,
        imageUrl: imageUrl || undefined,
        difficultyLevel,
        mealTime,
        preparationTime: preparationTime ? Number(preparationTime) : undefined,
        cookingTime: cookingTime ? Number(cookingTime) : undefined,
        tags,
      });
      navigate(`/recipes/${recipeId}`);
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Ошибка сохранения');
    } finally {
      setSubmitting(false);
    }
  };

  const onDelete = async () => {
    if (!confirm('Удалить рецепт?')) return;
    try {
      await recipesApi.remove(recipeId);
      navigate('/profile');
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Не удалось удалить');
    }
  };

  if (loading) {
    return (
      <div className="page-center">
        <p className="muted">Загрузка…</p>
      </div>
    );
  }

  if (!recipe) {
    return (
      <div className="page">
        <p className="form-error">{error || 'Рецепт не найден'}</p>
      </div>
    );
  }

  return (
    <div className="page narrow">
      <h1>Редактирование рецепта</h1>
      <form onSubmit={onSubmit} className="form card">
        <label>
          Название
          <input value={title} onChange={(e) => setTitle(e.target.value)} required />
        </label>
        <label>
          Описание (Markdown)
          <textarea
            ref={descriptionRef}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
            rows={10}
          />
        </label>
        <label>
          Добавить фото в описание (вставит Markdown-ссылку)
          <input
            type="file"
            accept="image/*"
            disabled={!canInsertImage}
            onChange={async (e) => {
              const file = e.target.files?.[0] ?? null;
              if (!file) return;
              setInsertingImage(true);
              try {
                const url = await uploadApi.image(file, 'recipes');
                insertIntoDescription(`\n\n![](${url})\n\n`);
              } catch (err) {
                alert(err instanceof HttpError ? err.message : 'Ошибка загрузки');
              } finally {
                setInsertingImage(false);
              }
            }}
          />
        </label>
        <label>
          URL изображения
          <input value={imageUrl} onChange={(e) => setImageUrl(e.target.value)} />
        </label>
        <label>
          Заменить изображение
          <input
            type="file"
            accept="image/*"
            onChange={async (e) => {
              const file = e.target.files?.[0];
              if (!file) return;
              try {
                setImageUrl(await uploadApi.image(file, 'recipes'));
              } catch (err) {
                alert(err instanceof HttpError ? err.message : 'Ошибка загрузки');
              }
            }}
          />
        </label>
        <label>
          Сложность
          <select
            value={difficultyLevel}
            onChange={(e) => setDifficultyLevel(e.target.value as DifficultyLevel)}
          >
            {DIFFICULTY_LEVELS.map((d) => (
              <option key={d} value={d}>
                {difficultyLabel[d]}
              </option>
            ))}
          </select>
        </label>
        <label>
          Приём пищи
          <select value={mealTime} onChange={(e) => setMealTime(e.target.value as MealTime)}>
            {MEAL_TIMES.map((m) => (
              <option key={m} value={m}>
                {mealTimeLabel[m]}
              </option>
            ))}
          </select>
        </label>
        <label>
          Подготовка (мин)
          <input
            type="number"
            min={0}
            value={preparationTime}
            onChange={(e) => setPreparationTime(e.target.value)}
          />
        </label>
        <label>
          Готовка (мин)
          <input
            type="number"
            min={0}
            value={cookingTime}
            onChange={(e) => setCookingTime(e.target.value)}
          />
        </label>
        <fieldset>
          <legend>Теги</legend>
          <TagPicker value={tags} onChange={setTags} />
        </fieldset>
        {error && <p className="form-error">{error}</p>}
        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            Сохранить
          </button>
          <button type="button" className="btn btn-danger" onClick={onDelete}>
            Удалить рецепт
          </button>
        </div>
      </form>
    </div>
  );
}
