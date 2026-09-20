package jar.controller;

import jar.repository.BedRepository;
import jar.repository.PatientRepository;
import jar.repository.DoctorRepository;
import jar.repository.WardRepository;
import jar.repository.AdmissionRepository;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final BedRepository bedRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final WardRepository wardRepository;
    private final AdmissionRepository admissionRepository;

    public DashboardController(
            BedRepository bedRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            WardRepository wardRepository,
            AdmissionRepository admissionRepository) {

        this.bedRepository = bedRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.wardRepository = wardRepository;
        this.admissionRepository = admissionRepository;
    }

    @GetMapping("/stats")
    public Map<String, Long> getDashboardStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("totalPatients", patientRepository.count());
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalWards", wardRepository.count());
        stats.put("totalAdmissions", admissionRepository.count());
        stats.put("totalBeds", bedRepository.count());

        return stats;
    }
}