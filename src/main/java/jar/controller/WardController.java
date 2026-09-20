package jar.controller;

import jar.entity.Ward;
import jar.repository.WardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wards")
public class WardController {

    private final WardRepository wardRepository;

    public WardController(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    // View all wards
    @GetMapping
    public List<Ward> getAllWards() {
        return wardRepository.findAll();
    }

    // Add new ward
    @PostMapping
    public Ward addWard(@RequestBody Ward ward) {
        return wardRepository.save(ward);
    }

    // Edit ward
@PutMapping("/{id}")
public ResponseEntity<Ward> updateWard(
        @PathVariable Long id,
        @RequestBody Ward wardDetails) {

    return wardRepository.findById(id)
            .map(ward -> {
                ward.setWardName(wardDetails.getWardName());
                ward.setWardType(wardDetails.getWardType());
                ward.setTotalBeds(wardDetails.getTotalBeds());

                Ward updatedWard = wardRepository.save(ward);

                return ResponseEntity.ok(updatedWard);
            })
            .orElse(ResponseEntity.notFound().build());
}

    // Delete ward
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWard(@PathVariable Long id) {

        if (!wardRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        wardRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}