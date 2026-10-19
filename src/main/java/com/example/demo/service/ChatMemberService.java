package com.example.demo.service;

import java.util.List;

public interface ChatMemberService {
    List<Long> getMembers(Long roomId);
}
