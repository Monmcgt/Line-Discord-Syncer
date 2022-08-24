package me.monmcgt.code.linediscordsyncer.api;

import com.google.gson.Gson;
import lombok.Data;
import lombok.SneakyThrows;
import me.monmcgt.code.linediscordsyncer.obj.ChannelAccessToken;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class UserApi {
    private final Gson gson;

    private final ChannelAccessToken channelAccessToken;

    public UserApi(Gson gson, ChannelAccessToken channelAccessToken) {
        this.gson = gson;
        this.channelAccessToken = channelAccessToken;
    }

    @SneakyThrows
    public SummaryResponse summary(String userId) {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL("https://api.line.me/v2/bot/profile/" + userId).openConnection();
        httpsURLConnection.setRequestMethod("GET");
        httpsURLConnection.setRequestProperty("Authorization", "Bearer " + this.channelAccessToken.getChannelAccessToken());
        httpsURLConnection.connect();
        if (httpsURLConnection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to get summary of user " + userId + ": " + httpsURLConnection.getResponseCode());
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        if (stringBuilder.length() == 0) {
            throw new RuntimeException("Failed to get summary of user " + userId + ": empty response");
        }
        return this.gson.fromJson(stringBuilder.toString(), SummaryResponse.class);
    }

    @Data
    public static class SummaryResponse {
        private String displayName;
        private String userId;
        private String language;
        private String pictureUrl;
        private String statusMessage;
    }
}
