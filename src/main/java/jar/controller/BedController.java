package jar.controller;

import jar.entity.Bed;
import jar.repository.BedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
public class BedController {

    private final BedRepository bedRepository;

    public BedController(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    // View all beds
    @GetMapping
    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }

    // Add new bed
    @PostMapping
    public Bed addBed(@RequestBody Bed bed) {
        return bedRepository.save(bed);
    }

    // Reserve bed
    @PutMapping("/reserve/{id}")
    public Bed reserveBed(@PathVariable Long id) {

        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bed not found"));

        bed.setStatus("RESERVED");

        return bedRepository.save(bed);
    }

    // Edit bed
    @PutMapping("/{id}")
    public ResponseEntity<Bed> updateBed(
            @PathVariable Long id,
            @RequestBody Bed bedDetails) {

        return bedRepository.findById(id)
                .map(bed -> {

                    bed.setBedNumber(bedDetails.getBedNumber());
                    bed.setWard(bedDetails.getWard());
                    bed.setStatus(bedDetails.getStatus());

                    Bed updatedBed = bedRepository.save(bed);

                    return ResponseEntity.ok(updatedBed);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete bed
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable Long id) {

        if (!bedRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        bedRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}