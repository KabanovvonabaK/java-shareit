package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {

    // Comment to trigger hook after changing repo visibility
    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);
    }

}
