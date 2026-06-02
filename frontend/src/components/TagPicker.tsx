import { PREFERENCE_TAGS, preferenceLabel } from '../constants/labels';
import type { PreferenceTag } from '../types';

interface TagPickerProps {
  value: PreferenceTag[];
  onChange: (tags: PreferenceTag[]) => void;
}

export function TagPicker({ value, onChange }: TagPickerProps) {
  const toggle = (tag: PreferenceTag) => {
    if (value.includes(tag)) {
      onChange(value.filter((t) => t !== tag));
    } else {
      onChange([...value, tag]);
    }
  };

  return (
    <div className="tag-picker">
      {PREFERENCE_TAGS.map((tag) => (
        <button
          key={tag}
          type="button"
          className={`tag tag-btn ${value.includes(tag) ? 'tag-active' : ''}`}
          onClick={() => toggle(tag)}
        >
          {preferenceLabel[tag]}
        </button>
      ))}
    </div>
  );
}
