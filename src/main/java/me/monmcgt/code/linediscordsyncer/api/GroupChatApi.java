package me.monmcgt.code.linediscordsyncer.api;

import com.google.gson.Gson;
import lombok.Data;
import lombok.SneakyThrows;
import me.monmcgt.code.linediscordsyncer.obj.ChannelAccessToken;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GroupChatApi {
    private final Gson gson;

    private final ChannelAccessToken channelAccessToken;

    public GroupChatApi(Gson gson, ChannelAccessToken channelAccessToken) {
        this.gson = gson;
        this.channelAccessToken = channelAccessToken;
    }

    @SneakyThrows
    public SummaryResponse summary(String groupId) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.line.me/v2/bot/group/" + groupId + "/summary").openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.channelAccessToken.getChannelAccessToken());
        httpURLConnection.connect();
        if (httpURLConnection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to get summary of group " + groupId + ": " + httpURLConnection.getResponseCode());
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        if (stringBuilder.length() == 0) {
            throw new RuntimeException("Failed to get summary of group " + groupId + ": empty response");
        }
        return this.gson.fromJson(stringBuilder.toString(), SummaryResponse.class);
    }

    @Data
    public static class SummaryResponse {
        private String groupId;
        private String groupName;
        private String pictureUrl;
    }
}
