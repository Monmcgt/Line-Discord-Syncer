package me.monmcgt.code.linediscordsyncer.util;

import me.monmcgt.code.linediscordsyncer.api.GroupChatApi;
import me.monmcgt.code.linediscordsyncer.api.UserApi;
import me.monmcgt.code.linediscordsyncer.obj.TransferMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class DiscordEmbedUtil {
    public static Color COLOUR_1 = new Color(0x00ff00);

    public EmbedBuilder createTransferEmbed(TransferMessage transferMessage) {
        UserApi.SummaryResponse userSummary = transferMessage.getUserSummary();
        GroupChatApi.SummaryResponse groupSummary = transferMessage.getGroupSummary();
        String authorPictureUrl = userSummary.getPictureUrl();
        String displayName = userSummary.getDisplayName();
        String message = transferMessage.getMessage();
        String groupName = groupSummary.getGroupName();
        String groupPictureUrl = groupSummary.getPictureUrl();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message);
        embedBuilder.setColor(COLOUR_1);
        embedBuilder.setAuthor(displayName, null, authorPictureUrl);
        embedBuilder.setFooter(groupName, groupPictureUrl);
        embedBuilder.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        return embedBuilder;
    }
}
