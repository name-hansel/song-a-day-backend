package com.hanselname.songaday.song.utils;

public class SongUtils {
    public static boolean isSongMemoryInvalid(String memory) {
        return memory != null && memory.length() > 200;
    }

    private SongUtils() {

    }
}
