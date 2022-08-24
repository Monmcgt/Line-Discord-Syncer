package me.monmcgt.code.linediscordsyncer.storage;

import lombok.Data;
import me.monmcgt.code.linediscordsyncer.api.UserApi;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserStorage {
    // TIMEOUT in milliseconds
    private static final long TIMEOUT = 5 * 60 * 1000;

    private final UserApi userApi;

    private final Set<User> users;

    public UserStorage(UserApi userApi) {
        this.userApi = userApi;
        this.users = new HashSet<>();
    }

    public UserApi.SummaryResponse getUserSummary(String userId) {
        this.loop();
        for (User user : this.users) {
            if (user.getSummary().getUserId().equals(userId)) {
                return user.getSummary();
            }
        }
        UserApi.SummaryResponse summary = this.userApi.summary(userId);
        this.users.add(new User(summary));
        return summary;
    }

    public String getUserDisplayName(String groupId) {
        UserApi.SummaryResponse groupSummary = this.getUserSummary(groupId);
        return groupSummary.getDisplayName();
    }

    public synchronized void loop() {
        Set<User> removeQueue = null;
        for (User user : this.users) {
            if (user.isExpired()) {
                if (removeQueue == null) {
                    removeQueue = new HashSet<>();
                }
                removeQueue.add(user);
            }
        }
        if (removeQueue != null) {
            this.users.removeAll(removeQueue);
        }
    }

    @Data
    private static class User {
        private long timeQueried;
        private UserApi.SummaryResponse summary;

        public User(UserApi.SummaryResponse summary) {
            this.summary = summary;
            this.timeQueried = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - this.timeQueried > TIMEOUT;
        }
    }
}
