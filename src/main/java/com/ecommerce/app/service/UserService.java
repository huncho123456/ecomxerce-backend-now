package com.ecommerce.app.service;

import com.ecommerce.app.dto.Response;
import com.ecommerce.app.dto.UserDto;
import com.ecommerce.app.entity.CartEntity;
import com.ecommerce.app.entity.CartItemEntity;
import com.ecommerce.app.entity.UserEntity;
import com.ecommerce.app.enums.UserRole;
import com.ecommerce.app.repo.CartRepository;
import com.ecommerce.app.repo.UserRepository;
import com.ecommerce.app.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CartRepository cartRepository;

    @Transactional
    public void migrateGuestCartToUser(UUID guestId, UUID userId) {
        // 1. Find the guest cart
        Optional<CartEntity> guestCartOpt = cartRepository.findById(guestId);

        if (guestCartOpt.isPresent()) {
            CartEntity guestCart = guestCartOpt.get();

            // 2. Create or find the user's permanent cart
            CartEntity userCart = cartRepository.findById(userId)
                    .orElseGet(() -> {
                        CartEntity newCart = new CartEntity();
                        newCart.setGuest_id(userId); // Now acting as user_id
                        return cartRepository.save(newCart);
                    });

            // 3. Move items from guest cart to user cart
            for (CartItemEntity item : guestCart.getItems()) {
                item.setCart(userCart);
                userCart.getItems().add(item);
            }

            // 4. Save the user cart (cascades to items) and delete the guest cart
            cartRepository.save(userCart);

            // Clear items from guest cart first to avoid constraint violations during delete
            guestCart.getItems().clear();
            cartRepository.delete(guestCart);
        }
    }

    public Response<UserDto> register(UserEntity user) {
        if (repo.existsByEmail(user.getEmail())) {
            return Response.<UserDto>builder()
                    .message("Email already registered: " + user.getEmail())
                    .status(400)
                    .build();
        }
        
        if (user.getRole() == null) {
            user.setRole(UserRole.CUSTOMER);
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity saved = repo.save(user);

        String token = jwtUtils.generateToken(saved.getEmail());


        UserDto dto = mapper.map(saved, UserDto.class);

        return Response.<UserDto>builder()
                .message("User registered successfully")
                .status(201)
                .token(token)
                .user(dto)
                .build();
    }

    public Response<UserDto> login(String email, String password) {
        UserEntity user = repo.findByEmail(email).orElse(null);

        if (user == null) {
            return Response.<UserDto>builder()
                    .message("User not found")
                    .status(404)
                    .build();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Response.<UserDto>builder()
                    .message("Password incorrect")
                    .status(401)
                    .build();
        }

        UserDto dto = mapper.map(user, UserDto.class);

        return Response.<UserDto>builder()
                .message(user.getEmail() + " successfully logged in")
                .status(200)
                .token(jwtUtils.generateToken(user.getEmail()))
                .role(user.getRole())
                .user(dto)
                .build();
    }

}
