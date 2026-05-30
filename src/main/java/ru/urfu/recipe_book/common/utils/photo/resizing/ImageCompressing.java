package ru.urfu.recipe_book.common.utils.photo.resizing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageCompressing {

    public File compressImage(MultipartFile file, int width, int height) throws IOException {

        Path tempPath = Files.createTempFile("compressed_", "." + getExtensionFromContentType(file.getContentType()));
        File tempFile = tempPath.toFile();

        Thumbnails.of(file.getInputStream())
                .size(width, height)
                .outputQuality(0.8)
                .outputFormat(getExtensionFromContentType(
                                file.getContentType()))
                .toFile(tempFile);

        return tempFile;
    }

    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return ".";
        }

        return switch (contentType) {
            case "image/jpg" -> "jpg";
            case "image/jpeg" -> "jpeg";
            case "image/png" -> "png";
            default -> "";
        };
    }
}
