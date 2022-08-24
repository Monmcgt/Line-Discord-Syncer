package me.monmcgt.code.linediscordsyncer.discord;

import me.monmcgt.code.linediscordsyncer.obj.DiscordToken;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class DiscordService {
    private final DiscordToken discordToken;

    private JDA jda;

    public DiscordService(DiscordToken discordToken) {
        this.discordToken = discordToken;
        this.main();
    }

    public void main() {
        try {
            this.jda = JDABuilder
                    .createDefault(this.discordToken.getDiscordToken())
                    .build()
                    .awaitReady();
        } catch (InterruptedException | LoginException e) {
            throw new RuntimeException(e);
        }
    }

    public JDA getJda() {
        return this.jda;
    }
}
