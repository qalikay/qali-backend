package com.qalikay.backend.security.services;

import com.qalikay.backend.security.entities.Role;
import com.qalikay.backend.security.entities.User;
import com.qalikay.backend.security.repositories.RoleRepository;
import com.qalikay.backend.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public Role grabar(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Integer insertUserRol(Long user_id, Long rol_id) {
        userRepository.insertUserRol(user_id, rol_id);
        return 1;
    }

    public boolean existePorUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
