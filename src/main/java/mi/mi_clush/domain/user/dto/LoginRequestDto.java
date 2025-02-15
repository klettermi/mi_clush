package mi.mi_clush.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank(message = "이메일은 공백이 불가능합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 불가능합니다.")
    private String password;

}