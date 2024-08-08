package com.teamChallenge.controller;

import com.teamChallenge.dto.request.AdsRequestDto;
import com.teamChallenge.dto.response.AdsResponseDto;
import com.teamChallenge.entity.advertisement.AdvertisementService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ads")
public class AdsController {

    private final AdvertisementService adsService;

    public static final String URI_ADS_WITH_ID = "/{id}";

    @PostMapping
    @Operation(summary = "Add new Ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Ads",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdsResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = "BearerAuth")
    public AdsResponseDto addAds(@Valid @RequestBody AdsRequestDto request) {
        AdsResponseDto ads = adsService.create(request);
        log.info("{}: Figure (id: {}) has been added", LogEnum.SERVICE, ads.id());
        return ads;
    }

    @GetMapping
    @Operation(summary = "Get all ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of ads",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AdsResponseDto.class)))}
            )
    })
    public List<AdsResponseDto> adsList() {
        List<AdsResponseDto> adsDtoList = adsService.getAll();
        log.info("{}: Ads list have been retrieved", LogEnum.CONTROLLER);
        return adsDtoList;
    }

    @GetMapping(URI_ADS_WITH_ID)
    @Operation(summary = "Get ads by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got ads",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdsResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Ads not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    public AdsResponseDto getAdsById(@PathVariable String id) {
        AdsResponseDto ads = adsService.getById(id);
        log.info("{}: Ads (id: {}) has been retrieved", LogEnum.SERVICE, ads.id());
        return ads;
    }

    @DeleteMapping(URI_ADS_WITH_ID)
    @Operation(summary = "Delete ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ads deleted"),
            @ApiResponse(responseCode = "404", description = "Ads not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @SecurityRequirement(name = "BearerAuth")
    public void deleteAdsById(@PathVariable String id) {
        adsService.delete(id);
        log.info("{}: Ads (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}
