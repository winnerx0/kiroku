package com.winnerx0.kiroku.services;

import com.cloudinary.utils.ObjectUtils;
import com.winnerx0.kiroku.config.CloudinaryConfig;
import com.winnerx0.kiroku.dto.PostDTO;
import com.winnerx0.kiroku.exceptions.NoDataFoundException;
import com.winnerx0.kiroku.models.Post;
import com.winnerx0.kiroku.models.User;
import com.winnerx0.kiroku.repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CustomUserDetailsService userDetailsService;
    private final CloudinaryConfig cloudinaryConfig;

    public PostService(PostRepository postRepository, CustomUserDetailsService userDetailsService, CloudinaryConfig cloudinaryConfig){
        this.postRepository = postRepository;
        this.userDetailsService = userDetailsService;
        this.cloudinaryConfig = cloudinaryConfig;
    }

    public List<Post> getPosts(){
        User user = userDetailsService.getCurrentUser();
        return postRepository.findAllUserPosts(user.getId());
    }

    public Post createPost(PostDTO postDTO) throws IOException, IllegalArgumentException {

        if(postDTO.file().getBytes().length > 10485760){
            throw new IllegalArgumentException("File must be at most 10mb");
        }
        Map params1 = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );
        File file = File.createTempFile("upload-", postDTO.file().getOriginalFilename());

        postDTO.file().transferTo(file);

        Object url = cloudinaryConfig.cloudinaryClient().uploader().upload(file, params1).get("secure_url");

        Post post = new Post(postDTO.title(), url.toString(), userDetailsService.getCurrentUser());
        return postRepository.save(post);

    }

    public String deletePost(String postId) throws NoDataFoundException {
        if(postRepository.findById(postId).isEmpty()){
            throw new NoDataFoundException("Post with ID " + postId + " not found");
        }
        postRepository.deleteById(postId);
        return "Deleted Successfully";
    }
}
