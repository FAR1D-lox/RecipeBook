import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import { authApi, resolveUserByUsername, usersApi } from '../api';
import type { CreateUserPayload, User } from '../types';
import { decodeJwtUsername } from '../utils/jwt';
import {
  clearAuth,
  getStoredUser,
  getToken,
  setStoredUser,
  setToken,
} from '../utils/storage';

interface AuthContextValue {
  user: User | null;
  isAuthenticated: boolean;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  register: (payload: CreateUserPayload) => Promise<void>;
  logout: () => void;
  refreshUser: () => Promise<void>;
  setUser: (user: User | null) => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(() => getStoredUser());
  const [loading, setLoading] = useState(true);

  const syncUserFromToken = useCallback(async () => {
    const token = getToken();
    if (!token) {
      setUser(null);
      return;
    }
    try {
      const profile = await usersApi.me();
      setStoredUser(profile);
      setUser(profile);
      return;
    } catch {
      /* fallback below */
    }
    const stored = getStoredUser();
    if (stored) {
      setUser(stored);
      return;
    }
    const username = decodeJwtUsername(token);
    if (!username) return;
    const found = await resolveUserByUsername(username);
    if (found) {
      setStoredUser(found);
      setUser(found);
    }
  }, []);

  useEffect(() => {
    syncUserFromToken()
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, [syncUserFromToken]);

  const login = useCallback(async (username: string, password: string) => {
    const token = await authApi.login(username, password);
    setToken(token);
    try {
      const profile = await usersApi.me();
      setStoredUser(profile);
      setUser(profile);
    } catch {
      const found = await resolveUserByUsername(username);
      if (found) {
        setStoredUser(found);
        setUser(found);
      } else {
        setStoredUser({ id: 0, username });
        setUser({ id: 0, username });
      }
    }
  }, []);

  const register = useCallback(async (payload: CreateUserPayload) => {
    const created = await authApi.register(payload);
    const token = await authApi.login(payload.username, payload.password);
    setToken(token);
    setStoredUser(created);
    setUser(created);
  }, []);

  const logout = useCallback(() => {
    clearAuth();
    setUser(null);
  }, []);

  const refreshUser = useCallback(async () => {
    await syncUserFromToken();
  }, [syncUserFromToken]);

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: Boolean(getToken()),
      loading,
      login,
      register,
      logout,
      refreshUser,
      setUser,
    }),
    [user, loading, login, register, logout, refreshUser],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
