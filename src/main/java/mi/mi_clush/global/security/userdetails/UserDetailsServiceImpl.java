package mi.mi_clush.global.security.userdetails;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.mi_clush.domain.user.entity.User;
import mi.mi_clush.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static mi.mi_clush.global.enums.ErrorCode.USER_NOT_FOUND;

@Service
@Slf4j(topic = "UserDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {

		log.info("email: {}", email);

		User user = userRepository.findByEmail(email).orElseThrow(
			() -> new UsernameNotFoundException(USER_NOT_FOUND.getDetail())
		);

		log.info("name: {}", user.getUsername());

		return new UserDetailsImpl(user);
	}
}

