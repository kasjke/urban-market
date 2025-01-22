package com.example.urbanmarket.utils;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class DropboxUtils {
    private static String ACCESS_TOKEN;
    private static final String APP_KEY = System.getenv("DROPBOX_APP_KEY");
    private static final String APP_SECRET = System.getenv("DROPBOX_APP_SECRET");
    private static final String REFRESH_TOKEN = "v3NMnsMy_poAAAAAAAAAMrSA3dS2mzfUwLZVTfDNKk8";
    private static final String TOKEN_ENDPOINT = "https://api.dropbox.com/oauth2/token";
    private static final RestTemplate restTemplate = new RestTemplate();


    public static DbxClientV2 getClient() throws DbxException {
        ensureAccessToken();
        DbxRequestConfig config = DbxRequestConfig.newBuilder("UrbanMarket").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        client = validateClient(client, config);
        return client;
    }

    private static void ensureAccessToken() {
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            ACCESS_TOKEN = refreshAccessToken();
        }
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            throw new RuntimeException();
        }
    }

    private static DbxClientV2 validateClient(DbxClientV2 client, DbxRequestConfig config) {
        try {
            client.users().getCurrentAccount();
            return client;
        } catch (DbxException e) {
            if (e.getMessage().contains("expired_access_token")) {
                ACCESS_TOKEN = refreshAccessToken();
                return new DbxClientV2(config, ACCESS_TOKEN);
            } else {
                throw new RuntimeException();
            }
        }
    }

    private static String refreshAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = String.format("grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s",
                    REFRESH_TOKEN, APP_KEY, APP_SECRET);

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_ENDPOINT, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(response.getBody(), HashMap.class);
                return (String) map.get("access_token");
            } else {
                throw new RuntimeException("Failed to refresh access token. Response: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh access token.", e);
        }
    }
}
