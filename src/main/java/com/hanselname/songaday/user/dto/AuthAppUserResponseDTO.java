package com.hanselname.songaday.user.dto;

import java.util.UUID;

public record AuthAppUserResponseDTO(UUID uuid, String appUserName,
                                     String timezone,
                                     String formattedDateForToday) {
}