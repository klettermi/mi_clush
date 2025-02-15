package mi.mi_clush.domain.user.controller;

import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.user.dto.UserReqDto;
import mi.mi_clush.domain.user.service.UserService;
import mi.mi_clush.global.dto.ApiResponse;
import mi.mi_clush.global.utils.ResponseUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody UserReqDto request) {
        return ResponseUtils.success(userService.register(request));
    }
}
