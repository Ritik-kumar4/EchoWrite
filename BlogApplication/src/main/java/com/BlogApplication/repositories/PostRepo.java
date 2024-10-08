package com.BlogApplication.repositories;

import com.BlogApplication.entities.Category;
import com.BlogApplication.entities.Post;
import com.BlogApplication.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Integer> {
    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);
    List<Post> findByTitleContaining(String title);
}
