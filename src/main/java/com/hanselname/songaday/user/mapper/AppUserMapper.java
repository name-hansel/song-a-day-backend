package com.hanselname.songaday.user.mapper;

import com.hanselname.songaday.user.AppUserUtils;
import com.hanselname.songaday.user.dto.AuthAppUserResponseDTO;
import com.hanselname.songaday.user.entity.AppUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AppUserUtils.class)
public interface AppUserMapper {
    @Mapping(source = "spotifyDisplayName", target = "appUserName")
    @Mapping(source = ".", target = "formattedDateForToday", qualifiedByName = "getFormattedDateForAppUser")
    AuthAppUserResponseDTO toDTO(AppUserEntity appUserEntity);
}
