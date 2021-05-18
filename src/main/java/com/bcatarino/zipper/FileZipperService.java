package com.bcatarino.zipper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileZipperService {
    static final String TEMP_FILES_FOLDER = System.getProperty("user.home") + File.separator;

    public Optional<Resource> createAndZipFile(String filenameToSave, MultipartFile[] files) {
        String localFileName = TEMP_FILES_FOLDER + filenameToSave;

        try {
            zipFile(Arrays.asList(files), new FileOutputStream(localFileName));

            return Optional.of(new UrlResource(Paths.get(localFileName).toUri()));
        } catch (MalformedURLException e) {
            log.error("Unable to create url for path " + localFileName, e);
        } catch (IOException e) {
            log.error("Unable to compress file from original files", e);
        }

        return Optional.empty();
    }

    private void zipFile(List<MultipartFile> multipartFiles, OutputStream outputStream) throws IOException {
        ZipOutputStream zipStream = new ZipOutputStream(outputStream);

        for (MultipartFile multipartFile : multipartFiles) {
            createZipEntry(zipStream, multipartFile);
        }

        zipStream.close();
    }

    private void createZipEntry(ZipOutputStream zipStream, MultipartFile multipartFile) throws IOException {
        zipStream.putNextEntry(new ZipEntry(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        byte[] fileContent = multipartFile.getBytes();
        zipStream.write(fileContent, 0, fileContent.length);
        zipStream.closeEntry();
    }
}
