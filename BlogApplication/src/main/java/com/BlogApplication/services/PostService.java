package com.BlogApplication.services;

import com.BlogApplication.entities.Post;
import com.BlogApplication.payloads.PostDto;
import com.BlogApplication.payloads.PostResponse;

import java.util.List;

public interface PostService {
    //create
    PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);

    //update
    PostDto updatePost(PostDto postDto,Integer postId);

    //delete
    void deletePost(Integer postId);

    //get All posts
    PostResponse getAllPost(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);

    //get single Post
    PostDto getPostById(Integer postId);

    //get all posts by category
    List<PostDto> getPostByCategory(Integer categoryId);

    //get all posts by user
    List<PostDto> getPostByUser(Integer userId);

    //search post
    List<PostDto> searchPosts(String keyword);
}
