import { Link, NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export function Layout() {
  const { isAuthenticated, user, logout } = useAuth();

  return (
    <div className="app-shell">
      <header className="app-header">
        <Link to="/" className="brand">
          <span className="brand-icon" aria-hidden>
            🌿
          </span>
          Книга рецептов
        </Link>
        <nav className="nav">
          <NavLink to="/recipes/search">Рецепты</NavLink>
          <NavLink to="/users/search">Люди</NavLink>
          {isAuthenticated && (
            <>
              <NavLink to="/recommendations">Подборка</NavLink>
              <NavLink to="/recipes/new">Создать</NavLink>
              <NavLink to="/favorites">Избранное</NavLink>
              <NavLink to="/profile">Профиль</NavLink>
            </>
          )}
        </nav>
        <div className="header-actions">
          {isAuthenticated ? (
            <>
              <span className="user-pill">{user?.username}</span>
              <button type="button" className="btn btn-ghost" onClick={logout}>
                Выйти
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn btn-ghost">
                Войти
              </Link>
              <Link to="/register" className="btn btn-primary">
                Регистрация
              </Link>
            </>
          )}
        </div>
      </header>
      <main className="app-main">
        <Outlet />
      </main>
      <footer className="app-footer">
        <span>Социальная сеть рецептов</span>
      </footer>
    </div>
  );
}
