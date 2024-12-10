package salen.loginService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import salen.loginService.entity.dto.UserDto;
import salen.loginService.entity.dto.UserMapper;
import salen.loginService.service.UserService;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping("/login")
    public String verify(@RequestBody UserDto userDto) {
        return userService.verify(mapper.toEntity(userDto));
    }
}

