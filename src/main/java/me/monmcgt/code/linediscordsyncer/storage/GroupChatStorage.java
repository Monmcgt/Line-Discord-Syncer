package me.monmcgt.code.linediscordsyncer.storage;

import lombok.Data;
import me.monmcgt.code.linediscordsyncer.api.GroupChatApi;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GroupChatStorage {
    // TIMEOUT in milliseconds
    private static final long TIMEOUT = 5 * 60 * 1000;

    private final GroupChatApi groupChatApi;

    private final Set<Group> groups;

    public GroupChatStorage(GroupChatApi groupChatApi) {
        this.groupChatApi = groupChatApi;
        this.groups = new HashSet<>();
    }

    public GroupChatApi.SummaryResponse getGroupSummary(String groupId) {
        this.loop();
        for (Group group : this.groups) {
            if (group.getSummary().getGroupId().equals(groupId)) {
                return group.getSummary();
            }
        }
        GroupChatApi.SummaryResponse summary = this.groupChatApi.summary(groupId);
        this.groups.add(new Group(summary));
        return summary;
    }

    public String getGroupName(String groupId) {
        GroupChatApi.SummaryResponse groupSummary = this.getGroupSummary(groupId);
        return groupSummary.getGroupName();
    }

    public synchronized void loop() {
        Set<Group> removeQueue = null;
        for (Group group : this.groups) {
            if (group.isExpired()) {
                if (removeQueue == null) {
                    removeQueue = new HashSet<>();
                }
                removeQueue.add(group);
            }
        }
        if (removeQueue != null) {
            this.groups.removeAll(removeQueue);
        }
    }

    @Data
    private static class Group {
        private long timeQueried;
        private GroupChatApi.SummaryResponse summary;

        public Group(GroupChatApi.SummaryResponse summary) {
            this.summary = summary;
            this.timeQueried = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - this.timeQueried > TIMEOUT;
        }
    }
}
