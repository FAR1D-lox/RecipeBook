package ru.urfu.RecipeBook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.RecipeBook.comment.entity.Comment;
import ru.urfu.RecipeBook.comment.service.CommentService;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void testAddComment() {

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@mail.com");
        user.setPassword("123");
        userRepository.save(user);

        Recipe recipe = new Recipe();
        recipe.setTitle("Test recipe");
        recipe.setDescription("Test description");
        recipe.setAuthor(user);
        recipeRepository.save(recipe);

        Comment comment = commentService.addComment(user.getId(), recipe.getId(), "Nice recipe");

        assertNotNull(comment.getId());
    }
}