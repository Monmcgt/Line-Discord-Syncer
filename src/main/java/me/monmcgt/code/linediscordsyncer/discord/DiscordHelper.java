package me.monmcgt.code.linediscordsyncer.discord;

import me.monmcgt.code.linediscordsyncer.obj.DiscordChannelId;
import me.monmcgt.code.linediscordsyncer.obj.TransferMessage;
import me.monmcgt.code.linediscordsyncer.util.DiscordEmbedUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.stereotype.Service;

@Service
public class DiscordHelper {
    private final DiscordService discordService;
    private final DiscordChannelId discordChannelId;

    private final DiscordEmbedUtil discordEmbedUtil;

    public DiscordHelper(DiscordService discordService, DiscordChannelId discordChannelId, DiscordEmbedUtil discordEmbedUtil) {
        this.discordService = discordService;
        this.discordChannelId = discordChannelId;
        this.discordEmbedUtil = discordEmbedUtil;
    }

    public void sendMessage(TransferMessage transferMessage) {
        this.sendMessage(transferMessage, this.discordChannelId.getDiscordChannelId());
    }

    public void sendMessage(TransferMessage transferMessage, long channelId) {
        JDA jda = this.getJda();
        TextChannel textChannel = jda.getTextChannelById(channelId);
        if (textChannel == null) {
            throw new RuntimeException("Could not find text channel with id " + channelId);
        }
        MessageEmbed embed = this.discordEmbedUtil.createTransferEmbed(transferMessage).build();
        textChannel.sendMessageEmbeds(embed).queue();
    }

    public JDA getJda() {
        return this.discordService.getJda();
    }

    public long getDiscordChannelId() {
        return this.discordChannelId.getDiscordChannelId();
    }
}
