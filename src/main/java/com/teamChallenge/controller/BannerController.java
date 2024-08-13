package com.teamChallenge.controller;

import com.teamChallenge.dto.request.BannerRequestDto;
import com.teamChallenge.dto.response.BannerResponseDto;
import com.teamChallenge.entity.banner.BannerService;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import com.teamChallenge.security.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/banners")
@Slf4j
@RequiredArgsConstructor
public class BannerController {

    public static final String URI_BANNER_WITH_ID = "/{id}";

    private final BannerService bannerService;
    private final AuthUtil authUtil;

    private final String secReq = "BearerAuth";

    @GetMapping
    @Operation(summary = "Get all banners")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of banners",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BannerResponseDto.class)))}
            )
    })
    public List<BannerResponseDto> getAll() {
        List<BannerResponseDto> bannerResponseDtoList = bannerService.getAll();
        log.info("{}: Banner list has been retrieved", LogEnum.CONTROLLER);
        return bannerResponseDtoList;
    }

    @GetMapping(URI_BANNER_WITH_ID)
    @Operation(description = "get a banner by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Banner found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BannerResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Banner not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public BannerResponseDto getById(@PathVariable String id) {
        BannerResponseDto bannerResponseDto = bannerService.getById(id);
        log.info("{}: Banner (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return bannerResponseDto;
    }

    @PostMapping
    @Operation(description = "create a banner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the banner",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BannerResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    @SecurityRequirement(name = secReq)
    public BannerResponseDto create(@RequestBody BannerRequestDto bannerRequestDto, Principal principal) throws UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);

        BannerResponseDto bannerResponseDto = bannerService.create(bannerRequestDto);
        log.info("{}: Banner (id: {}) has been added", LogEnum.CONTROLLER, bannerResponseDto.id());
        return bannerResponseDto;
    }

    @PutMapping(URI_BANNER_WITH_ID)
    @Operation(description = "update a banner by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the banner",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BannerResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Banner not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    @SecurityRequirement(name = secReq)
    public BannerResponseDto update(@PathVariable String id, @RequestBody BannerRequestDto bannerRequestDto, Principal principal) throws UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);

        BannerResponseDto bannerResponseDto = bannerService.update(id, bannerRequestDto);
        log.info("{}: Banner (id: {}) has been updated", LogEnum.CONTROLLER, id);
        return bannerResponseDto;
    }

    @DeleteMapping(URI_BANNER_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "delete a banner by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the banner",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BannerResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Banner not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public void delete(@PathVariable String id, Principal principal) throws UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);

        bannerService.delete(id);
        log.info("{}: Banner (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}
