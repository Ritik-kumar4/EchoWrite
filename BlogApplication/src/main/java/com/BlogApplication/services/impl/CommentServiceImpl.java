package com.BlogApplication.services.impl;

import com.BlogApplication.entities.Comment;
import com.BlogApplication.entities.Post;
import com.BlogApplication.entities.User;
import com.BlogApplication.exceptions.ResourceNotFoundException;
import com.BlogApplication.payloads.CommentDto;
import com.BlogApplication.repositories.CommentRepo;
import com.BlogApplication.repositories.PostRepo;
import com.BlogApplication.repositories.UserRepo;
import com.BlogApplication.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));

      Comment comment=  this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        Comment savedComment=this.commentRepo.save(comment);
        return this.modelMapper.map(savedComment,CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
      Comment com=  this.commentRepo.findById(commentId)
              .orElseThrow(()->new ResourceNotFoundException("Comment","comment id",commentId));
      this.commentRepo.delete(com);
    }
}
