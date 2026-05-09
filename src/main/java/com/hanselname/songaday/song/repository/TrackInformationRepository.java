package com.hanselname.songaday.song.repository;

import com.hanselname.songaday.song.entity.TrackInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackInformationRepository extends JpaRepository<TrackInformationEntity, String> {

}
