package ru.urfu.recipe_book.common.utils.minio;

import io.minio.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.urfu.recipe_book.common.utils.photo.resizing.ImageCompressing;
import ru.urfu.recipe_book.common.utils.photo.validation.FileValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Getter
@Setter
public class MinioService {
    private final MinioClient minioClient;
    private final ImageCompressing imageCompressing;
    private final FileValidator fileValidator;

    @Value("${minio.url}")
    private String minioUrl;

    public void bucketExists(String bucketName) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                .bucket(bucketName)
                .build());
        }
    }

    public String putObject(String bucketName, MultipartFile file, int compressedImageWidth, int compressedImageHeight) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file is empty");
        }

        fileValidator.validateFileContent(file);

        bucketExists(bucketName);

        File tempFile = null;

        String newFilename = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            try (InputStream originalStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object("original/" + newFilename)
                                .stream(originalStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            tempFile = imageCompressing.compressImage(file, compressedImageWidth, compressedImageHeight);


        try (FileInputStream compressedStream = new FileInputStream(tempFile)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("compressed/" + newFilename)
                            .stream(compressedStream, tempFile.length(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        }

        } finally {

            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        return minioUrl + bucketName + "/compressed/" + newFilename;
    }
}