package br.com.luzdosbichos.api.controller.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.model.animal.AnimalBase;
import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.Type;
import br.com.luzdosbichos.model.animal.req.AnimalRequestBody;
import br.com.luzdosbichos.service.animal.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/animal")
public class AnimalController {

    // TODO: implementar paginação em todos os endpoints que retornam listas

    private final AnimalService animalService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Register a new animal",
            description = "Creates a new animal record. An image can be uploaded together, " +
            "which will be stored in Oracle Cloud and its URL will be saved in the database."
    )
    public ResponseEntity<?> saveAnimal(
            @RequestPart("animal") AnimalBase animal,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            AnimalRequestBody req = new AnimalRequestBody();
            req.setAnimal(animal);
            req.setImage(image);
            return ResponseEntity.ok(animalService.saveAnimal(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get animal by ID",
            description = "Returns the details of a specific animal based on its ID."
    )
    public ResponseEntity<Animal> getAnimalById(@PathVariable Integer id) {
        return animalService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
            summary = "List all animals",
            description = "Returns the list of all registered animals, " +
                    "including both available and already adopted ones."
    )
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete animal",
            description = "Permanently deletes an animal from the database."
    )
    public void deleteAnimal(@PathVariable Integer id) {
        animalService.deleteAnimal(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update animal",
            description = "Updates the details of an existing animal. If a new image is provided, " +
                    "it will replace the previous one in Oracle Cloud."
    )
    public ResponseEntity<?> updateAnimal(
            @PathVariable Integer id,
            @RequestPart("animal") AnimalBase animal,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {

            AnimalRequestBody req = new AnimalRequestBody();
            req.setAnimal(animal);
            req.setImage(image);

            return ResponseEntity.ok(animalService.updateAnimal(id, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Get animals with filters",
            description = "Returns a filtered list of animals based on the given parameters. " +
                    "All parameters are optional and can be combined."
    )
    public ResponseEntity<List<Animal>> getAnimalsWithFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Size size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer age) {
        return ResponseEntity.ok(
                animalService.getAnimalsWithFilter(name, type, gender, size, color, age));
    }

    @PatchMapping("/{id}/adopted")
    @Operation(
            summary = "Mark animal as adopted",
            description = "Updates the adoption status of an animal. " +
                    "If `adopted=true`, the animal will be moved to the adopted list. " +
                    "If `adopted=false`, it will return to the available list."
    )
    public ResponseEntity<Animal> markAsAdopted(@PathVariable Integer id,
                                                @RequestParam Boolean adopted) {
        return ResponseEntity.ok(animalService.markAsAdopted(id, adopted));
    }

    @Operation(
            summary = "Get available animals",
            description = "Returns the list of animals that are still available for adoption."
    )
    @GetMapping("/available")
    public ResponseEntity<List<Animal>> getAvailableAnimals() {
        return ResponseEntity.ok(animalService.getAvailableAnimals());
    }

    @Operation(
            summary = "Get adopted animals",
            description = "Returns the list of animals that have already been adopted."
    )
    @GetMapping("/adopted")
    public ResponseEntity<List<Animal>> getAdoptedAnimals() {
        return ResponseEntity.ok(animalService.getAdoptedAnimals());
    }
}