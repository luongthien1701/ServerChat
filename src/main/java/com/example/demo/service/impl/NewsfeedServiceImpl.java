package com.example.demo.service.impl;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.post.UpdatePost;
import com.example.demo.dto.user.UserSummary;
import com.example.demo.model.Newsfeed;
import com.example.demo.model.Post_Image;
import com.example.demo.model.Post_Like;
import com.example.demo.model.Users;
import com.example.demo.repository.*;
import com.example.demo.service.NewsfeedService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class NewsfeedServiceImpl implements NewsfeedService {
    private final FriendRepository friendRepository;
    private final NewsfeedRepository newsfeedRepository;
    private final UsersRepository usersRepository;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;

    @Override
    public List<PostDTO> findAllByUserReceived(Long userId) {
        List<PostDTO> list = new ArrayList<>();
        List<Long> friend1 = friendRepository.findFriendsWhereUserIsSender(userId);
        List<Long> friend2 = friendRepository.findFriendsWhereUserIsReceiver(userId);
        Set<Long> listFriends = new HashSet<>();
        listFriends.addAll(friend1);
        listFriends.addAll(friend2);
        for (Long friendId : listFriends) {
            List<Newsfeed> newsfeed = newsfeedRepository.findAllByUserId(friendId);
            for (Newsfeed newsfeed1 : newsfeed) {
                if (!newsfeed1.isDeleted()) {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setId(newsfeed1.getId());
                    postDTO.setSender(new UserSummary(newsfeed1.getUser().getId(), newsfeed1.getUser().getDisplayName(), newsfeed1.getUser().getAvatarUrl()));
                    postDTO.setContent(newsfeed1.getContent());
                    postDTO.setFavorite(newsfeed1.getFavorite());
                    postDTO.setImage(imageRepository.findAllByNewsfeedId(newsfeed1.getId()).stream().map(Post_Image::getImgUrl).collect(Collectors.toList()));
                    postDTO.setCreateAt(newsfeed1.getCreateAt());
                    postDTO.setComments(newsfeed1.getComments());
                    postDTO.set_liked(likeRepository.existsByNewsfeedIdAndUserId(newsfeed1.getId(), userId));
                    list.add(postDTO);
                }
            }
        }
        return list;
    }

    @Override
    public long addPost(PostDTO postDTO) {
        Newsfeed nf = new Newsfeed();
        Users user = usersRepository.findUserById(postDTO.getSender().getId());
        nf.setComments(0L);
        nf.setContent(postDTO.getContent());
        nf.setFavorite(0L);
        nf.setUser(user);
        nf.setCreateAt(new Timestamp(System.currentTimeMillis()));
        nf.setDeleted(false);
        newsfeedRepository.save(nf);
        return nf.getId();
    }

    @Override
    public void addLikePost(UpdatePost updatePost) {
        if (likeRepository.existsByNewsfeedIdAndUserId(updatePost.getPostId(), updatePost.getUserId())) {
            throw new RuntimeException("Đã xảy ra lỗi");
        }
        Newsfeed nf = newsfeedRepository.findById(updatePost.getPostId());
        Users us = usersRepository.findUserById((long) updatePost.getUserId());
        if (nf == null) throw new RuntimeException("Lỗi bài viết có thể không tồn tại");
        nf.setFavorite(nf.getFavorite() + 1);
        Post_Like like = new Post_Like();
        like.setNewsfeed(nf);
        like.setUser(us);
        likeRepository.save(like);
        newsfeedRepository.save(nf);
    }

    @Override
    public void dscLikePost(UpdatePost updatePost) {
        System.out.println(updatePost.getPostId() + "" + updatePost.getUserId());
        if (!likeRepository.existsByNewsfeedIdAndUserId(updatePost.getPostId(), updatePost.getUserId())) {
            throw new RuntimeException("Đã xảy ra lỗi");
        }
        likeRepository.deleteByNewsfeedIdAndUserId(updatePost.getPostId(), updatePost.getUserId());
        Newsfeed nf = newsfeedRepository.findById(updatePost.getPostId());
        if (nf == null) throw new RuntimeException("Lỗi bài viết có thể không tồn tại");
        nf.setFavorite(nf.getFavorite() - 1);
        newsfeedRepository.save(nf);
    }

    @Override
    public void deletePost(int postId) {
        Newsfeed nf = newsfeedRepository.findById(postId);
        nf.setDeleted(true);
        newsfeedRepository.save(nf);
    }

    @Override
    public List<PostDTO> findAllPostsByUserId(long userId) {
        List<Newsfeed> newsfeedList = newsfeedRepository.findAllByUserId(userId);
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Newsfeed newsfeed : newsfeedList) {
            if (newsfeed.isDeleted()) continue;
            PostDTO postDTO = new PostDTO();
            postDTO.setId(newsfeed.getId());
            postDTO.setSender(new UserSummary(newsfeed.getUser().getId(), newsfeed.getUser().getDisplayName(), newsfeed.getUser().getAvatarUrl()));
            postDTO.setContent(newsfeed.getContent());
            postDTO.setFavorite(newsfeed.getFavorite());
            postDTO.setImage(imageRepository.findAllByNewsfeedId(newsfeed.getId()).stream().map(Post_Image::getImgUrl).collect(Collectors.toList()));
            postDTO.setCreateAt(newsfeed.getCreateAt());
            postDTO.setComments(newsfeed.getComments());
            postDTO.set_liked(likeRepository.existsByNewsfeedIdAndUserId(newsfeed.getId(), userId));
            postDTOList.add(postDTO);
        }
        return postDTOList;
    }

    @Override
    public Long getUserId(long postId) {
        return newsfeedRepository.findById(postId).getUser().getId();
    }
}
