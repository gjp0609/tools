package com.onysakura.tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetTest {

    public static void get(String url) {

        try {
            URI uri = URI.create(url);
            HttpResponse<String> response = HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(uri).build(), HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
