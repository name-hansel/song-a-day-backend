package com.hanselname.songaday.user.mapper;

import com.hanselname.songaday.user.dto.AuthAppUserResponseDTO;
import com.hanselname.songaday.user.entity.AppUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    @Mapping(source = "spotifyDisplayName", target = "appUserName")
    AuthAppUserResponseDTO toDTO(AppUserEntity appUserEntity);
}
