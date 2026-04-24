package ru.urfu.recipe_book.common.utils.minio;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class MinioController {

    private final MinioService minioService;
    @PostMapping("/upload")
    public String uploadObject(@RequestParam MultipartFile file, String bucketName, int compressedImageWidth, int compressedImageHeight) throws Exception {
        return minioService.putObject(bucketName, file, compressedImageWidth, compressedImageHeight);
    }
}
