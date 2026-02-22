package com.hanselname.songaday.common;

import org.mapstruct.Context;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommonUtils {
    public static final String API_PREFIX = "/api";
    public static final String USER_API_PREFIX = API_PREFIX + "/app-user";
    public static final String SONG_API_PREFIX = API_PREFIX + "/song-a-day";
    public static final String AUTH_API_PREFIX = API_PREFIX + "/auth";
    public static final String SPOTIFY_BASE_URL = "https://api.spotify.com/v1";
    public static final String SPOTIFY_ACCOUNTS_TOKEN_URL = "https://accounts.spotify.com/api/token";
    public static final String DEFAULT_TIME_ZONE_ID = "Etc/Greenwich";

    private static final DateTimeFormatter CURRENT_DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "EEEE, dd MMMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter SONG_ADDED_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "hh:mm a", Locale.ENGLISH);


    public static String formatDateForAppUser(LocalDate appUserToday) {
        return appUserToday.format(CURRENT_DATE_FORMATTER);
    }

    @Named("getFormattedTimeForSong")
    public static String getFormattedTimeForSong(Instant creationInstant, @Context ZoneId zoneId) {
        return creationInstant.atZone(zoneId).format(SONG_ADDED_TIME_FORMATTER);
    }

    private CommonUtils() {
        // No instantiation
    }
}
