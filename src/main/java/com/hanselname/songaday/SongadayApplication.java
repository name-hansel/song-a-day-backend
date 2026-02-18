package com.hanselname.songaday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SongadayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongadayApplication.class, args);
    }

}
