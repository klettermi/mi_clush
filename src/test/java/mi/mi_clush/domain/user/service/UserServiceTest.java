package mi.mi_clush.domain.user.service;

import mi.mi_clush.domain.user.dto.UserReqDto;
import mi.mi_clush.domain.user.dto.UserResDto;
import mi.mi_clush.domain.user.entity.Role;
import mi.mi_clush.domain.user.entity.User;
import mi.mi_clush.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;
    private UserReqDto userReqDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);

        // 테스트용 사용자 데이터 준비
        userReqDto = UserReqDto.builder()
                .username("testuser")
                .email("testuser@example.com")
                .password("password123")
                .build();
    }

    @Test
    void register_success() {
        // given
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(userReqDto.getPassword())).thenReturn(encodedPassword);

        User savedUser = User.createUser(userReqDto.getUsername(), userReqDto.getEmail(), encodedPassword, Role.USER, passwordEncoder);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        UserResDto result = userService.register(userReqDto);

        // then
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());

        // verify interactions
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(userReqDto.getPassword());
    }
}