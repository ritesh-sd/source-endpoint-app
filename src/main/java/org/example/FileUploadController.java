package org.example;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Map<String, Object>> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(filePart -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "File uploaded successfully");
                    response.put("filename", filePart.filename());
                    response.put("contentType", filePart.headers().getContentType());

                    // Here you can add file processing logic
                    // For now, just returning success response

                    return Mono.just(response);
                })
                .onErrorResume(error -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("status", "error");
                    errorResponse.put("message", "File upload failed: " + error.getMessage());
                    return Mono.just(errorResponse);
                });
    }
}
