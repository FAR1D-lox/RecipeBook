import { FormEvent, useState } from 'react';
import { Link } from 'react-router-dom';
import { usersApi } from '../api';
import { UserAvatar } from '../components/UserAvatar';
import { preferenceLabel } from '../constants/labels';
import { HttpError } from '../api/client';
import type { User } from '../types';

export function UserSearchPage() {
  const [query, setQuery] = useState('');
  const [users, setUsers] = useState<User[]>([]);
  const [cursor, setCursor] = useState<number | null>(null);
  const [hasNext, setHasNext] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const search = async (e?: FormEvent, nextCursor?: number | null) => {
    e?.preventDefault();
    if (!query.trim()) return;
    setLoading(true);
    setError('');
    try {
      const page = await usersApi.search(query.trim(), nextCursor ?? undefined);
      setUsers((prev) => (nextCursor != null ? [...prev, ...page.data] : page.data));
      setCursor(page.cursor);
      setHasNext(page.hasNext);
    } catch (err) {
      setError(err instanceof HttpError ? err.message : 'Ошибка поиска');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <h1>Поиск людей</h1>
      <form onSubmit={search} className="search-bar">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Имя пользователя…"
        />
        <button type="submit" className="btn btn-primary" disabled={loading}>
          Найти
        </button>
      </form>
      {error && <p className="form-error">{error}</p>}
      <ul className="user-list">
        {users.map((u) => (
          <li key={u.id}>
            <Link
              to={`/users/${u.id}`}
              state={{ user: u }}
              className="user-list-item card"
            >
              <UserAvatar username={u.username} avatarUrl={u.avatarUrl} />
              <div>
                <strong>{u.username}</strong>
                {u.preferences && u.preferences.length > 0 && (
                  <div className="tag-row">
                    {u.preferences.map((t) => (
                      <span key={t} className="tag">
                        {preferenceLabel[t]}
                      </span>
                    ))}
                  </div>
                )}
              </div>
            </Link>
          </li>
        ))}
      </ul>
      {hasNext && (
        <button
          type="button"
          className="btn btn-secondary"
          disabled={loading}
          onClick={() => search(undefined, cursor)}
        >
          Загрузить ещё
        </button>
      )}
    </div>
  );
}
