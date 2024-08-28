package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

//TODO: оповещения об окончании аукциона/выигранном аукционе, сообщения (с оповещениями на телефон и эл. почту), реклама Google, (?страница о нас, контакты?), список желаемого
//TODO: оповещения о появлении желаемого товара, обмен (простая версия и версия как с OLX доставкой)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
