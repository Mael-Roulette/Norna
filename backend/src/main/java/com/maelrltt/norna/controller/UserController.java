package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.auth.UserResponse;
import com.maelrltt.norna.dto.user.UpdateLastVisitedSpaceRequest;
import com.maelrltt.norna.dto.user.UpdatePreferredThemeRequest;
import com.maelrltt.norna.dto.user.UpdateUsernameRequest;
import com.maelrltt.norna.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/last-visited-space")
    public ResponseEntity<UserResponse> updateLastVisitedSpace(
            Authentication authentication,
            @RequestBody UpdateLastVisitedSpaceRequest request
    ) {
        return ResponseEntity.ok(userService.updateLastVisitedSpace(authentication, request));
    }

    @PatchMapping("/username")
    public ResponseEntity<UserResponse> updateUsername(
            Authentication authentication,
            @RequestBody UpdateUsernameRequest request
    ) {
        return ResponseEntity.ok(userService.updateUsername(authentication, request));
    }

    @PatchMapping("/preferred-theme")
    public ResponseEntity<UserResponse> updatePreferredTheme(
            Authentication authentication,
            @RequestBody UpdatePreferredThemeRequest request
    ) {
        return ResponseEntity.ok(userService.updatePreferredTheme(authentication, request));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(
            Authentication authentication
    ) {
        userService.deleteUser(authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
