package com.homeshare.service;

import com.homeshare.domain.User;
import com.homeshare.domain.request.CreateUserRequest;
import com.homeshare.repository.UserFileRepository;
import com.homeshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;       // real DB (e.g. JPA)
    private final UserFileRepository userFileRepository; // JSON-based

    public User updateUser(User user) {
        // 1) Update in DB
        User updatedUser = userRepository.save(user);

        // 2) Also update in JSON
        userFileRepository.saveUser(updatedUser);

        return updatedUser;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User awardPoints(User user, int points) {
        user.setPoints(user.getPoints() + points);
        return updateUser(user);
    }

}
