package com.example.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class Util {
    public static String saveUploadedThumbnail(HttpServletRequest req, MultipartFile multipartFile)
            throws IOException, ServletException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }
        String submittedName = multipartFile.getOriginalFilename();
        if (submittedName == null) {
            return null;
        }
        submittedName = Paths.get(submittedName).getFileName().toString();
        if (submittedName.trim().isEmpty()) {
            return null;
        }
        String extension = "";
        int dot = submittedName.lastIndexOf('.');
        if (dot != -1) {
            extension = submittedName.substring(dot);
        }
        String storedName = UUID.randomUUID() + extension;
        ServletContext context = req.getServletContext();
        String uploadDir = context.getRealPath("/uploads");
        if (uploadDir == null) {
            throw new ServletException("服务器无法解析 uploads 目录，请确保以 exploded 方式部署或手动配置上传目录");
        }
        Path uploadPath = Paths.get(uploadDir);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(storedName);
        try (InputStream input = multipartFile.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        syncSourceCopy(uploadDir, filePath, storedName);

        String url = req.getContextPath() + "/uploads/" + storedName;
        System.out.println("[Util] thumbnail URL = " + url);
        return url;
    }

    public static String saveUploadedThumbnail(HttpServletRequest req) throws IOException, ServletException {
        if (!isMultipart(req)) {
            return null;
        }
        Part part = req.getPart("thumbnailFile");
        if (part == null || part.getSize() == 0) {
            return null;
        }
        String submittedName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        if (submittedName.trim().isEmpty()) {
            return null;
        }
        String extension = "";
        int dot = submittedName.lastIndexOf('.');
        if (dot != -1) {
            extension = submittedName.substring(dot);
        }
        String storedName = UUID.randomUUID() + extension;
        ServletContext context = req.getServletContext();
        String uploadDir = context.getRealPath("/uploads");
        if (uploadDir == null) {
            throw new ServletException("服务器无法解析 uploads 目录，请确保以 exploded 方式部署或手动配置上传目录");
        }
        System.out.println("[Util] uploadDir = " + uploadDir);
        Path uploadPath = Paths.get(uploadDir);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(storedName);
        try (InputStream input = part.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // --- 新增代码：同时保存到源码目录，防止IDE重启/热部署时丢失 ---
        try {
            // 尝试推断源码路径 (假设标准Maven结构且在target目录下运行)
            // uploadDir 通常是 .../target/项目名/uploads
            // 我们需要找到 .../src/main/webapp/uploads
            String targetMarker = "target";
            int targetIndex = uploadDir.lastIndexOf(targetMarker);
            if (targetIndex > 0) {
                String projectRoot = uploadDir.substring(0, targetIndex);
                Path sourceUploadPath = Paths.get(projectRoot, "src", "main", "webapp", "uploads");

                // 如果源码目录存在，则复制一份过去
                if (Files.exists(sourceUploadPath)) {
                    Path sourceFile = sourceUploadPath.resolve(storedName);
                    Files.copy(filePath, sourceFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("[Util] 已同步保存图片到源码目录: " + sourceFile);
                }
            }
        } catch (Exception e) {
            System.err.println("[Util] 同步保存到源码目录失败 (非致命错误): " + e.getMessage());
        }
        // -----------------------------------------------------------

        String url = req.getContextPath() + "/uploads/" + storedName;
        System.out.println("[Util] contextPath = " + req.getContextPath());
        System.out.println("[Util] thumbnail URL = " + url);
        return url;
    }

    public static boolean isMultipart(HttpServletRequest req) {
        String contentType = req.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    public static boolean parseAllowComment(HttpServletRequest req) {
        String value = req.getParameter("allowComment");
        return parseAllowComment(value);
    }

    public static boolean parseAllowComment(String value) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return "1".equals(value) || "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
    }

    public static String resolveMarkdown(HttpServletRequest req) {
        return resolveMarkdown(req.getParameter("markdown"), req.getParameter("content"));
    }

    public static String resolveMarkdown(String markdown, String content) {
        if (markdown == null || markdown.trim().isEmpty()) {
            markdown = content;
        }
        return markdown;
    }

    private static void syncSourceCopy(String uploadDir, Path filePath, String storedName) {
        try {
            String targetMarker = "target";
            int targetIndex = uploadDir.lastIndexOf(targetMarker);
            if (targetIndex > 0) {
                String projectRoot = uploadDir.substring(0, targetIndex);
                Path sourceUploadPath = Paths.get(projectRoot, "src", "main", "webapp", "uploads");
                if (Files.exists(sourceUploadPath)) {
                    Path sourceFile = sourceUploadPath.resolve(storedName);
                    Files.copy(filePath, sourceFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("[Util] 已同步保存图片到源码目录: " + sourceFile);
                }
            }
        } catch (Exception e) {
            System.err.println("[Util] 同步保存到源码目录失败 (非致命错误): " + e.getMessage());
        }
    }
}
