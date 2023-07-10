package com.example.contents;

import com.example.contents.dto.UserDto;
import com.example.contents.entity.UserEntity;
import com.example.contents.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    // createUser
    public UserDto createUser(UserDto dto) {
        if (repository.existsByUsername(dto.getUsername()))
//            throw new UsernameExistException();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already username");
//            throw new IllegalStateException();

        UserEntity newUser = new UserEntity();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setPhone(dto.getPhone());
        newUser.setBio(dto.getBio());

        return UserDto.fromEntity(repository.save(newUser));
    }

    // readUserByUsername
    public UserDto readUserByUsername(String username) {
        Optional<UserEntity> optionalUser = repository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException();
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return UserDto.fromEntity(optionalUser.get());
    }

    // updateUser
    public UserDto updateUser(Long id, UserDto dto) {
        // update user로 사용자 username은 변경할 수 없도록
        Optional<UserEntity> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException();
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        UserEntity userEntity = optionalUser.get();
        userEntity.setEmail(dto.getEmail());
        userEntity.setPhone(dto.getPhone());
        userEntity.setBio(dto.getBio());
        return UserDto.fromEntity(repository.save(userEntity));
    }

    // updateUserAvatar
    public UserDto updateUserAvatar(Long id, MultipartFile avatarImage) {
        // 사용자가 프로필 이미지를 업로드

        // 1. 유저 존재 확인
        Optional<UserEntity> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException();
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 2. 파일 업로드 위치
        // media/{userId}/profile.{파일 확장자}
        String profileDir = String.format("media/%d", id);
        // 2-1. 폴더만 만들기
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 2-2. 확장자를 포함한 이미지 이름 만들기 (profile.{확장자})
        String originalFilename = avatarImage.getOriginalFilename();
        // ex) avatar.png -> fileNameSplit = {"avatar", "png"}
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profileFileName = "profile." + extension;
        log.info(profileFileName);

        // 2-3. 폴더와 파일 경로를 포함한 이름 만들기
        String profilePath = profileDir + "/" + profileFileName;

        // 3. MultipartFile 저장
        try {
            avatarImage.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 4. UserEntity 업데이트 (정적 프로필 이미지를 회수할 수 있는 URL)
        // http://localhost:8080/static/1/profile.png or .jpg
        log.info(String.format("/static/%d/%s", id, profileFileName));
        log.info("static/" + profileFileName);
        log.info(String.format("/media/%d" + profilePath, id));
        log.info("/static/" + profilePath);
        UserEntity userEntity = optionalUser.get();
        userEntity.setAvatar(String.format("/static/%d/%s", id, profileFileName));
        return UserDto.fromEntity(userEntity);
    }
}
