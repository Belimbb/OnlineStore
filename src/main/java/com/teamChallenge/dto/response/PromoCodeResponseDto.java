package com.teamChallenge.dto.response;

import java.util.Date;

public record PromoCodeResponseDto(String id, String code, int discount, Date expirationDate) {
}
