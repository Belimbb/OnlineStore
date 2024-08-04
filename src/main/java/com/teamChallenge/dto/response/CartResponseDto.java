package com.teamChallenge.dto.response;

import java.util.Map;

public record CartResponseDto(String id, Map<String, Integer> figureIdAndAmountMap, int price) {
}
