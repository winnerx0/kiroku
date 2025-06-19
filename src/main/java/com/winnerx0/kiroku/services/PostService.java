package com.winnerx0.kiroku.services;

import com.cloudinary.utils.ObjectUtils;
import com.winnerx0.kiroku.config.CloudinaryConfig;
import com.winnerx0.kiroku.dto.PostCreateRequestDTO;
import com.winnerx0.kiroku.dto.PostUpdateRequestDTO;
import com.winnerx0.kiroku.exceptions.NoDataFoundException;
import com.winnerx0.kiroku.models.Post;
import com.winnerx0.kiroku.models.User;
import com.winnerx0.kiroku.repositories.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {

  List<Post> getPosts();

  Post createPost(PostCreateRequestDTO postCreateRequestDTO) throws IOException;

  Post updatePost(PostUpdateRequestDTO postUpdateRequestDTO) throws IOException;

  String deletePost(String postId) throws NoDataFoundException;

}
