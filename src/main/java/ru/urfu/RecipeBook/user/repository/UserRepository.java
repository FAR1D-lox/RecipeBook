package ru.urfu.RecipeBook.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.RecipeBook.user.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

}
