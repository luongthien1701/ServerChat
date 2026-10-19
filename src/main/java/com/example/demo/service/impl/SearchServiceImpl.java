package com.example.demo.service.impl;

import com.example.demo.dto.chat.MessengerResponse;
import com.example.demo.dto.search.SelectSearch;
import com.example.demo.model.ChatRoom;
import com.example.demo.model.Users;
import com.example.demo.service.ChatRoomService;
import com.example.demo.service.MessageService;
import com.example.demo.service.SearchService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserService userService;
    private final MessageService messageService;
    private final ChatRoomService chatRoomService;

    @Override
    public List<SelectSearch> searchAll(Long userId, String query) {
        List<SelectSearch> results = new ArrayList<>();

        List<Users> users = userService.findAllByLikeDisplayName(query);
        List<MessengerResponse> messageList = messageService.findLikeContent(userId, query);
        List<ChatRoom> rooms = chatRoomService.findbylikeName(userId, query);

        if (users != null) {
            users.forEach(user -> results.add(
                    new SelectSearch("user", user.getId(), user.getDisplayName(), user.getAvatarUrl(), null, null, null)
            ));
        }

        if (messageList != null) {
            messageList.forEach(mess -> results.add(
                    new SelectSearch(
                            "messager",
                            mess.getId(),
                            mess.getSender() != null ? mess.getSender().getName() : null,
                            null,
                            mess.getContent(),
                            mess.getGroupId(),
                            mess.getGroupName()
                    )
            ));
        }

        if (rooms != null) {
            rooms.forEach(room -> results.add(
                    new SelectSearch("room", null, null, room.getAvatarUrl(), null, room.getId(), room.getName())
            ));
        }

        return results;
    }
}
