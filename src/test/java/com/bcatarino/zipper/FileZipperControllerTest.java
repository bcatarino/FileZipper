package com.bcatarino.zipper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.bcatarino.zipper.FileZipperController.FILES_ZIPPED_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FileZipperController.class)
public class FileZipperControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileZipperService fileZipperService;

    @Test
    public void shouldReturnBadRequestIfAllParametersMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(FILES_ZIPPED_URL))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfMissingFiles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(FILES_ZIPPED_URL)
                .param("saveWithName", "compressedFile.zip"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfFileNameMissing() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file1", "blahblah".getBytes());

        when(fileZipperService.createAndZipFile("compressedFile.zip", new MockMultipartFile[]{file})).thenReturn(Optional.empty());

        mockMvc.perform(multipart(FILES_ZIPPED_URL)
                .file("files", file.getBytes()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfZipFileNotCreated() throws Exception {
        String fileName = "compressedFile.zip";

        MockMultipartFile file = new MockMultipartFile("file1", "blahblah".getBytes());

        when(fileZipperService.createAndZipFile(fileName, new MockMultipartFile[]{file})).thenReturn(Optional.empty());

        mockMvc.perform(multipart(FILES_ZIPPED_URL)
                .file("files", file.getBytes())
                .param("saveWithName", fileName))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnZipForSingleFile() throws Exception {
        String fileName = "compressedFile.zip";

        MockMultipartFile file = new MockMultipartFile("file1.txt", "blahblah".getBytes());

        when(fileZipperService.createAndZipFile(eq(fileName), refEq(new MultipartFile[]{file})))
                .thenReturn(Optional.of(new ClassPathResource("singleFile.zip")));

        MvcResult mvcResult = mockMvc.perform(multipart(FILES_ZIPPED_URL)
                .file("files", file.getBytes())
                .param("saveWithName", fileName))
                .andExpect(status().isOk())
                .andReturn();

        byte[] responseAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(responseAsByteArray));

        ZipEntry singleEntry = zipInputStream.getNextEntry();
        assertThat(Objects.requireNonNull(singleEntry).getName()).isEqualTo("file1.txt");

        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    @Test
    public void shouldReturnZipForMultipleFiles() throws Exception {
        String fileName = "compressedFile.zip";

        MockMultipartFile file1 = new MockMultipartFile("file1.txt", "blahblah".getBytes());

        String anotherFileContent = "anotherfile\n" +
                "to test\n" +
                "that this\n" +
                "works";
        MockMultipartFile anotherFile = new MockMultipartFile("another.txt", anotherFileContent.getBytes());

        when(fileZipperService.createAndZipFile(eq(fileName), refEq(new MultipartFile[]{file1, anotherFile})))
                .thenReturn(Optional.of(new ClassPathResource("multipleFiles.zip")));

        MvcResult mvcResult = mockMvc.perform(multipart(FILES_ZIPPED_URL)
                .file("files", file1.getBytes())
                .file("files", anotherFile.getBytes())
                .param("saveWithName", fileName))
                .andExpect(status().isOk())
                .andReturn();

        byte[] responseAsByteArray = mvcResult.getResponse().getContentAsByteArray();

        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(responseAsByteArray));

        ZipEntry entry = zipInputStream.getNextEntry();
        assertThat(Objects.requireNonNull(entry).getName()).isEqualTo("file1.txt");

        zipInputStream.closeEntry();

        entry = zipInputStream.getNextEntry();
        assertThat(entry.getName()).isEqualTo("another.txt");

        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    String aaa = "anotherfile\n" +
            "to test\n" +
            "that this\n" +
            "works";
}
