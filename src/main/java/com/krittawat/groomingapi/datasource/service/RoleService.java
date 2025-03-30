package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.ERole;
import com.krittawat.groomingapi.datasource.repository.RoleRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public ERole getRoleById(Long id) throws DataNotFoundException {
        Optional<ERole> role = roleRepository.findByid(id);
        if (role.isPresent()){
            return role.get();
        } else {
            throw new DataNotFoundException("Role not found");
        }
    }

    public ERole getRoleAdmin() throws DataNotFoundException {
        Optional<ERole> role = roleRepository.findByName(EnumUtil.Role.ADMIN.name());
        if (role.isPresent()){
            return role.get();
        } else {
            throw new DataNotFoundException("Role not found");
        }
    }

    public ERole getRoleCUSTOMER() throws DataNotFoundException {
        Optional<ERole> role = roleRepository.findByName(EnumUtil.Role.CUSTOMER.name());
        if (role.isPresent()){
            return role.get();
        } else {
            throw new DataNotFoundException("Role not found");
        }
    }

}
