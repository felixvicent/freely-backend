package com.freely.backend.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.freely.backend.exceptions.BadRequestException;

@Service
public class UploadService {
  public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

  public String uploadImage(MultipartFile file, String folder) throws IOException {

    String filename = file.getOriginalFilename();

    if (filename == null) {
      throw new BadRequestException("Arquivo não enviado");
    }

    String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);
    StringBuilder fileNames = new StringBuilder();
    fileNames.append(UUID.randomUUID().toString()).append(".").append(fileExtension);

    Path filePath = Paths.get(UPLOAD_DIRECTORY, folder);

    if (!Files.isDirectory(filePath)) {
      Files.createDirectories(filePath);
    }

    Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, folder, fileNames.toString());
    Files.write(fileNameAndPath, file.getBytes());

    return fileNames.toString();

  }
}
