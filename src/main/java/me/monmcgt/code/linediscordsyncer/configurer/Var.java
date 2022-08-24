package me.monmcgt.code.linediscordsyncer.configurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.monmcgt.code.linediscordsyncer.obj.ChannelAccessToken;
import me.monmcgt.code.linediscordsyncer.obj.DiscordChannelId;
import me.monmcgt.code.linediscordsyncer.obj.DiscordToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Var {
    private static final String CHANNEL_ACCESS_TOKEN = System.getenv("CHANNEL_ACCESS_TOKEN");

    private final Gson gson;

    public Var() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public Gson gson() {
        return this.gson;
    }

    @Bean
    public ChannelAccessToken channelAccessToken() {
        return new ChannelAccessToken(CHANNEL_ACCESS_TOKEN);
    }

    @Bean
    public DiscordToken discordToken() {
        return new DiscordToken(System.getenv("DISCORD_TOKEN"));
    }

    @Bean
    public DiscordChannelId discordChannelId() {
        return new DiscordChannelId(Long.parseLong(System.getenv("DISCORD_CHANNEL_ID")));
    }
}
