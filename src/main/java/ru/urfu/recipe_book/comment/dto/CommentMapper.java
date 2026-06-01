package ru.urfu.recipe_book.comment.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.urfu.recipe_book.comment.entity.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target= "author", ignore = true)
    @Mapping(target= "recipe", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CreateCommentDto createCommentDto);

    @Mapping(target = "authorId", source="author.id")
    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(target= "authorUsername", ignore = true)
    ResponseCommentDto toCommentResponse(Comment comment);

}
