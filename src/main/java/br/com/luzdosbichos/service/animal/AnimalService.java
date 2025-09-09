package br.com.luzdosbichos.service.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.repository.animal.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {
    private final AnimalRepository animalRepository;

    public Animal saveAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    public Optional<Animal> getById(Integer id) {
        return Optional.ofNullable(
                animalRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal not found"))
        );
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public void deleteAnimal(Integer id) {
        animalRepository.deleteById(id);
    }
}
