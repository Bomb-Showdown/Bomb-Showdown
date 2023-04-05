package edu.eci.arsw.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class STOMPMessagesController {

    @Autowired
    SimpMessagingTemplate msgt;

    List<String> rooms = new CopyOnWriteArrayList<>();

    @MessageMapping("/code.{room}")
    public String messagesHandler(String code, @DestinationVariable String room) throws Exception {
        msgt.convertAndSend("/topic/code."+room, code);
        if (!rooms.contains(room)) {
            rooms.add(room);
        }
        System.out.println(rooms);
        return "You just entered to the room with code: " + code;
    }
}
