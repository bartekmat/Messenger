package com.gruzini.messenger.config;


import com.gruzini.messenger.models.MyPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class UserInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        /*
        here we intercept and connect command - in our case every time user connects to chat - opens our application
        we check if there is any principal in the session, in the beginning there never is, so we create new one with name of current session id number

         */
        if (StompCommand.CONNECT.equals(accessor.getCommand())){
            if (accessor.getUser() == null){
                accessor.setUser(new MyPrincipal(accessor.getSessionId()));
            }
        }
        return message;
    }
}
