package com.hanselname.songaday.song.mapper;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.ZoneId;

@Mapper(componentModel = "spring", uses = CommonUtils.class)
public interface SongMapper {
    @Mapping(target = "trackInformation", source = "track")
    @Mapping(target = "addedAtTime", source = "songEntity.creatingDate", qualifiedByName = "getFormattedTimeForSong")
    SongResponseDTO toDTO(SongEntity songEntity, TrackSearchDTO track, @Context ZoneId zoneId);
}
