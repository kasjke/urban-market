package com.example.urbanmarket.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class MultipartFileConverter {


    public MultipartFile base64ToMultipartFile(String base64) {
        if (base64 == null || base64.isEmpty()) {
            throw new IllegalArgumentException("Base64 string is null or empty");
        }

        try {
            String[] parts = base64.split(",");
            String metaInfo = parts.length > 1 ? parts[0] : "";
            byte[] fileBytes = Base64.getDecoder().decode(parts.length > 1 ? parts[1] : parts[0]);

            String contentType = metaInfo.contains(";") ? metaInfo.split(";")[0].split(":")[1] : "application/octet-stream";

            return new CustomMultipartFile(
                    fileBytes,
                    "file",
                    "image.png",
                    contentType
            );
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 input", e);
        }
    }
}
