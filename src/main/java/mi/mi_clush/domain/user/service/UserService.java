package mi.mi_clush.domain.user.service;

import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.user.dto.UserReqDto;
import mi.mi_clush.domain.user.dto.UserResDto;
import mi.mi_clush.domain.user.entity.Role;
import mi.mi_clush.domain.user.entity.User;
import mi.mi_clush.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResDto register(UserReqDto request) {
        User user = User.createUser(request.getUsername(), request.getEmail(), request.getPassword(), Role.USER, passwordEncoder);
        User savedUser = userRepository.save(user);
        return UserResDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();
    }
}
