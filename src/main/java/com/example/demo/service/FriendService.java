package com.example.demo.service;

import com.example.demo.dto.friend.FriendRequest;
import com.example.demo.dto.friend.FriendResponse;
import com.example.demo.model.Friend;

import java.util.List;

public interface FriendService {
    List<FriendResponse> getAllFriends(Long userId);
    void addFriend(FriendRequest fr);
    void removeFriend(FriendRequest fr);
    void blockFriend(FriendRequest fr);
    Friend.Status checkFriend(Long userId, Long friendId);
    void acceptFriend(Long userId, Long friendId);
}
