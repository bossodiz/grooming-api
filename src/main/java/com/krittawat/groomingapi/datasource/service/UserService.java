package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.ERole;
import com.krittawat.groomingapi.datasource.entity.EUserProfile;
import com.krittawat.groomingapi.datasource.repository.UserRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public EUserProfile findByUsername(String username) throws DataNotFoundException {
        Optional<EUserProfile> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public EUserProfile save(EUserProfile user) {
        return userRepository.save(user);
    }

    public List<EUserProfile> findByCustomers() throws DataNotFoundException {
        ERole roleCustomer = roleService.getRoleCUSTOMER();
        return userRepository.findByRole(roleCustomer);
    }

    public EUserProfile findByCustomersId(Long id) throws DataNotFoundException {
        return userRepository.findByid(id).orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public boolean existsByUsernameNotId(Long id, String phone) {
        return userRepository.findByUsernameAndIdNot(phone, id).isPresent();
    }
}

