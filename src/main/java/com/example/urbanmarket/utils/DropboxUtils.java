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
    private static String ACCESS_TOKEN = "sl.u.AFdYSUUA8FjVcFAFZUq4oxtIgPyyycn4fva7JHzz0IdYe8VzJJqG9HwWR7uWm8eW-IeXMNOG6-29VoqUujVVqkOO1SZw1YdVMZKftzqRRVjXEuek7fVIt6ozYzW1i6ZuGb0ZviKx6pEzjmYyi-O08ZT1eaN5HTYpxXS8mi6LCbEHCWia99ccCV4gfc8Bj6DqWYsanwA1URTVqM8XxPuBv7_PFTa70gntsLWgh9IEZuPBKcaxLTd_t2BbzDeI9-2c0R4-5frFf7qQGFafHB0Gg0g4vQyEIUHpb6poCRDWkdEx62MVliJp7jJpx66UqdbOdiglbGcoVAgXJTkd87FRwEuRl0Ak90loT2Y2dSgDWvjemnCFRsuNJ9XQg5_rpNNxP5vSich1bg6664iD4RH03x1C4gfIdRhIxXBopAHI3laNlTTTGCONPkyCfYp1teAHNcaN0MUNpyfVC7r6TcuM3IT3zhHO8RXcqdu0hPSZ6zHzr8RLXeOmdOuZn-n1VW1tcxxVKBDnO9VLGh_7QwfBSOVxCqBnpuCgRtfenznRSPBi70XL9_GgB9LMUMTrGQ8KG9PcPXZKBOKlAHcDzoD4zFPGt8DGi16WbhK1n1oTKMCEBFSlELGJpJe0YOlBHBEZjKbVGSKdfxkn6FGpz3M-IZr4MQfdI3iwtrdeCgw3CuXghD8szKSj5DsBP-KPk5cvvu153hBheFWkJiW5E_MZs9T7ytf3vZfVA5dgqK1U9T7gPQsPxdstmJFc3ALCOffJnsLHGeYQs7XGD4Tl42jHWVvO0eYfw7vn4l8lV85LiyDLmqMdvrWJabZqINCU2CDlQe0ynmatz0LkrhFuSD-BR4xqtjYcTL17neYtBuJ3qKN3Kj_02LMNCalYLpvXtW0FjHWK0U2wTfjadh5mSdFj_y28GMMy0bpapUk7cVtbAizNvVEA7RhRairoR4HSm-gsjUmckFg2Q6MiPpp9ol09mDHS9-t8q8gFdc_Y4AW3IB16RkND7NPzsrLGS2Jcf87WE8sJsDqLY29yoM3XlN9aK320nO_7zZaPEsmwT95LUyylT4FPv_zuJEQ0DPQWG3gyUekyiW7q8HvdZqDV28fdu_3DAM3vSaWib7Us8EyZiIhP7ED-mKPszAwCrFh1oY22KwAyIJnx9W09jezF-EScJQ3GsTgb6vSUGS3qiXnmLtBi46ozCjbZgF1KvYWbe7K0Ah221m0vckjllm7DVGLK2Z986FzP7FEK49aSLkeIIa3EbNtD6U2CSnUNxoWrYp597G0KpB2R-1BXRbXAv2Kgs529DfDOiTMG6Bb5y4tzJ7PRfOBDFxNJZvtibCgBx79DUaGWtDzlaZSMb199fKt1vdyGVKACwU7kcTYSPIz_rHPGZQIq2YFWAVHvmTL5uWno5xI97WJB6OMCg_UR1cGfN6pdvNT2PlE0QnTUdn1Ec-vIxA";
    private static final String APP_KEY = "ocrm3yluvriipti";
    private static final String APP_SECRET = "pux9vip6b0tu9fm";
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
