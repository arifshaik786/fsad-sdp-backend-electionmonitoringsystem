package com.klef.sdp.electionmonitoringsystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.sdp.electionmonitoringsystem.entity.Admin;
import com.klef.sdp.electionmonitoringsystem.entity.DataAnalyst;
import com.klef.sdp.electionmonitoringsystem.entity.ElectionObserver;
import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;
import com.klef.sdp.electionmonitoringsystem.entity.RefreshToken;
import com.klef.sdp.electionmonitoringsystem.service.AdminService;
import com.klef.sdp.electionmonitoringsystem.service.RefreshTokenService;
import com.klef.sdp.electionmonitoringsystem.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/adminapi")
@CrossOrigin("*")
public class AdminController
{
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/")
    public String home()
    {
        return "Admin API is running";
    }

    // ─────────────────────────────────────────────────────────────
    // Admin Auth
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin)
    {
        try
        {
            if (admin == null || admin.getEmail() == null || admin.getPassword() == null)
            {
                return ResponseEntity.badRequest().body("Email and password are required");
            }

            admin.setEmail(admin.getEmail().trim().toLowerCase());
            admin.setPassword(admin.getPassword().trim());

            String message = adminService.registerAdmin(admin);
            if ("Admin already exists".equals(message))
            {
                return ResponseEntity.status(409).body(message);
            }
            return ResponseEntity.status(201).body(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin, HttpServletResponse httpResponse)
    {
        try
        {
            if (admin == null || admin.getEmail() == null || admin.getPassword() == null)
            {
                return ResponseEntity.badRequest().body("Email and password are required");
            }

            String email = admin.getEmail().trim().toLowerCase();
            String password = admin.getPassword().trim();

            Admin authenticatedAdmin = adminService.verifyAdminLogin(email, password);
            if (authenticatedAdmin == null)
            {
                return ResponseEntity.status(401).body("Invalid email or password");
            }

            String token = jwtUtil.generateToken(email, "admin");
            
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(email, "admin");
            
            Cookie cookie = new Cookie("refreshToken", refreshToken.getTokenHash());
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // set to true if using https
            cookie.setPath("/auth/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            httpResponse.addCookie(cookie);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Polling Stations
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/polling-station/add")
    public ResponseEntity<?> addPollingStation(@RequestBody PollingStation station)
    {
        try
        {
            if (station == null
                    || station.getStationName() == null
                    || station.getLocation() == null
                    || station.getDistrict() == null)
            {
                return ResponseEntity.badRequest().body("Station name, location, and district are required");
            }

            station.setStationName(station.getStationName().trim());
            station.setLocation(station.getLocation().trim());
            station.setDistrict(station.getDistrict().trim());

            if (station.getState() != null)
                station.setState(station.getState().trim());
            if (station.getAddress() != null)
                station.setAddress(station.getAddress().trim());

            String message = adminService.addPollingStation(station);
            return ResponseEntity.status(201).body(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/polling-station/all")
    public ResponseEntity<?> getAllPollingStations()
    {
        try
        {
            List<PollingStation> stations = adminService.getAllPollingStations();
            return ResponseEntity.ok(stations);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/polling-station/getbyid")
    public ResponseEntity<?> getPollingStationById(@RequestParam Long id)
    {
        try
        {
            PollingStation station = adminService.getPollingStationById(id);
            if (station == null)
            {
                return ResponseEntity.status(404).body("Polling station not found");
            }
            return ResponseEntity.ok(station);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/polling-station/getbydistrict")
    public ResponseEntity<?> getPollingStationsByDistrict(@RequestParam String district)
    {
        try
        {
            List<PollingStation> stations = adminService.getPollingStationsByDistrict(district.trim());
            return ResponseEntity.ok(stations);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @DeleteMapping("/polling-station/delete")
    public ResponseEntity<?> deletePollingStation(@RequestParam Long id)
    {
        try
        {
            String message = adminService.deletePollingStation(id);
            if ("Polling station not found".equals(message))
            {
                return ResponseEntity.status(404).body(message);
            }
            return ResponseEntity.ok(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Data Analysts
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/analyst/add")
    public ResponseEntity<?> addDataAnalyst(@RequestBody DataAnalyst dataAnalyst)
    {
        try
        {
            if (dataAnalyst == null || dataAnalyst.getEmail() == null)
            {
                return ResponseEntity.badRequest().body("Email is required");
            }

            // Removed default password assignment

            dataAnalyst.setEmail(dataAnalyst.getEmail().trim().toLowerCase());
            if (dataAnalyst.getPassword() != null)
                dataAnalyst.setPassword(dataAnalyst.getPassword().trim());

            if (dataAnalyst.getAnalystName() != null)
                dataAnalyst.setAnalystName(dataAnalyst.getAnalystName().trim());
            if (dataAnalyst.getPhone() != null)
                dataAnalyst.setPhone(dataAnalyst.getPhone().trim());
            if (dataAnalyst.getExpertise() != null)
                dataAnalyst.setExpertise(dataAnalyst.getExpertise().trim());
            if (dataAnalyst.getStatus() == null)
                dataAnalyst.setStatus("active");

            DataAnalyst saved = adminService.addDataAnalyst(dataAnalyst);
            if (saved == null)
            {
                return ResponseEntity.status(409).body("Data analyst already exists");
            }
            return ResponseEntity.status(201).body(saved);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/analyst/all")
    public ResponseEntity<?> getAllDataAnalysts()
    {
        try
        {
            List<DataAnalyst> analysts = adminService.getAllDataAnalysts();
            return ResponseEntity.ok(analysts);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/analyst/getbyemail")
    public ResponseEntity<?> getDataAnalystByEmail(@RequestParam(required = false) String email)
    {
        try
        {
            if (email == null || email.trim().isEmpty())
            {
                return ResponseEntity.badRequest().body("Email is required");
            }
            DataAnalyst analyst = adminService.getDataAnalystByEmail(email.trim().toLowerCase());
            if (analyst == null)
            {
                return ResponseEntity.status(404).body("Data analyst not found");
            }
            return ResponseEntity.ok(analyst);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @DeleteMapping("/analyst/delete")
    public ResponseEntity<?> deleteDataAnalyst(@RequestParam(required = false) String email)
    {
        try
        {
            if (email == null || email.trim().isEmpty())
            {
                return ResponseEntity.badRequest().body("Email is required");
            }
            String message = adminService.deleteDataAnalyst(email.trim().toLowerCase());
            if ("Data analyst not found".equals(message))
            {
                return ResponseEntity.status(404).body(message);
            }
            return ResponseEntity.ok(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @DeleteMapping("/analyst/delete/{email}")
    public ResponseEntity<?> deleteDataAnalystPath(@PathVariable String email)
    {
        return deleteDataAnalyst(email);
    }

    @PutMapping("/analyst/assign-district")
    public ResponseEntity<?> assignDistrictToAnalyst(@RequestBody Map<String, String> body)
    {
        try
        {
            String email = body.get("email");
            String district = body.get("district");

            if (email == null || district == null)
            {
                return ResponseEntity.badRequest().body("Email and district are required");
            }

            String message = adminService.assignDistrictToAnalyst(
                    email.trim().toLowerCase(), district.trim());

            if ("Data analyst not found".equals(message))
            {
                return ResponseEntity.status(404).body(message);
            }
            return ResponseEntity.ok(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Election Observers
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/observer/add")
    public ResponseEntity<?> addElectionObserver(@RequestBody ElectionObserver electionObserver)
    {
        try
        {
            if (electionObserver == null || electionObserver.getEmail() == null)
            {
                return ResponseEntity.badRequest().body("Email is required");
            }

            // Removed default password assignment

            electionObserver.setEmail(electionObserver.getEmail().trim().toLowerCase());
            if (electionObserver.getPassword() != null)
                electionObserver.setPassword(electionObserver.getPassword().trim());

            if (electionObserver.getObserverName() != null)
                electionObserver.setObserverName(electionObserver.getObserverName().trim());
            if (electionObserver.getPhone() != null)
                electionObserver.setPhone(electionObserver.getPhone().trim());
            if (electionObserver.getDistrict() != null)
                electionObserver.setDistrict(electionObserver.getDistrict().trim());
            if (electionObserver.getStatus() == null)
                electionObserver.setStatus("active");

            ElectionObserver saved = adminService.addElectionObserver(electionObserver);
            if (saved == null)
            {
                return ResponseEntity.status(409).body("Election observer already exists");
            }
            return ResponseEntity.status(201).body(saved);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/observer/all")
    public ResponseEntity<?> getAllElectionObservers()
    {
        try
        {
            List<ElectionObserver> observers = adminService.getAllElectionObservers();
            return ResponseEntity.ok(observers);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/observer/getbyemail")
    public ResponseEntity<?> getElectionObserverByEmail(@RequestParam(required = false) String email)
    {
        try
        {
            if (email == null || email.trim().isEmpty())
            {
                return ResponseEntity.badRequest().body("Email is required");
            }
            ElectionObserver observer = adminService.getElectionObserverByEmail(
                    email.trim().toLowerCase());
            if (observer == null)
            {
                return ResponseEntity.status(404).body("Election observer not found");
            }
            return ResponseEntity.ok(observer);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @DeleteMapping("/observer/delete")
    public ResponseEntity<?> deleteElectionObserver(@RequestParam(required = false) String email)
    {
        try
        {
            if (email == null || email.trim().isEmpty())
            {
                return ResponseEntity.badRequest().body("Email is required");
            }
            String message = adminService.deleteElectionObserver(
                    email.trim().toLowerCase());
            if ("Election observer not found".equals(message))
            {
                return ResponseEntity.status(404).body(message);
            }
            return ResponseEntity.ok(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @DeleteMapping("/observer/delete/{email}")
    public ResponseEntity<?> deleteElectionObserverPath(@PathVariable String email)
    {
        return deleteElectionObserver(email);
    }

    @PutMapping("/observer/assign-station")
    public ResponseEntity<?> assignStationToObserver(@RequestBody Map<String, String> body)
    {
        try
        {
            String email = body.get("email");
            String assignedStation = body.get("assignedStation");
            if (assignedStation == null) assignedStation = body.get("stationName");

            if (email == null || assignedStation == null)
            {
                return ResponseEntity.badRequest().body("Email and assignedStation are required");
            }

            String message = adminService.assignStationToObserver(
                    email.trim().toLowerCase(), assignedStation.trim());

            if ("Election observer not found".equals(message))
            {
                return ResponseEntity.status(404).body(message);
            }
            return ResponseEntity.ok(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PutMapping("/observer/assign-district")
    public ResponseEntity<?> assignDistrictToObserver(@RequestBody Map<String, String> body)
    {
        try
        {
            String email = body.get("email");
            String district = body.get("district");

            if (email == null || district == null)
            {
                return ResponseEntity.badRequest().body("Email and district are required");
            }

            String message = adminService.assignDistrictToObserver(
                    email.trim().toLowerCase(), district.trim());

            if ("Election observer not found".equals(message))
            {
                return ResponseEntity.status(404).body(message);
            }
            return ResponseEntity.ok(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
