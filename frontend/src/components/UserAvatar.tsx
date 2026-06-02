interface UserAvatarProps {
  username: string;
  avatarUrl?: string | null;
  size?: 'sm' | 'md' | 'lg';
}

export function UserAvatar({ username, avatarUrl, size = 'md' }: UserAvatarProps) {
  const initial = username.charAt(0).toUpperCase();
  return (
    <div className={`avatar avatar-${size}`} title={username}>
      {avatarUrl ? (
        <img src={avatarUrl} alt={username} />
      ) : (
        <span>{initial}</span>
      )}
    </div>
  );
}
