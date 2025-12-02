package com.anujtech.app.notification;

import static com.google.common.base.Preconditions.checkArgument;

import android.app.Notification;

import androidx.annotation.NonNull;

import com.google.api.client.util.Key;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Booleans;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a message that can be sent via Firebase Cloud Messaging (FCM). Contains payload
 * information as well as the recipient information. The recipient information must contain exactly
 * one token, topic or condition parameter. Instances of this class are thread-safe and immutable.
 * Use {@link Message.Builder} to create new instances.
 *
 * @see
 * <a href="https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages">FCM message
 * format</a>
 */
public class Message {

    @Key("data")
    private final Map<String, String> data;

    @Key("notification")
    private final Notification notification;

    @Key("token")
    private final String token;

    @Key("topic")
    private final String topic;

    @Key("condition")
    private final String condition;

    private Message(Builder builder) {
        this.data = builder.data.isEmpty() ? null : ImmutableMap.copyOf(builder.data);
        this.notification = builder.notification;
        int count = Booleans.countTrue(
                !Strings.isNullOrEmpty(builder.token),
                !Strings.isNullOrEmpty(builder.topic),
                !Strings.isNullOrEmpty(builder.condition)
        );
        checkArgument(count == 1, "Exactly one of token, topic or condition must be specified");
        this.token = builder.token;
        this.topic = stripPrefix(builder.topic);
        this.condition = builder.condition;
    }

    @VisibleForTesting
    Map<String, String> getData() {
        return data;
    }

    @VisibleForTesting
    Notification getNotification() {
        return notification;
    }


    @VisibleForTesting
    String getToken() {
        return token;
    }

    @VisibleForTesting
    String getTopic() {
        return topic;
    }

    @VisibleForTesting
    String getCondition() {
        return condition;
    }


    Map<String, Object> wrapForTransport(boolean dryRun) {
        ImmutableMap.Builder<String, Object> payload = ImmutableMap.<String, Object>builder()
                .put("message", this);
        if (dryRun) {
            payload.put("validate_only", true);
        }
        return payload.build();
    }

    private static String stripPrefix(String topic) {
        if (Strings.isNullOrEmpty(topic)) {
            return null;
        }
        if (topic.startsWith("/topics/")) {
            topic = topic.replaceFirst("^/topics/", "");
        }
        // Checks for illegal characters and empty string.
        checkArgument(topic.matches("[a-zA-Z0-9-_.~%]+"), "Malformed topic name");
        return topic;
    }

    /**
     * Creates a new {@link Message.Builder}.
     *
     * @return A {@link Message.Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class  Builder {

        private final Map<String, String> data = new HashMap<>();
        private Notification notification;
        private String token;
        private String topic;
        private String condition;


        private Builder() {}

        /**
         * Sets the notification information to be included in the message.
         *
         * @param notification A {@link Notification} instance.
         * @return This builder.
         */
        public Builder setNotification(Notification notification) {
            this.notification = notification;
            return this;
        }


        /**
         * Sets the registration token of the device to which the message should be sent.
         *
         * @param token A valid device registration token.
         * @return This builder.
         */
        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        /**
         * Sets the name of the FCM topic to which the message should be sent. Topic names may
         * contain the {@code /topics/} prefix.
         *
         * @param topic A valid topic name.
         * @return This builder.
         */
        public Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        /**
         * Sets the FCM condition to which the message should be sent.
         *
         * @param condition A valid condition string (e.g. {@code "'foo' in topics"}).
         * @return This builder.
         */
        public Builder setCondition(String condition) {
            this.condition = condition;
            return this;
        }

        /**
         * Adds the given key-value pair to the message as a data field. Key or the value may not be
         * null.
         *
         * @param key Name of the data field. Must not be null.
         * @param value Value of the data field. Must not be null.
         * @return This builder.
         */
        public Builder putData(@NonNull String key, @NonNull String value) {
            this.data.put(key, value);
            return this;
        }

        /**
         * Adds all the key-value pairs in the given map to the message as data fields. None of the
         * keys or values may be null.
         *
         * @param map A non-null map of data fields. Map must not contain null keys or values.
         * @return This builder.
         */
        public Builder putAllData(@NonNull Map<String, String> map) {
            this.data.putAll(map);
            return this;
        }

        /**
         * Creates a new {@link Message} instance from the parameters set on this builder.
         *
         * @return A new {@link Message} instance.
         * @throws IllegalArgumentException If any of the parameters set on the builder are invalid.
         */
        public Message build() {
            return new Message(this);
        }
    }
}