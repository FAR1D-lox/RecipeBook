export function stripHtmlTags(input: string): string {
  return input.replace(/<[^>]*>/g, '');
}

export function normalizeNewlines(input: string): string {
  return input.replace(/\r\n/g, '\n');
}

