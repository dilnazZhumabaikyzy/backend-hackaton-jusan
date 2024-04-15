package com.example.backend.controller;
import com.example.backend.dto.RequestDto;
import com.example.backend.dto.UserDto;
import com.example.backend.service.UserService;
import com.example.backend.service.impl.UserServiceImpl;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication){
        UserDto userDto = userService.getUserInfo(authentication);
        return  ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserInfoByEmail(@RequestParam String email){
        UserDto userDto = userService.getUserInfoByEmail(email);
        return  ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(Authentication authentication){
        userService.delete(authentication);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @Transactional
    @PostMapping("/image")
    public ResponseEntity<Void> uploadImage(@RequestParam("image") MultipartFile file, Authentication authentication) throws IOException {
         userService.uploadUserImage(file ,authentication);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Transactional
    @GetMapping("/image")
    public ResponseEntity<?> downloadImage(Authentication authentication){
        byte[] imageData = userService.downloadImage(authentication);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @PutMapping("/update/general")
    public ResponseEntity<UserDto> updateUserInfo(@RequestBody UserDto updateUser, Authentication authentication){
        return  ResponseEntity.ok().body(userService.updateGeneralData(updateUser, authentication));
    }
    @PutMapping("/update/password")
    public ResponseEntity<UserDto> updateUserInfo(@RequestBody RequestDto requestDto, Authentication authentication){
        return  ResponseEntity.ok().body(userService.updatePassword(requestDto, authentication));
    }

}
