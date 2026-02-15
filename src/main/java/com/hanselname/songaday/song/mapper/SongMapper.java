package com.hanselname.songaday.song.mapper;

import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongMapper {
    @Mapping(target = "trackInformation", source = "track")
    SongResponseDTO toDTO(SongEntity songEntity, TrackSearchDTO track);
}
