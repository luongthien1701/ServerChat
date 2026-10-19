package com.example.demo.service.impl;

import com.example.demo.dto.friend.FriendRequest;
import com.example.demo.dto.friend.FriendResponse;
import com.example.demo.manager.GlobalOnlineManager;
import com.example.demo.model.Friend;
import com.example.demo.model.Users;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.FriendService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UsersRepository usersRepository;

    public FriendServiceImpl(FriendRepository friendRepository, UsersRepository usersRepository) {
        this.friendRepository = friendRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public List<FriendResponse> getAllFriends(Long userId) {
        List<FriendResponse> sent = friendRepository.findAllByUserSent(userId);
        List<FriendResponse> received = friendRepository.findAllByUserReceived(userId);

        Map<Long, FriendResponse> unique = new LinkedHashMap<>();
        for (FriendResponse f : sent) {
            if (GlobalOnlineManager.isOnlineUser(f.getFriendId())) {
                f.setStatus("ONLINE");
            }
            unique.put(f.getFriendId(), f);
        }
        for (FriendResponse f : received) {
            if (GlobalOnlineManager.isOnlineUser(f.getFriendId())) {
                f.setStatus("ONLINE");
            }
            unique.put(f.getFriendId(), f);
        }
        return new ArrayList<>(unique.values());
    }

    @Override
    public void addFriend(FriendRequest fr) {
        Users sender = usersRepository.findById(fr.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found"));
        Users received = usersRepository.findById(fr.getReceiverId()).orElseThrow(() -> new RuntimeException("Receiver not found"));
        Friend friend = new Friend();
        friend.setUser(sender);
        friend.setFriend(received);
        friend.setStatus(Friend.Status.PENDING);
        friendRepository.save(friend);
    }

    @Override
    public void removeFriend(FriendRequest fr) {
        Friend f = friendRepository.findRelation(fr.getSenderId(), fr.getReceiverId());
        if (f != null) {
            friendRepository.delete(f);
        } else {
            throw new RuntimeException("Friend not found");
        }
    }

    @Override
    public void blockFriend(FriendRequest fr) {
        Friend f = friendRepository.findRelation(fr.getSenderId(), fr.getReceiverId());
        if (f == null) {
            f = friendRepository.findRelation(fr.getReceiverId(), fr.getSenderId());
        }
        if (f != null) {
            f.setStatus(Friend.Status.BLOCKED);
            friendRepository.save(f);
        } else {
            throw new RuntimeException("Friend not found");
        }
    }

    @Override
    public Friend.Status checkFriend(Long userId, Long friendId) {
        Friend fr = friendRepository.findRelation(userId, friendId);
        if (fr == null) return Friend.Status.NONE;
        if (fr.getStatus() == Friend.Status.ACCEPTED) return Friend.Status.ACCEPTED;
        if (fr.getStatus() == Friend.Status.PENDING) {
            if (fr.getUser().getId().equals(userId)) {
                return Friend.Status.RECEIVED;
            } else {
                return Friend.Status.PENDING;
            }
        }
        return fr.getStatus();
    }

    @Override
    public void acceptFriend(Long userId, Long friendId) {
        Friend f = friendRepository.findRelation(userId, friendId);
        if (f == null) {
            throw new RuntimeException("Friend not found");
        }
        f.setStatus(Friend.Status.ACCEPTED);
        friendRepository.save(f);
    }
}
