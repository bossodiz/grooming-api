package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EUser;
import com.krittawat.groomingapi.datasource.repository.UserRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public EUser findByUsername(String username) throws DataNotFoundException {
        Optional<EUser> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void save(EUser user) {
        userRepository.save(user);
    }
}
