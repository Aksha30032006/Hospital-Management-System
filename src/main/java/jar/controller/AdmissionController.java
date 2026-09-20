package jar.controller;

import jar.entity.Admission;
import jar.entity.Bed;
import jar.repository.AdmissionRepository;
import jar.repository.BedRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private final AdmissionRepository admissionRepository;
    private final BedRepository bedRepository;

    public AdmissionController(
            AdmissionRepository admissionRepository,
            BedRepository bedRepository) {

        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
    }

    // Get all admissions
    @GetMapping
    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }

    // Add new admission
    @PostMapping
    @Transactional
    public Admission addAdmission(@RequestBody Admission admission) {

        Bed bed = bedRepository.findById(admission.getBedId())
                .orElseThrow(() ->
                        new RuntimeException("Bed not found"));

        if ("OCCUPIED".equals(bed.getStatus())) {
            throw new RuntimeException("This bed is already occupied");
        }

        if ("RESERVED".equals(bed.getStatus())) {
            throw new RuntimeException("This bed is reserved");
        }

        bed.setStatus("OCCUPIED");
        bedRepository.save(bed);

        admission.setStatus("ADMITTED");

        return admissionRepository.save(admission);
    }

    // Edit admission
    @PutMapping("/{id}")
    @Transactional
    public Admission updateAdmission(
            @PathVariable Long id,
            @RequestBody Admission updatedAdmission) {

        Admission existing = admissionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Admission not found"));

        if ("DISCHARGED".equals(existing.getStatus())) {
            throw new RuntimeException(
                    "Discharged admission cannot be edited");
        }

        Long oldBedId = existing.getBedId();
        Long newBedId = updatedAdmission.getBedId();

        // If bed is changed, validate the new bed
        if (!oldBedId.equals(newBedId)) {

            Bed newBed = bedRepository.findById(newBedId)
                    .orElseThrow(() ->
                            new RuntimeException("New bed not found"));

            if ("OCCUPIED".equals(newBed.getStatus())
                    || "RESERVED".equals(newBed.getStatus())) {
                throw new RuntimeException(
                        "Selected bed is not available");
            }

            // Release old bed
            Bed oldBed = bedRepository.findById(oldBedId)
                    .orElseThrow(() ->
                            new RuntimeException("Old bed not found"));

            oldBed.setStatus("AVAILABLE");
            bedRepository.save(oldBed);

            // Occupy new bed
            newBed.setStatus("OCCUPIED");
            bedRepository.save(newBed);

            existing.setBedId(newBedId);
        }

        existing.setPatientId(updatedAdmission.getPatientId());
        existing.setAdmissionDate(updatedAdmission.getAdmissionDate());

        return admissionRepository.save(existing);
    }

    // Discharge patient
    @PutMapping("/discharge/{id}")
    @Transactional
    public Admission dischargePatient(@PathVariable Long id) {

        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Admission not found"));

        if ("DISCHARGED".equals(admission.getStatus())) {
            throw new RuntimeException(
                    "Patient is already discharged");
        }

        admission.setStatus("DISCHARGED");
        admission.setDischargeDate(LocalDate.now().toString());

        Bed bed = bedRepository.findById(admission.getBedId())
                .orElseThrow(() ->
                        new RuntimeException("Bed not found"));

        bed.setStatus("AVAILABLE");
        bedRepository.save(bed);

        return admissionRepository.save(admission);
    }

    // Delete admission
    @DeleteMapping("/{id}")
    @Transactional
    public String deleteAdmission(@PathVariable Long id) {

        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Admission not found"));

        // Release bed only if patient is still admitted
        if ("ADMITTED".equals(admission.getStatus())) {

            Bed bed = bedRepository.findById(admission.getBedId())
                    .orElseThrow(() ->
                            new RuntimeException("Bed not found"));

            bed.setStatus("AVAILABLE");
            bedRepository.save(bed);
        }

        admissionRepository.delete(admission);

        return "Admission deleted successfully";
    }
}