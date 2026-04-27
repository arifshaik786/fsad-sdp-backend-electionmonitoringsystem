package com.klef.sdp.electionmonitoringsystem.service;

import com.klef.sdp.electionmonitoringsystem.entity.RefreshToken;
import com.klef.sdp.electionmonitoringsystem.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username, String role) {
        // Optionally, revoke existing token for this user/role
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsernameAndRole(username, role);
        if (existingToken.isPresent()) {
            RefreshToken token = existingToken.get();
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRole(role);
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setTokenHash(UUID.randomUUID().toString()); // Use UUID as token for simplicity
        refreshToken.setRevoked(false);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenRepository.findByTokenHash(tokenHash);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked.");
        }
        return token;
    }

    public void revokeToken(String tokenHash) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByTokenHash(tokenHash);
        if (tokenOpt.isPresent()) {
            RefreshToken token = tokenOpt.get();
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }
}
