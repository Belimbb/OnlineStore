package com.teamChallenge.images;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadingService {

    String upload(MultipartFile multipartFile);
}
