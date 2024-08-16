package com.teamChallenge.controller;

import com.teamChallenge.images.ImageUploadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@Slf4j
@RequiredArgsConstructor
public class ImageUploadingController {

    private final ImageUploadingService imageUploadingService;

    public static final String SEC_REC = "BearerAuth";

    @PostMapping
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Upload image")
    @RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "object", properties = {
            @StringToClassMapItem(key = "imageFile", value = MultipartFile.class)
    })))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Received filename of the uploaded image")
    })
    public String upload(@NotNull @RequestParam MultipartFile imageFile) {
        return imageUploadingService.upload(imageFile);
    }
}
