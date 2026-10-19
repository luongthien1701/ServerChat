package com.example.demo.dto.friend;

import com.example.demo.model.Friend;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendStatusResponse {
    private Friend.Status status;
}
