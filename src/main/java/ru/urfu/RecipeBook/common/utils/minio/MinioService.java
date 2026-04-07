package ru.urfu.RecipeBook.common.utils.minio;

import io.minio.*;
import io.minio.errors.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Getter
@Setter
public class MinioService {
    private final MinioClient minioClient;

    public void bucketExists(String bucketName) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                .bucket(bucketName)
                .build());
        }
    }

    public String putObject(String bucketName, MultipartFile file) throws Exception {
        bucketExists(bucketName);

        String newFilename = UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newFilename)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build());
        return "http://localhost:9000/" + bucketName + "/" + newFilename;
    }
}