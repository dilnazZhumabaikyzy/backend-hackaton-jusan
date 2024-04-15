package com.example.backend.service;


import com.example.backend.dto.RequestDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface UserService {
    List<User> getUsers();
    UserDto getUserInfo(Authentication authentication);
    void uploadUserImage(MultipartFile file, Authentication authentication) throws IOException;
    byte[] downloadImage(Authentication authentication);
    void delete(Authentication authentication);
    UserDto updateGeneralData(UserDto updateUser, Authentication authentication);
    UserDto updatePassword(RequestDto requestDto, Authentication authentication);
    UserDto getUserInfoByEmail(String email);
     User getUser(Authentication authentication);
}