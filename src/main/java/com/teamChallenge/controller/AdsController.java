package com.teamChallenge.controller;

import com.teamChallenge.entity.advertisement.AdvertisementDto;
import com.teamChallenge.entity.advertisement.AdvertisementServiceImpl;
import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import com.teamChallenge.request.AdsRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ads")
public class AdsController {
    private final AdvertisementServiceImpl adsService;
    private final UserServiceImpl userService;

    @PostMapping("/add")
    @Operation(summary = "Add new Ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Ads",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<AdvertisementDto> addAds(@Valid @NotNull @RequestBody AdsRequest request, Principal principal) throws UnauthorizedAccessException {
        if (!userService.getByEmail(principal.getName()).role().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }

        AdvertisementDto ads = adsService.createAds(request.text(), request.url());
        log.info("{}: Figure (id: {}) has been added", LogEnum.SERVICE, ads.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ads);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of ads",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AdvertisementDto.class)))}
            )
    })
    public ResponseEntity<List<AdvertisementDto>> adsList() {
        List<AdvertisementDto> adsDtoList = adsService.getAll();

        log.info("{}: Ads list have been retrieved", LogEnum.CONTROLLER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adsDtoList);
    }

    @GetMapping("/{adsId}")
    @Operation(summary = "Get ads by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got ads",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Ads not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    public ResponseEntity<AdvertisementDto> getAdsById(@NotBlank @NotNull @PathVariable("adsId") String adsId) {
        AdvertisementDto ads = adsService.getById(adsId);
        log.info("{}: Ads (id: {}) has been retrieved", LogEnum.SERVICE, ads.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ads);
    }

    @DeleteMapping("/{adsId}")
    @Operation(summary = "Delete ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ads deleted"),
            @ApiResponse(responseCode = "404", description = "Ads not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "BearerAuth")
    public void deleteAdsById(@PathVariable("adsId") String adsId, Principal principal) throws UnauthorizedAccessException {
        if (!userService.getByEmail(principal.getName()).role().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }

        adsService.deleteAds(adsId);
        log.info("{}: Ads (id: {}) has been deleted", LogEnum.CONTROLLER, adsId);
    }
}
