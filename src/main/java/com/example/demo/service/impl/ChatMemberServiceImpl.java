package com.example.demo.service.impl;

import com.example.demo.model.ChatMember;
import com.example.demo.repository.ChatMemberRepository;
import com.example.demo.service.ChatMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMemberServiceImpl implements ChatMemberService {
    private final ChatMemberRepository chatMemberRepository;

    @Autowired
    public ChatMemberServiceImpl(ChatMemberRepository chatMemberRepository) {
        this.chatMemberRepository = chatMemberRepository;
    }

    @Override
    public List<Long> getMembers(Long roomId) {
        return chatMemberRepository.findAllByChatRoomId(roomId)
                .stream()
                .distinct()
                .map(k -> k.getUser().getId())
                .toList();
    }
}
