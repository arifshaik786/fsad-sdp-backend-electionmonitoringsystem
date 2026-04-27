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

import com.klef.sdp.electionmonitoringsystem.entity.DataAnalyst;
import com.klef.sdp.electionmonitoringsystem.entity.RefreshToken;
import com.klef.sdp.electionmonitoringsystem.service.DataAnalystService;
import com.klef.sdp.electionmonitoringsystem.service.RefreshTokenService;
import com.klef.sdp.electionmonitoringsystem.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/analystapi")
@CrossOrigin("*")
public class DataAnalystController {

	@Autowired
	private DataAnalystService dataAnalystService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RefreshTokenService refreshTokenService;
	@GetMapping("/")
	public String home() {
		return "Data Analyst API is running";
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerDataAnalyst(@RequestBody DataAnalyst analyst) {
		try {
			if (analyst == null || analyst.getEmail() == null || analyst.getPassword() == null) {
				return ResponseEntity.badRequest().body("Email and password are required");
			}
			String message = dataAnalystService.registerDataAnalyst(analyst);
			if (message.equals("Data analyst already exists")) {
				return ResponseEntity.status(409).body(message);
			}
			return ResponseEntity.status(201).body(message);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginDataAnalyst(@RequestBody DataAnalyst dataAnalyst, HttpServletResponse httpResponse) {
		try {
			if (dataAnalyst == null || dataAnalyst.getEmail() == null || dataAnalyst.getPassword() == null) {
				return ResponseEntity.badRequest().body("Email and password are required");
			}

			String email = dataAnalyst.getEmail().trim().toLowerCase();
			String password = dataAnalyst.getPassword().trim();

			DataAnalyst authenticatedDataAnalyst = dataAnalystService.verifyDataAnalystLogin(email, password);
			if (authenticatedDataAnalyst == null) {
				return ResponseEntity.status(401).body("Invalid email or password");
			}

			String token = jwtUtil.generateToken(email, "analyst");

			RefreshToken refreshToken = refreshTokenService.createRefreshToken(email, "analyst");
            
            Cookie cookie = new Cookie("refreshToken", refreshToken.getTokenHash());
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/auth/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            httpResponse.addCookie(cookie);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Login successful");
			response.put("token", token);
			response.put("user", authenticatedDataAnalyst);

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/profile")
	public ResponseEntity<?> getDataAnalystProfile(@RequestParam String email) {
		try {
			DataAnalyst analyst = dataAnalystService.getDataAnalystByEmail(email.trim().toLowerCase());
			if (analyst == null) {
				return ResponseEntity.status(404).body("Data analyst not found");
			}
			return ResponseEntity.ok(analyst);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/polling-stations/my-district")
	public ResponseEntity<?> getPollingStationsByDistrict(@RequestParam String district) {
		try {
			return ResponseEntity.ok(dataAnalystService.getPollingStationsByDistrict(district.trim()));
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/polling-stations/all")
	public ResponseEntity<?> getAllPollingStations() {
		try {
			return ResponseEntity.ok(dataAnalystService.getAllPollingStations());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}
	@PutMapping("/profile/update")
	public ResponseEntity<?> updateDataAnalystProfile(@RequestBody DataAnalyst analyst) {
	    try {
	        if (analyst == null || analyst.getEmail() == null || analyst.getEmail().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body("Email is required");
	        }

	        analyst.setEmail(analyst.getEmail().trim().toLowerCase());
	        DataAnalyst updated = dataAnalystService.updateDataAnalystProfile(analyst);

	        if (updated == null) {
	            return ResponseEntity.status(404).body("Data analyst not found");
	        }

	        return ResponseEntity.ok(updated);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Internal Server Error");
	    }
	}

}
