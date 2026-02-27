package com.hanselname.songaday.spotify.mapper;

import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.response_model.Image;
import com.hanselname.songaday.spotify.response_model.search.AlbumSearch;
import com.hanselname.songaday.spotify.response_model.search.ArtistSearch;
import com.hanselname.songaday.spotify.response_model.search.TrackSearch;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TrackSearchMapper {
    @Mapping(source = "id", target = "spotifyId")
    @Mapping(source = "name", target = "trackName")
    @Mapping(source = "album.name", target = "albumName")
    @Mapping(target = "artistName", expression = "java(getArtistName(track))")
    @Mapping(target = "imageUrl", expression = "java(getImageForSearch(track, needLargeImage))")
    @Mapping(target = "spotifyUrl", expression = "java(getSpotifyUrl(track))")
    TrackSearchDTO toDTO(TrackSearch track, @Context boolean needLargeImage);

    List<TrackSearchDTO> toDTOList(List<TrackSearch> tracks, @Context boolean needLargeImage);

    default String getArtistName(TrackSearch track) {
        return track.getArtists().stream().map(ArtistSearch::getName)
                    .collect(Collectors.joining(", "));
    }

    default String getImageForSearch(TrackSearch track, boolean needLargeImage) {
        AlbumSearch album = track.getAlbum();
        if (album == null || album.getImages().isEmpty()) {
            return null;
        }

        List<Image> images = album.getImages();

        return needLargeImage ? images.getFirst().getUrl() : images.getLast()
                                                                   .getUrl();
    }

    default String getSpotifyUrl(TrackSearch track) {
        return track.getExternalUrls().getSpotify();
    }
}
