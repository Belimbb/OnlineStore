package com.teamChallenge.images;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomBadRequestException;
import com.teamChallenge.exception.exceptions.generalExceptions.SomethingWentWrongException;
import com.teamChallenge.exception.exceptions.imageExceptions.IncorrectFileExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class ImageUploadingServiceImpl implements ImageUploadingService {

    @Value("${firebase.properties.bucket_name}")
    private String bucketName;

    @Value("${firebase.properties.private_key_filename}")
    private String privateKeyFilename;

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            String filename = multipartFile.getOriginalFilename();
            filename = UUID.randomUUID().toString().concat(this.getExtension(filename));

            File file = this.convertToFile(multipartFile, filename);
            uploadFile(file, filename);
            file.delete();
            return filename;
        }   catch (IOException ex) {
            ex.printStackTrace();
            throw new SomethingWentWrongException();
        }
    }

    private File convertToFile(MultipartFile multipartFile, String filename) throws IOException {
        File tempFile = new File(filename);
        try(FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private void uploadFile(File file, String filename) throws IOException {
        BlobId blobId = BlobId.of(bucketName, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        InputStream inputStream = new FileInputStream("/etc/secrets/" + privateKeyFilename);
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }

    private String getExtension(String filename) {
        String extension = filename.substring(filename.lastIndexOf("."));

        if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")) {
            return extension;
        }
        throw new IncorrectFileExtension(extension);
    }
}
