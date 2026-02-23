package com.hanselname.songaday.user;

import com.hanselname.songaday.user.entity.AppUserEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AppUserUtils {

    public static LocalDate getLocalDateForAppUser(AppUserEntity appUserEntity) {
        return ZonedDateTime.now(ZoneId.of(appUserEntity.getTimezone()))
                            .toLocalDate();
    }

    private AppUserUtils() {
        // No instantiation
    }
}
