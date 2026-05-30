package ru.urfu.recipe_book.common.exception;

public record ErrorResponse(int status, String message) {
}
