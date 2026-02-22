package com.hanselname.songaday.user;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.user.entity.AppUserEntity;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class AppUserUtils {

    @Named("getFormattedDateForAppUser")
    public static String getFormattedDateForAppUser(AppUserEntity appUserEntity) {
        return CommonUtils.formatDateForAppUser(
                getLocalDateForAppUser(appUserEntity));
    }

    public static LocalDate getLocalDateForAppUser(AppUserEntity appUserEntity) {
        return ZonedDateTime.now(ZoneId.of(appUserEntity.getTimezone()))
                            .toLocalDate();
    }

    private AppUserUtils() {
        // No instantiation
    }
}
