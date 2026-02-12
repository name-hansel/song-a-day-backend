package com.hanselname.songaday.spotify.mapper;

import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.response_model.Artist;
import com.hanselname.songaday.spotify.response_model.Track;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TrackSearchMapper {
    @Mapping(source = "name", target = "trackName")
    @Mapping(source = "album.name", target = "albumName")
    @Mapping(target = "artistName", expression = "java(getArtistName(track))")
    TrackSearchDTO toDTO(Track track);

    default String getArtistName(Track track) {
        return track.getArtists().stream().map(Artist::getName).collect(Collectors.joining(", "));
    }
}
