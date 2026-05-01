package com.klef.sdp.electionmonitoringsystem.controller;

import com.klef.sdp.electionmonitoringsystem.entity.RefreshToken;
import com.klef.sdp.electionmonitoringsystem.service.RefreshTokenService;
import com.klef.sdp.electionmonitoringsystem.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "https://humble-fulfillment-production-f04f.up.railway.app"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenString = getRefreshTokenFromCookies(request);

        if (refreshTokenString == null || refreshTokenString.isEmpty()) {
            return ResponseEntity.status(401).body("Refresh Token is empty!");
        }

        try {
            Optional<RefreshToken> tokenOpt = refreshTokenService.findByTokenHash(refreshTokenString);
            if (tokenOpt.isPresent()) {
                RefreshToken token = tokenOpt.get();
                refreshTokenService.verifyExpiration(token);

                // Token is valid, rotate it (create a new one and revoke the old)
                RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(token.getUsername(), token.getRole());
                
                // Set the new refresh token in cookie
                Cookie cookie = new Cookie("refreshToken", newRefreshToken.getTokenHash());
                cookie.setHttpOnly(true);
                cookie.setSecure(false); // Make true if using HTTPS
                cookie.setPath("/auth/");
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                response.addCookie(cookie);

                // Generate new access token
                String newAccessToken = jwtUtil.generateToken(token.getUsername(), token.getRole());

                Map<String, String> res = new HashMap<>();
                res.put("accessToken", newAccessToken);

                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.status(401).body("Refresh token is not in database!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenString = getRefreshTokenFromCookies(request);

        if (refreshTokenString != null && !refreshTokenString.isEmpty()) {
            refreshTokenService.revokeToken(refreshTokenString);
        }

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/auth/");
        cookie.setMaxAge(0); // Delete cookie
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
