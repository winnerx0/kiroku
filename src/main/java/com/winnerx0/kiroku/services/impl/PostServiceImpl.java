package com.winnerx0.kiroku.services.impl;

import com.winnerx0.kiroku.config.CloudinaryConfig;
import com.winnerx0.kiroku.dto.PostCreateRequestDTO;
import com.winnerx0.kiroku.dto.PostUpdateRequestDTO;
import com.winnerx0.kiroku.exceptions.NoDataFoundException;
import com.winnerx0.kiroku.models.Post;
import com.winnerx0.kiroku.models.User;
import com.winnerx0.kiroku.repositories.PostRepository;
import com.winnerx0.kiroku.services.CustomUserDetailsServiceImpl;
import com.winnerx0.kiroku.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final CloudinaryConfig cloudinaryConfig;

    public PostServiceImpl(PostRepository postRepository, CustomUserDetailsServiceImpl userDetailsService, CloudinaryConfig cloudinaryConfig){
        this.postRepository = postRepository;
        this.userDetailsService = userDetailsService;
        this.cloudinaryConfig = cloudinaryConfig;
    }

    @Override
    public List<Post> getPosts(){
        User user = userDetailsService.getCurrentUser();
        return postRepository.findAllUserPosts(user.getId());
    }

    @Override
    public Post createPost(PostCreateRequestDTO postDTO) throws IOException {

        String url =  cloudinaryConfig.saveImage(postDTO.file());

        Post post = new Post();
        post.setTitle(postDTO.title());
        post.setImage(url);
        post.setStatus(postDTO.status());
        post.setUser(userDetailsService.getCurrentUser());
        return postRepository.save(post);

    }

    @Override
    public Post updatePost(PostUpdateRequestDTO postUpdateRequestDTO) throws IOException {
        Post post = postRepository.findById(postUpdateRequestDTO.id()).orElseThrow(() -> new EntityNotFoundException("Post With ID " + postUpdateRequestDTO.id() + " Not Found"));

        if(!postUpdateRequestDTO.file().isEmpty()){
            String url =  cloudinaryConfig.saveImage(postUpdateRequestDTO.file());
            post.setImage(url);
        }
        post.setTitle(postUpdateRequestDTO.title());
        post.setStatus(postUpdateRequestDTO.status());
        post.setUser(userDetailsService.getCurrentUser());

        return postRepository.save(post);
    }

    @Override
    public String deletePost(String postId) throws NoDataFoundException {
        if(postRepository.findById(postId).isEmpty()){
            throw new NoDataFoundException("Post with ID " + postId + " not found");
        }
        postRepository.deleteById(postId);
        return "Deleted Successfully";
    }
}
