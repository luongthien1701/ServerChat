package com.example.demo.service;

import com.example.demo.dto.noti.Locate;
import com.example.demo.dto.user.UserProfile;
import com.example.demo.dto.user.UserRegisterRequest;
import com.example.demo.dto.user.UserSummary;
import com.example.demo.manager.LocateManager;
import com.example.demo.model.Users;
import com.example.demo.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersrepository) {
        this.usersRepository = usersrepository;
    }

    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public void save(UserRegisterRequest user) {
        Users users=new Users();
        users.setUsername(user.getUsername());
        users.setPassword(user.getPassword());
        users.setEmail(user.getEmail());
        users.setPhone(user.getPhone());
        users.setDisplayName(user.getDisplay_name());
        users.setStatus("OFFLINE");
        users.setLastLogin(new Timestamp(System.currentTimeMillis()));
        users.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        users.setDeletedAt(false);
        usersRepository.save(users);
    }

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Users findByDisplay_name(String display_name) {
        return usersRepository.findByDisplayName(display_name);
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public UserSummary findById(Long id) {
        return usersRepository.findUsersById(id);
    }

    public List<Users> findAllByLikeDisplayName(String likeDisplayName) {
        return usersRepository.findByDisplayNameContaining(likeDisplayName);
    }
    public UserProfile findProfileById(Long id) {
        return usersRepository.findUserProfilesById(id);
    }

    public String changePassword(Long user_id, String oldPassword, String newPassword) {
        Optional<Users> user = usersRepository.findById(user_id);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(oldPassword)) {
                user.get().setPassword(newPassword);
                usersRepository.save(user.get());
                return "Thay đôỉ mật khẩu thành công";
            } else return ("Mật khẩu không đúng vui lòng kiểm tra lại");
        } else return ("Lỗi,không thể thay đổi mật khẩu");
    }

    public void changeAvatar(Long user_id, String avatar) {
        Users user = usersRepository.findUserById(user_id);
        if (user == null) throw new NullPointerException("Không thể tìm thấy người dùng");
        user.setAvatarUrl(avatar);
        usersRepository.save(user);
    }

    public void changeProfile(UserProfile userProfile) {
        Users user = usersRepository.findUserById(userProfile.getId());
        if (user == null) throw new NullPointerException("Người dùng không tồn tại");
        user.setDisplayName(userProfile.getDisplayName());
        user.setEmail(userProfile.getEmail());
        user.setPhone(userProfile.getPhone());
        usersRepository.save(user);
    }

    public List<Map<String, Object>> findUserNear(Long userId) {

        List<Long> users=LocateManager.getUserIds();
        Locate locate=LocateManager.getLocate(userId);
        List<Map<String, Object>> list=new ArrayList<>();
        for(Long id:users){
            if (id==userId) continue;
            UserSummary user=usersRepository.findUsersById(id);
            Locate _locate=LocateManager.getLocate(id);
            if(_locate==null) continue;
            Double distance=LocateManager.distanceKm(locate.getLat(),locate.getLon(),_locate.getLat(),_locate.getLon());
            if (distance<5)
            {
                list.add(Map.of("sender",user,"distance",distance));
            }
        }
        System.out.println("Đây là list:"+list);
        return list;
    }

}
