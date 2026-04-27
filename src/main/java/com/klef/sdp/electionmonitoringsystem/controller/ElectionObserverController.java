package com.klef.sdp.electionmonitoringsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klef.sdp.electionmonitoringsystem.entity.ElectionObserver;
import com.klef.sdp.electionmonitoringsystem.entity.RefreshToken;
import com.klef.sdp.electionmonitoringsystem.service.ElectionObserverService;
import com.klef.sdp.electionmonitoringsystem.service.RefreshTokenService;
import com.klef.sdp.electionmonitoringsystem.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/observerapi")
@CrossOrigin("*")
public class ElectionObserverController {

	@Autowired
	private ElectionObserverService electionObserverService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RefreshTokenService refreshTokenService;
	@GetMapping("/")
	public String home() {
		return "Election Observer API is running";
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerElectionObserver(@RequestBody ElectionObserver observer) {
		try {
			if (observer == null || observer.getEmail() == null || observer.getPassword() == null) {
				return ResponseEntity.badRequest().body("Email and password are required");
			}
			String message = electionObserverService.registerElectionObserver(observer);
			if (message.equals("Election observer already exists")) {
				return ResponseEntity.status(409).body(message);
			}
			return ResponseEntity.status(201).body(message);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginElectionObserver(@RequestBody ElectionObserver electionObserver, HttpServletResponse httpResponse) {
		try {
			if (electionObserver == null || electionObserver.getEmail() == null || electionObserver.getPassword() == null) {
				return ResponseEntity.badRequest().body("Email and password are required");
			}

			String email = electionObserver.getEmail().trim().toLowerCase();
			String password = electionObserver.getPassword().trim();

			ElectionObserver authenticatedElectionObserver = electionObserverService.verifyElectionObserverLogin(email, password);
			if (authenticatedElectionObserver == null) {
				return ResponseEntity.status(401).body("Invalid email or password");
			}

			String token = jwtUtil.generateToken(email, "observer");

			RefreshToken refreshToken = refreshTokenService.createRefreshToken(email, "observer");
            
            Cookie cookie = new Cookie("refreshToken", refreshToken.getTokenHash());
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/auth/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            httpResponse.addCookie(cookie);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Login successful");
			response.put("token", token);
			response.put("user", authenticatedElectionObserver);

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/profile")
	public ResponseEntity<?> getElectionObserverProfile(@RequestParam String email) {
		try {
			ElectionObserver observer = electionObserverService.getElectionObserverByEmail(email.trim().toLowerCase());
			if (observer == null) {
				return ResponseEntity.status(404).body("Election observer not found");
			}
			return ResponseEntity.ok(observer);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/my-station")
	public ResponseEntity<?> getMyStation(@RequestParam String stationName) {
		try {
			return ResponseEntity.ok(electionObserverService.getPollingStationByName(stationName.trim()));
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/my-district-stations")
	public ResponseEntity<?> getPollingStationsByDistrict(@RequestParam String district) {
		try {
			return ResponseEntity.ok(electionObserverService.getPollingStationsByDistrict(district.trim()));
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}
	@PutMapping("/profile/update")
	public ResponseEntity<?> updateElectionObserverProfile(@RequestBody ElectionObserver observer) {
	    try {
	        if (observer == null || observer.getEmail() == null || observer.getEmail().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body("Email is required");
	        }

	        observer.setEmail(observer.getEmail().trim().toLowerCase());
	        ElectionObserver updated = electionObserverService.updateElectionObserverProfile(observer);

	        if (updated == null) {
	            return ResponseEntity.status(404).body("Election observer not found");
	        }

	        return ResponseEntity.ok(updated);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Internal Server Error");
	    }
	}
}
