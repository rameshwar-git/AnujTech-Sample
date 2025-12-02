package com.anujtech.app.notification;

public class SendNotification{
    public SendNotification(String title,String body){
        String topic="all";

        Message message= Message.builder()
                .putData("title",title)
                .putData("body",body)
                .setTopic(topic)
                .build();

         
    }
}
