import { FormEvent, useMemo, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { recipesApi, uploadApi } from '../api';
import { TagPicker } from '../components/TagPicker';
import {
  DIFFICULTY_LEVELS,
  MEAL_TIMES,
  difficultyLabel,
  mealTimeLabel,
} from '../constants/labels';
import { HttpError } from '../api/client';
import type { DifficultyLevel, MealTime, PreferenceTag } from '../types';

export function CreateRecipePage() {
  const navigate = useNavigate();
  const descriptionRef = useRef<HTMLTextAreaElement | null>(null);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [difficultyLevel, setDifficultyLevel] = useState<DifficultyLevel>('EASY');
  const [mealTime, setMealTime] = useState<MealTime>('LUNCH');
  const [preparationTime, setPreparationTime] = useState('');
  const [cookingTime, setCookingTime] = useState('');
  const [tags, setTags] = useState<PreferenceTag[]>([]);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [insertingImage, setInsertingImage] = useState(false);

  const canInsertImage = useMemo(() => !uploading && !insertingImage, [uploading, insertingImage]);

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

  const onImageUpload = async (file: File | null) => {
    if (!file) return;
    setUploading(true);
    try {
      const url = await uploadApi.image(file, 'recipes');
      setImageUrl(url);
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Ошибка загрузки');
    } finally {
      setUploading(false);
    }
  };

  const onDescriptionImageUpload = async (file: File | null) => {
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
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      const created = await recipesApi.create({
        title: title.trim(),
        description,
        imageUrl: imageUrl || undefined,
        difficultyLevel,
        mealTime,
        preparationTime: preparationTime ? Number(preparationTime) : undefined,
        cookingTime: cookingTime ? Number(cookingTime) : undefined,
        tags,
      });
      navigate(`/recipes/${created.id}`);
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Не удалось создать рецепт');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="page narrow">
      <h1>Новый рецепт</h1>
      <p className="muted">Описание поддерживает Markdown</p>
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
            onChange={(e) => onDescriptionImageUpload(e.target.files?.[0] ?? null)}
          />
        </label>
        <label>
          URL изображения
          <input value={imageUrl} onChange={(e) => setImageUrl(e.target.value)} />
        </label>
        <label>
          Загрузить изображение
          <input
            type="file"
            accept="image/*"
            disabled={uploading}
            onChange={(e) => onImageUpload(e.target.files?.[0] ?? null)}
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
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Сохранение…' : 'Опубликовать'}
        </button>
      </form>
    </div>
  );
}
