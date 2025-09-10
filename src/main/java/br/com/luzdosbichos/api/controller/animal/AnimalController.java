package br.com.luzdosbichos.api.controller.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.TypeAnimal;
import br.com.luzdosbichos.service.animal.AnimalService;
import br.com.luzdosbichos.service.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;
    private final StorageService storageService;

    @Value("${oci.objectstorage.namespace}")
    private String namespace;
    @Value("${oci.objectstorage.bucket}")
    private String bucket;

    @PostMapping
    @Operation(
            summary = "Register a new animal",
            description = "Creates a new animal record. An image can be uploaded together, " +
                    "which will be stored in Oracle Cloud and its URL will be saved in the database."
    )
    public ResponseEntity<Animal> saveAnimal(
            @RequestPart("animal") Animal animal,
            @RequestPart(value = "photo", required = false)MultipartFile photo) {

        if(photo != null && !photo.isEmpty()) {
            String objectName = storageService.upload(photo);
            String photoUrl = String.format(
                    "https://%s.compat.objectstorage.sa-saopaulo-1.oraclecloud.com/n/%s/b/%s/o/%s",
                    namespace, namespace, bucket, objectName
            );
            animal.setPhotoUrl(photoUrl);
        }

        return ResponseEntity.ok(animalService.saveAnimal(animal));
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

    @PutMapping("/{id}")
    @Operation(
            summary = "Update animal",
            description = "Updates the details of an existing animal. If a new image is provided, " +
                    "it will replace the previous one in Oracle Cloud."
    )
    public ResponseEntity<Animal> updateAnimal(
            @PathVariable Integer id,
            @RequestPart("animal") Animal animal,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        if(photo != null && !photo.isEmpty()) {
            String objectName = storageService.upload(photo);
            String photoUrl = String.format(
                    "https://%s.compat.objectstorage.sa-saopaulo-1.oraclecloud.com/n/%s/b/%s/o/%s",
                    namespace, namespace, bucket, objectName
            );
            animal.setPhotoUrl(photoUrl);
        }

        return ResponseEntity.ok(animalService.updateAnimal(id, animal));
    }

    @GetMapping("/{filter}")
    @Operation(
            summary = "Get animals with filters",
            description = "Returns a filtered list of animals based on the given parameters. " +
                    "All parameters are optional and can be combined."
    )
    public ResponseEntity<List<Animal>> getAnimalsWithFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) TypeAnimal typeAnimal,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Size size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer age) {

        return ResponseEntity.ok(
                animalService.getAnimalsWithFilter(name, typeAnimal, gender, size, color, age));
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