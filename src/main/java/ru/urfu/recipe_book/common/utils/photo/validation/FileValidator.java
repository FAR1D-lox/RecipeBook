package ru.urfu.recipe_book.common.utils.photo.validation;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileValidator {

    private final Tika tika = new Tika();

    public void validateFileContent(MultipartFile file) throws IOException {

        String detectedType = tika.detect(file.getInputStream());

        List<String> allowedTypes = List.of(
            "image/jpeg", "image/png", "image/jpg");

        if (!allowedTypes.contains(detectedType)) {
            throw new IllegalArgumentException(
                "File content type " + detectedType + " is not allowed. You can only upload images with extension jpg, png"
            );
        }
    }
}
