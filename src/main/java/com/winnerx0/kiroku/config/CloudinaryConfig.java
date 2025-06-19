package com.winnerx0.kiroku.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryConfig {

    public Cloudinary cloudinaryClient() {
        Dotenv dotenv = Dotenv.load();
        String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");

        if (cloudinaryUrl == null) {
            throw new IllegalStateException("CLOUDINARY_URL environment variable is not set.");
        }
        return new Cloudinary(cloudinaryUrl);
    }

    public String saveImage(MultipartFile multipartFile) throws IOException {
        if(multipartFile.getBytes().length > 10485760){
            throw new IllegalArgumentException("File must be at most 10mb");
        }
        Map params1 = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );
        File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());

        multipartFile.transferTo(file);

        Object url = cloudinaryClient().uploader().upload(file, params1).get("secure_url");
        return (String) url;
    }
}
