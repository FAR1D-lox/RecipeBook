import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { TagPicker } from '../components/TagPicker';
import { useAuth } from '../context/AuthContext';
import { HttpError } from '../api/client';
import type { PreferenceTag } from '../types';

export function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [preferences, setPreferences] = useState<PreferenceTag[]>([]);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await register({
        username: username.trim(),
        email: email.trim(),
        password,
        preferences: preferences.length ? preferences : undefined,
      });
      navigate('/profile', { replace: true });
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Не удалось зарегистрироваться');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="card auth-card auth-card-wide">
        <h1>Регистрация</h1>
        <form onSubmit={onSubmit} className="form">
          <label>
            Имя пользователя
            <input
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              minLength={3}
              maxLength={50}
            />
          </label>
          <label>
            Email
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </label>
          <label>
            Пароль
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={8}
            />
          </label>
          <fieldset>
            <legend>Предпочтения (необязательно)</legend>
            <TagPicker value={preferences} onChange={setPreferences} />
          </fieldset>
          {error && <p className="form-error">{error}</p>}
          <button type="submit" className="btn btn-primary btn-block" disabled={submitting}>
            {submitting ? 'Создание…' : 'Создать аккаунт'}
          </button>
        </form>
        <p className="auth-switch">
          Уже есть аккаунт? <Link to="/login">Войти</Link>
        </p>
      </div>
    </div>
  );
}
