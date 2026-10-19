package com.example.demo.service.impl;

import com.example.demo.dto.noti.Locate;
import com.example.demo.dto.user.*;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.manager.LocateManager;
import com.example.demo.model.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public UserServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null || userLoginRequest.getUsername() == null || userLoginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin đăng nhập");
        }
        Users user = usersRepository.findByUsername(userLoginRequest.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại");
        }
        boolean matches = passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())
                || user.getPassword().equals(userLoginRequest.getPassword());
        if (!matches) {
            throw new InvalidCredentialsException("Mật khẩu không chính xác");
        }
        String token=jwtService.generateToken(user.getId());
        return new LoginResponse(
                user.getId(),
                user.getDisplayName(),
                user.getAvatarUrl() == null ? "" : user.getAvatarUrl(),
                token
        );
    }

    @Override
    public void register(UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null || userRegisterRequest.getUsername() == null) {
            throw new IllegalArgumentException("Thông tin đăng ký không hợp lệ");
        }
        if (usersRepository.findByUsername(userRegisterRequest.getUsername()) != null) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!Vui lòng thử lại");
        }
        if (usersRepository.findByEmail(userRegisterRequest.getEmail()) != null) {
            throw new IllegalArgumentException("Email đã tồn tại!Vui lòng thử lại");
        }
        if (usersRepository.findByDisplayName(userRegisterRequest.getDisplay_name()) != null) {
            throw new IllegalArgumentException("Tên hiển thị đã tồn tại!Vui lòng thử lại");
        }
        save(userRegisterRequest);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return usersRepository.findAll().stream()
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .displayName(u.getDisplayName())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .avatarUrl(u.getAvatarUrl())
                        .status(u.getStatus())
                        .build())
                .toList();
    }

    @Override
    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Override
    public void save(UserRegisterRequest user) {
        Users users = new Users();
        users.setUsername(user.getUsername());
        users.setPassword(passwordEncoder.encode(user.getPassword()));
        users.setEmail(user.getEmail());
        users.setPhone(user.getPhone());
        users.setDisplayName(user.getDisplay_name());
        users.setStatus("OFFLINE");
        users.setLastLogin(new Timestamp(System.currentTimeMillis()));
        users.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        users.setDeletedAt(false);
        usersRepository.save(users);
    }

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public Users findByDisplay_name(String display_name) {
        return usersRepository.findByDisplayName(display_name);
    }

    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public UserSummary findById(Long id) {
        return usersRepository.findUsersById(id);
    }

    @Override
    public List<Users> findAllByLikeDisplayName(String likeDisplayName) {
        return usersRepository.findByDisplayNameContaining(likeDisplayName);
    }

    @Override
    public UserProfile findProfileById(Long id) {
        return usersRepository.findUserProfilesById(id);
    }

    @Override
    public String changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            boolean matches = passwordEncoder.matches(oldPassword, user.get().getPassword())
                    || user.get().getPassword().equals(oldPassword);
            if (matches) {
                user.get().setPassword(passwordEncoder.encode(newPassword));
                usersRepository.save(user.get());
                return "Thay đôỉ mật khẩu thành công";
            } else {
                return "Mật khẩu không đúng vui lòng kiểm tra lại";
            }
        } else {
            return "Lỗi,không thể thay đổi mật khẩu";
        }
    }

    @Override
    public void changeAvatar(Long userId, String avatar) {
        Users user = usersRepository.findUserById(userId);
        if (user == null) throw new NullPointerException("Không thể tìm thấy người dùng");
        user.setAvatarUrl(avatar);
        usersRepository.save(user);
    }

    @Override
    public void changeProfile(UserProfile userProfile) {
        Users user = usersRepository.findUserById(userProfile.getId());
        if (user == null) throw new NullPointerException("Người dùng không tồn tại");
        user.setDisplayName(userProfile.getDisplayName());
        user.setEmail(userProfile.getEmail());
        user.setPhone(userProfile.getPhone());
        usersRepository.save(user);
    }

    @Override
    public List<UserNearResponse> findUserNear(Long userId) {
        List<Long> users = LocateManager.getUserIds();
        Locate locate = LocateManager.getLocate(userId);
        List<UserNearResponse> list = new ArrayList<>();
        if (locate == null) {
            return list;
        }
        for (Long id : users) {
            if (id.equals(userId)) continue;
            UserSummary user = usersRepository.findUsersById(id);
            Locate _locate = LocateManager.getLocate(id);
            if (_locate == null) continue;
            Double distance = LocateManager.distanceKm(locate.getLat(), locate.getLon(), _locate.getLat(), _locate.getLon());
            if (distance < 5) {
                list.add(new UserNearResponse(user, distance));
            }
        }
        return list;
    }

    @Override
    public void updateUserLocation(Long userId, Locate locate) {
        if (locate != null) {
            LocateManager.addLocate(userId, locate.getLat(), locate.getLon());
        }
    }
}
