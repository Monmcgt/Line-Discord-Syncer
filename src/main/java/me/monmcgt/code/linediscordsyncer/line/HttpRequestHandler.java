package me.monmcgt.code.linediscordsyncer.line;

import com.google.gson.Gson;
import me.monmcgt.code.linediscordsyncer.api.UserApi;
import me.monmcgt.code.linediscordsyncer.discord.DiscordHelper;
import me.monmcgt.code.linediscordsyncer.discord.DiscordService;
import me.monmcgt.code.linediscordsyncer.event.TextMessageEvent;
import me.monmcgt.code.linediscordsyncer.obj.TransferMessage;
import me.monmcgt.code.linediscordsyncer.storage.GroupChatStorage;
import me.monmcgt.code.linediscordsyncer.api.GroupChatApi;
import me.monmcgt.code.linediscordsyncer.storage.UserStorage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HttpRequestHandler {
    private final Gson gson;

    private final GroupChatStorage groupChatStorage;
    private final UserStorage userStorage;

    private final DiscordService discordService;
    private final DiscordHelper discordHelper;

    public HttpRequestHandler(Gson gson, GroupChatStorage groupChatStorage, UserStorage userStorage, DiscordService discordService, DiscordHelper discordHelper) {
        this.gson = gson;
        this.groupChatStorage = groupChatStorage;
        this.userStorage = userStorage;
        this.discordService = discordService;
        this.discordHelper = discordHelper;
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String json, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(200);
        TextMessageEvent textMessageEvent = this.gson.fromJson(json, TextMessageEvent.class);
        TextMessageEvent.Event[] events = textMessageEvent.getEvents();
        if (events.length > 0) {
            TextMessageEvent.Event event = events[0];
            TextMessageEvent.Event.Source source = event.getSource();
            String userId = source.getUserId();
            String groupId = source.getGroupId();
            if (groupId != null) {
//                System.out.println("groupId: " + groupId);
                GroupChatApi.SummaryResponse groupSummary = this.groupChatStorage.getGroupSummary(groupId);
                UserApi.SummaryResponse userSummary = this.userStorage.getUserSummary(userId);
                TransferMessage transferMessage = new TransferMessage(event, groupSummary, userSummary, event.getMessage().getText());
                this.discordHelper.sendMessage(transferMessage);
            }
        }
    }
}
