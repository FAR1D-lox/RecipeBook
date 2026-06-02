import { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { uploadApi, usersApi } from '../api';
import { TagPicker } from '../components/TagPicker';
import { useAuth } from '../context/AuthContext';
import { HttpError } from '../api/client';
import { setStoredUser } from '../utils/storage';
import type { PreferenceTag } from '../types';

export function EditProfilePage() {
  const { user, logout, setUser } = useAuth();
  const navigate = useNavigate();

  const [username, setUsername] = useState(user?.username ?? '');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [avatarUrl, setAvatarUrl] = useState(user?.avatarUrl ?? '');
  const [preferences, setPreferences] = useState<PreferenceTag[]>(user?.preferences ?? []);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [uploading, setUploading] = useState(false);

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!user) return;
    setError('');
    setSuccess('');
    setSubmitting(true);
    try {
      const updated = await usersApi.update({
        username: username !== user.username ? username : undefined,
        email: email || undefined,
        password: password || undefined,
        avatarUrl: avatarUrl || undefined,
        preferences,
      });
      setStoredUser(updated);
      setUser(updated);
      setSuccess('Профиль сохранён');
      navigate('/profile');
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Ошибка сохранения');
    } finally {
      setSubmitting(false);
    }
  };

  const onDelete = async () => {
    if (!confirm('Удалить аккаунт безвозвратно? Все рецепты и данные будут удалены.')) {
      return;
    }
    try {
      await usersApi.remove();
      logout();
      navigate('/');
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Не удалось удалить аккаунт');
    }
  };

  const onAvatarUpload = async (file: File | null) => {
    if (!file) return;
    setUploading(true);
    try {
      const url = await uploadApi.image(file, 'avatars');
      setAvatarUrl(url);
    } catch (err) {
      alert(err instanceof HttpError ? err.message : 'Ошибка загрузки');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="page narrow">
      <h1>Редактирование профиля</h1>
      <form onSubmit={onSubmit} className="form card">
        <label>
          Имя пользователя
          <input value={username} onChange={(e) => setUsername(e.target.value)} required />
        </label>
        <label>
          Новый email (оставьте пустым, если не меняете)
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </label>
        <label>
          Новый пароль (оставьте пустым, если не меняете)
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            minLength={8}
          />
        </label>
        <label>
          URL аватара
          <input value={avatarUrl} onChange={(e) => setAvatarUrl(e.target.value)} />
        </label>
        <label>
          Загрузить аватар
          <input
            type="file"
            accept="image/*"
            disabled={uploading}
            onChange={(e) => onAvatarUpload(e.target.files?.[0] ?? null)}
          />
        </label>
        <fieldset>
          <legend>Предпочтения</legend>
          <TagPicker value={preferences} onChange={setPreferences} />
        </fieldset>
        {error && <p className="form-error">{error}</p>}
        {success && <p className="form-success">{success}</p>}
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          Сохранить
        </button>
      </form>
      <div className="danger-zone card">
        <h2>Опасная зона</h2>
        <p className="muted">Удаление аккаунта необратимо.</p>
        <button type="button" className="btn btn-danger" onClick={onDelete}>
          Удалить аккаунт
        </button>
      </div>
    </div>
  );
}
