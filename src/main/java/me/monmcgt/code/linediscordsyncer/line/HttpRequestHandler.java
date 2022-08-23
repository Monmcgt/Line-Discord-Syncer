package me.monmcgt.code.linediscordsyncer.line;

import com.google.gson.Gson;
import me.monmcgt.code.linediscordsyncer.event.TextMessageEvent;
import me.monmcgt.code.linediscordsyncer.storage.GroupChatStorage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HttpRequestHandler {
    private final Gson gson;

    private final GroupChatStorage groupChatStorage;

    public HttpRequestHandler(Gson gson, GroupChatStorage groupChatStorage) {
        this.gson = gson;
        this.groupChatStorage = groupChatStorage;
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String json, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(200);
        TextMessageEvent textMessageEvent = this.gson.fromJson(json, TextMessageEvent.class);
        TextMessageEvent.Event[] events = textMessageEvent.getEvents();
        if (events.length > 0) {
            TextMessageEvent.Event event = events[0];
            TextMessageEvent.Event.Source source = event.getSource();
            String groupId = source.getGroupId();
            if (groupId != null) {
//                System.out.println("groupId: " + groupId);
                String groupName = this.groupChatStorage.getGroupName(groupId);
                if (groupName != null) {
                    String text = event.getMessage().getText();
                    System.out.println("----------------------------------------------------");
                    System.out.println("Group Name: " + groupName);
                    System.out.println("Message:\n" + text);
                }
            }
        }
    }
}
