package ru.lazarenko.storemanagement.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import ru.lazarenko.storemanagement.entity.Product;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {

    @Value("${upload.path}")
    private static String uploadPath;

    public static void saveFile(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            product.setFilename(resultFileName);
        }
    }
}
