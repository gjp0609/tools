//package com.onysakura.tools;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//@SpringBootApplication
//@EnableScheduling
//public class Mains {
//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(Mains.class);
//        HttpClient client = HttpClient.newBuilder().build();
//        HttpResponse<String> response = client.send(HttpRequest.newBuilder().uri(URI.create("https://www.baidu.com")).GET().build(), HttpResponse.BodyHandlers.ofString());
//        System.out.println(response);
//    }
//
//}
