package com.teamChallenge.controller;

import com.teamChallenge.dto.request.AdsRequestDto;
import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.AdsResponseDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.advertisement.AdvertisementServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import com.teamChallenge.security.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ads")
public class AdsController {
    private static final String URI_WITH_ID = "/{id}";
    private static final String SEC_REC = "BearerAuth";

    private final AdvertisementServiceImpl adsService;
    private final AuthUtil authUtil;

    @PostMapping()
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add new Ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Ads",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdsResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public ResponseEntity<AdsResponseDto> create(@Valid @NotNull @RequestBody AdsRequestDto request, Principal principal) throws UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);

        AdsResponseDto ads = adsService.create(request);
        log.info("{}: Figure (id: {}) has been added", LogEnum.SERVICE, ads.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ads);
    }

    @GetMapping()
    @Operation(summary = "Get all ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of ads",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AdsResponseDto.class)))}
            )
    })
    public ResponseEntity<List<AdsResponseDto>> getAll() {
        List<AdsResponseDto> adsDtoList = adsService.getAll();

        log.info("{}: Ads list have been retrieved", LogEnum.CONTROLLER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adsDtoList);
    }

    @GetMapping(URI_WITH_ID)
    @Operation(summary = "Get ads by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ads received",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdsResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Ads not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    public ResponseEntity<AdsResponseDto> getById(@NotBlank @NotNull @PathVariable String id) {
        AdsResponseDto ads = adsService.getById(id);
        log.info("{}: Ads (id: {}) has been retrieved", LogEnum.SERVICE, ads.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ads);
    }

    @PutMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Update ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ads updated"),
            @ApiResponse(responseCode = "404", description = "Ads not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public AdsResponseDto update(@PathVariable String id, @NotNull @RequestBody AdsRequestDto adsDto, Principal principal) throws CustomNotFoundException, UnauthorizedAccessException{
        authUtil.validateAdminRole(principal);

        AdsResponseDto updated = adsService.update(id, adsDto);
        log.info("{}: Ads (id: {}) has been updated", LogEnum.CONTROLLER, id);

        return updated;
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ads deleted"),
            @ApiResponse(responseCode = "404", description = "Ads not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void delete(@PathVariable String id, Principal principal) throws UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);

        adsService.delete(id);
        log.info("{}: Ads (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}
