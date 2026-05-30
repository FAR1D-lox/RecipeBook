package ru.urfu.recipe_book.common.utils.minio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class MinioController {

    private final MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadObject(@RequestParam MultipartFile file,
                                                 @RequestParam String bucketName,
                                                 @RequestParam int compressedImageWidth,
                                                 @RequestParam int compressedImageHeight) {
        String url = minioService.putObject(bucketName, file, compressedImageWidth, compressedImageHeight);
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
}
