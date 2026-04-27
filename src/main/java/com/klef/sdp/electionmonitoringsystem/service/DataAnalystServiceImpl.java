package com.klef.sdp.electionmonitoringsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.electionmonitoringsystem.entity.DataAnalyst;
import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;
import com.klef.sdp.electionmonitoringsystem.repository.DataAnalystRepository;
import com.klef.sdp.electionmonitoringsystem.repository.PollingStationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class DataAnalystServiceImpl implements DataAnalystService
{
    @Autowired
    private DataAnalystRepository dataAnalystRepository;

    @Autowired
    private PollingStationRepository pollingStationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerDataAnalyst(DataAnalyst dataAnalyst)
    {
        if (dataAnalystRepository.existsById(dataAnalyst.getEmail()))
        {
            return "Data analyst already exists";
        }
        dataAnalyst.setRole("DATA_ANALYST");
        dataAnalyst.setPassword(passwordEncoder.encode(dataAnalyst.getPassword()));
        dataAnalystRepository.save(dataAnalyst);
        return "Data analyst registered successfully";
    }

    @Override
    public DataAnalyst verifyDataAnalystLogin(String email, String password)
    {
        Optional<DataAnalyst> optional = dataAnalystRepository.findById(email);
        if (optional.isPresent()) {
            DataAnalyst analyst = optional.get();
            if (passwordEncoder.matches(password, analyst.getPassword())) {
                return analyst;
            }
        }
        return null;
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
    public String assignDistrict(String email, String district)
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

    @Override
    public List<PollingStation> getPollingStationsByDistrict(String district)
    {
        return pollingStationRepository.findByDistrict(district);
    }

    @Override
    public List<PollingStation> getAllPollingStations()
    {
        return pollingStationRepository.findAll();
    }
    @Override
    public DataAnalyst updateDataAnalystProfile(DataAnalyst analyst) {
        Optional<DataAnalyst> optional = dataAnalystRepository.findById(analyst.getEmail());

        if (optional.isEmpty()) {
            return null;
        }

        DataAnalyst existing = optional.get();
        if (analyst.getPassword() != null && !analyst.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(analyst.getPassword()));
        }

        return dataAnalystRepository.save(existing);
    }
}
