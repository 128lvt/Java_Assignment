package com.example.assignment01.services;

import com.example.assignment01.models.User;
import com.example.assignment01.repositories.UserRepository;
import com.example.assignment01.securities.UserPrincipal;
import com.example.assignment01.systems.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User save(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    public User findById(Integer id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("user", id));
    }

    public User update(Integer userId, User updateUser) {
        return this.userRepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setFullname(updateUser.getFullname());
                    oldUser.setAddress(updateUser.getAddress());
                    oldUser.setEnabled(updateUser.isEnabled());
                    oldUser.setUpdate(new Date());
                    oldUser.setPhoneNumber(updateUser.getPhoneNumber());
                    User updatedUser = this.userRepository.save(oldUser);
                    return updatedUser;
                })
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));

    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("category", userId));
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(user -> new UserPrincipal(user))
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " is not found"));
    }
}
