package br.com.luzdosbichos.api.controller.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.service.animal.AnimalService;
import br.com.luzdosbichos.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;
    private final StorageService storageService;

    public ResponseEntity<Animal> saveAnimal(
            @RequestPart("animal") Animal animal,
            @RequestPart(value = "photo", required = false)MultipartFile photo) {

        if(photo != null && !photo.isEmpty()) {
            String storage = storageService.upload(photo);
        }

        return ResponseEntity.ok(animalService.saveAnimal(animal));
    }
}
