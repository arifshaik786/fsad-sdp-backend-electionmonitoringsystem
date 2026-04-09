package com.klef.fsad.electionmonitoringsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import com.klef.fsad.electionmonitoringsystem.entity.DataAnalyst;
import com.klef.fsad.electionmonitoringsystem.service.DataAnalystService;

@RestController
@RequestMapping("/analystapi")
@CrossOrigin("*")
public class DataAnalystController {

	@Autowired
	private DataAnalystService dataAnalystService;

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
	public ResponseEntity<?> loginDataAnalyst(@RequestBody DataAnalyst dataAnalyst) {
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

			return ResponseEntity.ok().body(authenticatedDataAnalyst);
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
