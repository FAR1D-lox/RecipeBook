import type { ApiError } from '../types';
import { getToken } from '../utils/storage';

function apiBase(): string {
  const url = import.meta.env.VITE_API_URL?.trim();
  if (url) return url.replace(/\/$/, '');
  return '/api';
}

export class HttpError extends Error {
  status: number;

  constructor(status: number, message: string) {
    super(message);
    this.status = status;
    this.name = 'HttpError';
  }
}

async function parseError(res: Response): Promise<string> {
  try {
    const body = (await res.json()) as ApiError;
    if (body?.message) return body.message;
  } catch {
    /* ignore */
  }
  return res.statusText || 'Ошибка запроса';
}

export async function apiFetch<T>(
  path: string,
  options: RequestInit & { auth?: boolean } = {},
): Promise<T> {
  const { auth = true, headers: initHeaders, ...rest } = options;
  const headers = new Headers(initHeaders);

  if (!headers.has('Content-Type') && rest.body && !(rest.body instanceof FormData)) {
    headers.set('Content-Type', 'application/json');
  }

  if (auth) {
    const token = getToken();
    if (token) headers.set('Authorization', `Bearer ${token}`);
  }

  const res = await fetch(`${apiBase()}${path}`, { ...rest, headers });

  if (res.status === 204) return undefined as T;

  if (!res.ok) {
    throw new HttpError(res.status, await parseError(res));
  }

  const contentType = res.headers.get('Content-Type') ?? '';
  if (contentType.includes('application/json')) {
    return (await res.json()) as T;
  }

  const text = await res.text();
  return text as T;
}

export async function apiFetchText(
  path: string,
  options: RequestInit & { auth?: boolean } = {},
): Promise<string> {
  const { auth = true, headers: initHeaders, ...rest } = options;
  const headers = new Headers(initHeaders);

  if (!headers.has('Content-Type') && rest.body && !(rest.body instanceof FormData)) {
    headers.set('Content-Type', 'application/json');
  }

  if (auth) {
    const token = getToken();
    if (token) headers.set('Authorization', `Bearer ${token}`);
  }

  const res = await fetch(`${apiBase()}${path}`, { ...rest, headers });
  if (!res.ok) {
    throw new HttpError(res.status, await parseError(res));
  }

  let text = await res.text();
  if (text.startsWith('"') && text.endsWith('"')) {
    text = JSON.parse(text) as string;
  }
  return text;
}
