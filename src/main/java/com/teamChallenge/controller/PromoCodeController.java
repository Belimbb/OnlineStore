package com.teamChallenge.controller;

import com.teamChallenge.dto.request.PromoCodeRequestDto;
import com.teamChallenge.dto.response.PromoCodeResponseDto;
import com.teamChallenge.entity.promoCode.PromoCodeService;
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
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promo_codes")
public class PromoCodeController {

    private static final String URI_WITH_ID = "/{id}";
    private static final String SEC_REC = "BearerAuth";

    private final PromoCodeService promoCodeService;

    @PostMapping
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add new Promo Code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Promo Code",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PromoCodeResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public PromoCodeResponseDto create(@Valid @RequestBody PromoCodeRequestDto request) {
        PromoCodeResponseDto promoCode = promoCodeService.create(request);
        log.info("{}: Promo Code (id: {}) has been added", LogEnum.SERVICE, promoCode.id());
        return promoCode;
    }

    @GetMapping()
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Get all promo codes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of promo codes",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PromoCodeResponseDto.class)))}
            )
    })
    public List<PromoCodeResponseDto> getAll() {
        List<PromoCodeResponseDto> promoCodes = promoCodeService.getAll();
        log.info("{}: Promo Codes have been retrieved", LogEnum.CONTROLLER);
        return promoCodes;
    }

    @GetMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "get a promo code by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found a promo code",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PromoCodeResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Promo code not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public PromoCodeResponseDto getById(@PathVariable String id) {
        PromoCodeResponseDto promoCode = promoCodeService.getById(id);
        log.info("{}: Promo Code (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return promoCode;
    }

    @PutMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "update aa promo code by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promo code updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PromoCodeResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public PromoCodeResponseDto update(@PathVariable String id, @Valid @RequestBody PromoCodeRequestDto userRequestDto) {
        PromoCodeResponseDto promoCode = promoCodeService.update(id, userRequestDto);
        log.info("{}: Promo Code (id: {}) has been updated", LogEnum.CONTROLLER, promoCode.id());
        return promoCode;
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Delete promo code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promo Code deleted"),
            @ApiResponse(responseCode = "404", description = "Promo Code not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void delete(@PathVariable String id) {
        promoCodeService.delete(id);
        log.info("{}: Promo Code (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}
