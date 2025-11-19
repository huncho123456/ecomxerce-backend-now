package com.ecommerce.app.security;


import com.ecommerce.app.entity.UserEntity;
import com.ecommerce.app.exceptions.NotFoundException;
import com.ecommerce.app.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("user Not Found"));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
