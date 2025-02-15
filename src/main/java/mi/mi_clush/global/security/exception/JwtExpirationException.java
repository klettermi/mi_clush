package mi.mi_clush.global.security.exception;


import mi.mi_clush.global.enums.ErrorCode;
import mi.mi_clush.global.exception.GlobalException;

public class JwtExpirationException extends GlobalException {
	public JwtExpirationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
