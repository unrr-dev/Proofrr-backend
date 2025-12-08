package com.proofrr.proofrr.controller;

import com.proofrr.proofrr.dto.ChatMessageRequest;
import com.proofrr.proofrr.dto.ChatMessageResponse;
import com.proofrr.proofrr.model.ProjectChatMessage;
import com.proofrr.proofrr.service.ProjectChatService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageWebSocketController {

    private final ProjectChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageWebSocketController(ProjectChatService chatService,
                                          SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/projects/{shareUuid}/chat/send")
    public void handleChatMessage(@DestinationVariable("shareUuid") String shareUuid,
                                  @Valid ChatMessageRequest request) {
        ProjectChatMessage saved = chatService.saveVisitorMessage(
                shareUuid,
                request.getVisitorId(),
                request.getSenderName(),
                request.getContent());

        ChatMessageResponse payload = ChatMessageResponse.from(saved);
        messagingTemplate.convertAndSend("/topic/projects/" + shareUuid + "/chat", payload);
    }

    @MessageExceptionHandler(IllegalArgumentException.class)
    @SendToUser("/queue/errors")
    public String handleIllegalArgument(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
