package com.klef.fsad.electionmonitoringsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.klef.fsad.electionmonitoringsystem.entity.Admin;
import com.klef.fsad.electionmonitoringsystem.entity.DataAnalyst;
import com.klef.fsad.electionmonitoringsystem.entity.ElectionObserver;
import com.klef.fsad.electionmonitoringsystem.entity.PollingStation;
import com.klef.fsad.electionmonitoringsystem.repository.AdminRepository;
import com.klef.fsad.electionmonitoringsystem.repository.DataAnalystRepository;
import com.klef.fsad.electionmonitoringsystem.repository.ElectionObserverRepository;
import com.klef.fsad.electionmonitoringsystem.repository.PollingStationRepository;

@Service
@Transactional
public class AdminServiceImpl implements AdminService
{
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DataAnalystRepository dataAnalystRepository;

    @Autowired
    private ElectionObserverRepository electionObserverRepository;

    @Autowired
    private PollingStationRepository pollingStationRepository;

    // ── Admin Auth ──────────────────────────────────────────────

    @Override
    public String registerAdmin(Admin admin)
    {
        if (adminRepository.existsById(admin.getEmail()))
        {
            return "Admin already exists";
        }
        admin.setRole("ADMIN");
        adminRepository.save(admin);
        return "Admin registered successfully";
    }

    @Override
    public Admin verifyAdminLogin(String email, String password)
    {
        return adminRepository.findByEmailAndPassword(email, password);
    }

    // ── Polling Stations ────────────────────────────────────────

    @Override
    public String addPollingStation(PollingStation station)
    {
        pollingStationRepository.save(station);
        return "Polling station added successfully";
    }

    @Override
    public List<PollingStation> getAllPollingStations()
    {
        return pollingStationRepository.findAll();
    }

    @Override
    public PollingStation getPollingStationById(Long id)
    {
        Optional<PollingStation> optional = pollingStationRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public String deletePollingStation(Long id)
    {
        if (!pollingStationRepository.existsById(id))
        {
            return "Polling station not found";
        }
        pollingStationRepository.deleteById(id);
        return "Polling station deleted successfully";
    }

    @Override
    public List<PollingStation> getPollingStationsByDistrict(String district)
    {
        return pollingStationRepository.findByDistrict(district);
    }

    // ── Data Analysts ───────────────────────────────────────────

    @Override
    public DataAnalyst addDataAnalyst(DataAnalyst dataAnalyst)
    {
        if (dataAnalystRepository.existsById(dataAnalyst.getEmail()))
        {
            return null;
        }
        dataAnalyst.setRole("DATA_ANALYST");
        return dataAnalystRepository.save(dataAnalyst);
    }

    @Override
    public List<DataAnalyst> getAllDataAnalysts()
    {
        return dataAnalystRepository.findAll();
    }

    @Override
    public DataAnalyst getDataAnalystByEmail(String email)
    {
        Optional<DataAnalyst> optional = dataAnalystRepository.findById(email);
        return optional.orElse(null);
    }

    @Override
    public String deleteDataAnalyst(String email)
    {
        if (!dataAnalystRepository.existsById(email))
        {
            return "Data analyst not found";
        }
        dataAnalystRepository.deleteById(email);
        return "Data analyst deleted successfully";
    }

    @Override
    public String assignDistrictToAnalyst(String email, String district)
    {
        Optional<DataAnalyst> optional = dataAnalystRepository.findById(email);
        if (optional.isEmpty())
        {
            return "Data analyst not found";
        }
        DataAnalyst analyst = optional.get();
        analyst.setAssignedDistrict(district);
        dataAnalystRepository.save(analyst);
        return "District assigned successfully";
    }

    // ── Election Observers ──────────────────────────────────────

    @Override
    public ElectionObserver addElectionObserver(ElectionObserver electionObserver)
    {
        if (electionObserverRepository.existsById(electionObserver.getEmail()))
        {
            return null;
        }
        electionObserver.setRole("ELECTION_OBSERVER");
        return electionObserverRepository.save(electionObserver);
    }

    @Override
    public List<ElectionObserver> getAllElectionObservers()
    {
        return electionObserverRepository.findAll();
    }

    @Override
    public ElectionObserver getElectionObserverByEmail(String email)
    {
        Optional<ElectionObserver> optional = electionObserverRepository.findById(email);
        return optional.orElse(null);
    }

    @Override
    public String deleteElectionObserver(String email)
    {
        if (!electionObserverRepository.existsById(email))
        {
            return "Election observer not found";
        }
        electionObserverRepository.deleteById(email);
        return "Election observer deleted successfully";
    }

    @Override
    public String assignStationToObserver(String email, String assignedStation)
    {
        Optional<ElectionObserver> optional = electionObserverRepository.findById(email);
        if (optional.isEmpty())
        {
            return "Election observer not found";
        }
        ElectionObserver observer = optional.get();
        observer.setAssignedStation(assignedStation);
        electionObserverRepository.save(observer);
        return "Station assigned successfully";
    }

    @Override
    public String assignDistrictToObserver(String email, String district)
    {
        Optional<ElectionObserver> optional = electionObserverRepository.findById(email);
        if (optional.isEmpty())
        {
            return "Election observer not found";
        }
        ElectionObserver observer = optional.get();
        observer.setDistrict(district);
        electionObserverRepository.save(observer);
        return "District assigned successfully";
    }
}
