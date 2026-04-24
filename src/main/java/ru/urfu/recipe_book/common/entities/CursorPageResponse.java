package ru.urfu.recipe_book.common.entities;

import java.util.List;

public record CursorPageResponse<T> (
    List<T> data,
    int pageSize,
    Long cursor,
    boolean hasNext
) {}
