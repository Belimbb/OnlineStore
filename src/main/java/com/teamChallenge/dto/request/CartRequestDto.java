package com.teamChallenge.dto.request;

import java.util.Map;

public record CartRequestDto(Map<String, Integer> figureIdAndAmountMap) {
}
