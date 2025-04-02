package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.ERole;
import com.krittawat.groomingapi.datasource.entity.EUser;
import com.krittawat.groomingapi.datasource.repository.UserRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public EUser findByUsername(String username) throws DataNotFoundException {
        Optional<EUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public EUser save(EUser user) {
        return userRepository.save(user);
    }

    public List<EUser> findByCustomers() throws DataNotFoundException {
        ERole roleCustomer = roleService.getRoleCUSTOMER();
        return userRepository.findByRole(roleCustomer);
    }

    public EUser findByCustomersId(Long id) throws DataNotFoundException {
        return userRepository.findByid(id).orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public boolean existsByUsernameNotId(Long id, String phone) {
        return userRepository.findByUsernameAndIdNot(phone, id).isPresent();
    }
}

