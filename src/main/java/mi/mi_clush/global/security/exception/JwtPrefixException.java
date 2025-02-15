package mi.mi_clush.global.security.exception;

import mi.mi_clush.global.enums.ErrorCode;
import mi.mi_clush.global.exception.GlobalException;

public class JwtPrefixException extends GlobalException {
	public JwtPrefixException(ErrorCode errorCode) {
		super(errorCode);
	}
}
