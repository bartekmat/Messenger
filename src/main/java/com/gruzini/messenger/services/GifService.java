package com.gruzini.messenger.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class GifService {
    public boolean isGifMessage(final String content) {
        return content != null && content.startsWith("::");
    }
    public String prepareGifMessageText(final String searchText){
        Map<String, Map<String, String>> result = new RestTemplate().getForObject("https://api.giphy.com/v1/gifs/random?" +
                "api_key=4AWXpY3c6uLL8uH9dBWS8Xku4KFcQ2hE&" +
                "tag=" + searchText + "&" +
                "rating=g", Map.class);
        if (result == null) {
            return "GIF ERROR";
        }
        String mp4Link = result.get("data").get("image_mp4_url");
        return mp4Link;
    }
}
