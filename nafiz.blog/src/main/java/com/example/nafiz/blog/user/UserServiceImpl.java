package com.example.nafiz.blog.user;

import com.example.nafiz.blog.common.ChangePassword;
import com.example.nafiz.blog.common.ResponseWrapper;
import com.example.nafiz.blog.common.enums.UserRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserInfoDTO mapToDTO(UserInfo entity) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setContact(entity.getContact());
        dto.setRoles(entity.getRoles().name());
        return dto;
    }

    private UserInfo mapToEntity(UserInfoDTO dto) {
        UserInfo entity = new UserInfo();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername().toLowerCase());
        entity.setEmail(dto.getEmail());
        entity.setContact(dto.getContact());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRoles(UserRoles.ROLE_USER);
        return entity;
    }

    @Override
    public Map<String, Object> saveUser(UserInfoDTO userDTO) {
        try {
            UserInfo saved = userRepository.save(mapToEntity(userDTO));
            log.info("Created user with ID: {}", saved.getId());
            return ResponseWrapper.wrap("User created successfully", mapToDTO(saved));
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseWrapper.wrapFailure("Failed to create user");
        }
    }


    @Override
    public Map<String, Object> getAllUsers() {
        try {
            List<UserInfoDTO> users = userRepository.findAll().stream()
                    .filter(user -> user.getRoles().equals(UserRoles.ROLE_USER))
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            log.info("Fetched {} user(s)", users.size());
            return ResponseWrapper.wrap("Users fetched successfully", users);
        } catch (Exception e) {
            log.error("Error fetching users", e);
            return ResponseWrapper.wrapFailure("Failed to fetch users");
        }
    }


    @Override
    public Map<String, Object> getUserById(Long id) {
        try {
            UserInfo user = userRepository.findById(id)
                    .filter(u -> u.getRoles().equals(UserRoles.ROLE_USER))
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            log.info("Fetched user with ID: {}", id);
            return ResponseWrapper.wrap("User fetched successfully", mapToDTO(user));
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            return ResponseWrapper.wrapFailure(e.getMessage());
        }
    }


    @Override
    public Map<String, Object> updateUser(Long id, UserInfoDTO userDTO) {
        try {
            UserInfo user = userRepository.findById(id)
                    .filter(u -> u.getRoles().equals(UserRoles.ROLE_USER))
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername().toLowerCase());
            user.setEmail(userDTO.getEmail());
            user.setContact(userDTO.getContact());

            UserInfo updated = userRepository.save(user);
            log.info("Updated user with ID: {}", id);
            return ResponseWrapper.wrap("User updated successfully", mapToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating user with ID: {}", id, e);
            return ResponseWrapper.wrapFailure(e.getMessage());
        }
    }


    @Override
    public Map<String, Object> deleteUser(Long id) {
        try {
            UserInfo user = userRepository.findById(id)
                    .filter(u -> u.getRoles().equals(UserRoles.ROLE_USER))
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            userRepository.delete(user);
            log.info("Deleted user with ID: {}", id);
            return ResponseWrapper.wrap("User deleted successfully", null);
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id, e);
            return ResponseWrapper.wrapFailure(e.getMessage());
        }
    }


    @Override
    public Map<String, Object> changePassword(Long id, ChangePassword changePassword) {
        try {
            UserInfo user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            if (passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
                userRepository.save(user);
                log.info("Password changed for user ID: {}", id);
                return ResponseWrapper.wrap("Password changed successfully", null);
            } else {
                return ResponseWrapper.wrapFailure("Old password does not match");
            }
        } catch (Exception e) {
            log.error("Error changing password for user ID: {}", id, e);
            return ResponseWrapper.wrapFailure(e.getMessage());
        }
    }

}
