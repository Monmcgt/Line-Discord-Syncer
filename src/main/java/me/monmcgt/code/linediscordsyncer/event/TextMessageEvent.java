package me.monmcgt.code.linediscordsyncer.event;

import lombok.Data;

@Data
public class TextMessageEvent {
    private String destination;
    private Event[] events;

    @Data
    public static class Event {
        private String replyToken;
        private String type;
        private String mode;
        private long timestamp;
        private Source source;
        private String webhookEventId;
        private DeliveryContext deliveryContext;
        private Message message;

        @Data
        public static class Source {
            private String type;
            private String userId;
            private String groupId;
            private String roomId;
        }

        @Data
        public static class DeliveryContext {
            private boolean isRedelivery;
        }

        @Data
        public static class Message {
            private String id;
            private String type;
            private String text;
            private Emoji[] emojis;
            private Mention mention;

            @Data
            public static class Emoji {
                private int index;
                private int length;
                private String productId;
                private String emojiId;
            }

            @Data
            public static class Mention {
                private Mentionees[] mentionees;

                @Data
                public static class Mentionees {
                    private int index;
                    private int length;
                    private String userId;
                }
            }
        }
    }
}
