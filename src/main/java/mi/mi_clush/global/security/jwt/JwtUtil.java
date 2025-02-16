package mi.mi_clush.global.security.jwt;

import io.jsonwebtoken.*;
		import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mi.mi_clush.global.security.exception.JwtExpirationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static mi.mi_clush.global.enums.ErrorCode.JWT_EXPIRATION;

@Slf4j(topic = "jwtUtil 관련 로그")
@Component
public class JwtUtil {

	public static final String AUTHORIZATION_HEADER = "Authorization";   // Header의 KEY 값
	private final String tokenPrefix = "Bearer "; // Token 식별자
	private final long tokenTime;
	private final Key key;

	// JwtUtil 생성자
	public JwtUtil(
			@Value("${jwt.token.expiration.time}") long tokenTime,
			@Value("${jwt.secret.key}") String secretKey) {

		this.tokenTime = tokenTime;
		byte[] bytes = Base64.getDecoder().decode(secretKey);  // 비밀 키를 Base64로 디코딩
		this.key = new SecretKeySpec(bytes, SignatureAlgorithm.HS256.getJcaName());  // HMAC SHA Key 생성
	}
	// jwt 토큰 생성
	public String createToken(String userEmail) {
		Date date = new Date();
		return tokenPrefix
				+ Jwts.builder()
				.setSubject(userEmail)  // 사용자 고유 정보 설정
				.setExpiration(new Date(date.getTime() + tokenTime))  // 토큰 만료 시간 설정
				.setIssuedAt(date)  // 토큰 발급 시간 설정
				.signWith(SignatureAlgorithm.HS256, key)  // 서명에 사용할 키와 알고리즘 명시
				.compact();  // JWT 토큰 생성
	}

	// jwt 토큰이 "Bearer "로 시작하는지 확인
	public boolean isIllegalPrefix(String tokenValue) {
		return !tokenValue.startsWith(tokenPrefix);
	}

	// jwt 토큰에서 "Bearer "를 제외한 부분을 추출
	public String substringToken(String tokenValue) {
		return tokenValue.substring(7);  // "Bearer " 제거 후 반환
	}

	// jwt 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.setSigningKey(key.getEncoded())  // Key를 byte[]로 변환하여 설정
					.parseClaimsJws(token);  // key로 token 검증;  // key로 token 검증
			return true;    // 유효한 경우 true 반환
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("유효하지 않은 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.error("토큰이 만료되었습니다.");
			throw new JwtExpirationException(JWT_EXPIRATION);  // 만료된 토큰 예외 처리
		} catch (UnsupportedJwtException e) {
			log.error("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.error("잘못된 JWT 토큰입니다.");
		}
		return false;  // 예외가 발생한 경우 false 반환
	}

	// jwt 토큰에서 클레임(Claims) 정보 추출
	public Claims getClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(key.getEncoded())  // Key를 byte[]로 변환하여 설정
				.parseClaimsJws(token)  // JWT 파싱
				.getBody();  // 클레임 정보 반환
	}

	// 생성된 jwt 토큰을 HTTP 응답 헤더에 추가
	public void addTokenToHeader(String token, HttpServletResponse response) {
		response.setHeader(AUTHORIZATION_HEADER, token);  // Authorization 헤더에 토큰 추가
	}
}
