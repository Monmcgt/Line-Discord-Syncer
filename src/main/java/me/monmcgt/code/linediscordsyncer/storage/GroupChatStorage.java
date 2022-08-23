package me.monmcgt.code.linediscordsyncer.storage;

import lombok.Data;
import me.monmcgt.code.linediscordsyncer.util.GroupChatApiUtil;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GroupChatStorage {
    // TIMEOUT in milliseconds
    private static final long TIMEOUT = 15 * 60 * 1000;

    private final GroupChatApiUtil groupChatApiUtil;

    private final Set<Group> groups;

    public GroupChatStorage(GroupChatApiUtil groupChatApiUtil) {
        this.groupChatApiUtil = groupChatApiUtil;
        this.groups = new HashSet<>();
    }

    public GroupChatApiUtil.SummaryResponse getGroupSummary(String groupId) {
        this.loop();
        for (Group group : this.groups) {
            if (group.getSummary().getGroupId().equals(groupId)) {
                return group.getSummary();
            }
        }
        GroupChatApiUtil.SummaryResponse summary = this.groupChatApiUtil.summary(groupId);
        this.groups.add(new Group(summary));
        return summary;
    }

    public String getGroupName(String groupId) {
        GroupChatApiUtil.SummaryResponse groupSummary = this.getGroupSummary(groupId);
        return groupSummary.getGroupName();
    }

    public void loop() {
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
        private GroupChatApiUtil.SummaryResponse summary;

        public Group(GroupChatApiUtil.SummaryResponse summary) {
            this.summary = summary;
            this.timeQueried = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - this.timeQueried > TIMEOUT;
        }
    }
}
