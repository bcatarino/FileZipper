package com.bcatarino.zipper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileZipperController {
    static final String FILES_ZIPPED_URL = "/files/zipped";

    private final FileZipperService fileZipperService;

    @PostMapping(FILES_ZIPPED_URL)
    public ResponseEntity<?> zipFiles(@RequestParam(name = "saveWithName") String filenameToSave,
                                      @RequestParam("files") MultipartFile[] files) {
        return fileZipperService.createAndZipFile(filenameToSave, files)
                .map(resource -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource))
                .orElse(ResponseEntity.notFound().build());
    }
}
