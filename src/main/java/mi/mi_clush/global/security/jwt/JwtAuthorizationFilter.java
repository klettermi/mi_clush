package mi.mi_clush.global.security.jwt;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.mi_clush.global.exception.GlobalException;
import mi.mi_clush.global.security.exception.JwtPrefixException;
import mi.mi_clush.global.security.userdetails.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static mi.mi_clush.global.enums.ErrorCode.JWT_PREFIX_ERROR;

@Slf4j(topic = "JWT 검증 후 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String tokenValue = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

		// 토큰이 없으면 다음 필터로 이동
		if (!StringUtils.hasText(tokenValue)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			// 토큰의 접두사 검증
			if (jwtUtil.isIllegalPrefix(tokenValue)) {
				log.error("토큰의 식별자가 유효하지 않습니다.");
				throw new JwtPrefixException(JWT_PREFIX_ERROR);  // 예외 처리
			}

			// 토큰 추출
			String token = jwtUtil.substringToken(tokenValue);

			// 토큰 유효성 검증
			if (!jwtUtil.validateToken(token)) {
				log.error("토큰 유효성 검증 실패");
				throw new JwtPrefixException(JWT_PREFIX_ERROR);  // 유효하지 않은 토큰 처리
			}

			// 토큰에서 사용자 정보 추출
			Claims info = jwtUtil.getClaimsFromToken(token);
			log.info("Extracted email from token: {}", info.getSubject());
			// 사용자 인증 정보 설정
			setAuthentication(info.getSubject());

		} catch (GlobalException e) {
			// 예외가 발생한 경우, 응답 상태 코드와 메시지 처리
			log.error("JWT 인증 처리 중 오류 발생", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getErrorCode().getDetail());
			return;  // 오류 발생 시, 이후 필터가 실행되지 않도록 처리
		}

		// 정상적으로 인증된 경우, 다음 필터로 처리
		filterChain.doFilter(request, response);
	}


	// 인증 처리
	private void setAuthentication(String userEmail) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); // 여기서 UserDetails를 제대로 가져오는지 확인
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		log.info("Authentication set for user: {}", userEmail);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String userEmail) {

		UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
		log.info("UserDetails: {}", userDetails.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}