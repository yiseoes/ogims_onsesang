// com.semi.util.FileUtil.java
package com.semi.common.util;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;
import javax.servlet.http.Part;
import javax.servlet.ServletContext;

public class FileUtil {

    // 이미지 저장 : 저장된 파일명 반환(예: 4c9f...-a1.png)
    public static String saveImage(Part part, ServletContext ctx) throws IOException {
        if (part == null || part.getSize() == 0) return null;

        // 원본 파일명에서 확장자만 추출(최소 처리)
        String submitted = getSubmittedFileName(part);
        String ext = "";
        if (submitted != null) {
            int dot = submitted.lastIndexOf('.');
            if (dot >= 0) ext = submitted.substring(dot); // ".png" 포함
        }

        String storedName = UUID.randomUUID().toString().replace("-", "") + ext;

        // /images 물리 경로
        String imagesPath = ctx.getRealPath("/images");
        Files.createDirectories(Paths.get(imagesPath));

        Path target = Paths.get(imagesPath, storedName);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return storedName;
    }

    public static void deleteImageIfExists(String storedName, ServletContext ctx) {
        if (storedName == null || storedName.isEmpty()) return;
        String imagesPath = ctx.getRealPath("/images");
        Path target = Paths.get(imagesPath, storedName);
        try {
            Files.deleteIfExists(target);
        } catch (IOException ignore) {
            // 최소 요구사항이므로 로깅만 하거나 무시
        }
    }

    // Servlet 3.0 Part에서 클라이언트 제출 파일명 얻기
    private static String getSubmittedFileName(Part part) {
        String cd = part.getHeader("content-disposition");
        if (cd == null) return null;
        for (String token : cd.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                String name = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                return name;
            }
        }
        return null;
    }
}
