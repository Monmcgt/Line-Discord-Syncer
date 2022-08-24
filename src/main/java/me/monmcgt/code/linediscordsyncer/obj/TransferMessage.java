package me.monmcgt.code.linediscordsyncer.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.monmcgt.code.linediscordsyncer.api.UserApi;
import me.monmcgt.code.linediscordsyncer.event.TextMessageEvent;
import me.monmcgt.code.linediscordsyncer.api.GroupChatApi;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferMessage {
    private TextMessageEvent.Event event;
    private GroupChatApi.SummaryResponse groupSummary;
    private UserApi.SummaryResponse userSummary;
    private String message;
}
