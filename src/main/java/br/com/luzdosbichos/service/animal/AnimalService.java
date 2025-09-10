package br.com.luzdosbichos.service.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.TypeAnimal;
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
        return animalRepository.findById(id);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public void deleteAnimal(Integer id) {
        animalRepository.deleteById(id);
    }

    public List<Animal> getAnimalsWithFilter(
            String name,
            TypeAnimal typeAnimal,
            Gender gender,
            Size size,
            String color,
            Integer age) {
        return animalRepository.filterAnimals(name, typeAnimal, gender, size, color, age);
    }

    public Animal updateAnimal(Integer id, Animal animal) {
        Animal animalExists = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        animalExists.setName(animal.getName());
        animalExists.setTypeAnimal(animal.getTypeAnimal());
        animalExists.setGender(animal.getGender());
        animalExists.setColor(animal.getColor());
        animalExists.setAge(animal.getAge());
        animalExists.setSize(animal.getSize());
        animalExists.setStory(animal.getStory());
        animalExists.setCastrated(animal.getCastrated());
        animalExists.setVeterinary_care(animal.getVeterinary_care());
        animalExists.setNeutered_spayed(animal.getNeutered_spayed());
        animalExists.setDewormed(animal.getDewormed());
        animalExists.setMicrochipped(animal.getMicrochipped());
        animalExists.setSpecial_needs(animal.getSpecial_needs());
        animalExists.setDocile(animal.getDocile());
        animalExists.setAggressive(animal.getAggressive());
        animalExists.setPlayful(animal.getPlayful());
        animalExists.setCalm(animal.getCalm());
        animalExists.setSociable(animal.getSociable());
        animalExists.setShy(animal.getShy());
        animalExists.setIndependent(animal.getIndependent());
        animalExists.setNeedy(animal.getNeedy());
        animalExists.setGood_with_children(animal.getGood_with_children());
        animalExists.setGood_with_cats(animal.getGood_with_cats());
        animalExists.setGood_with_dogs(animal.getGood_with_dogs());
        animalExists.setGood_with_strangers(animal.getGood_with_strangers());

        if(animal.getPhotoUrl() != null) {
            animalExists.setPhotoUrl(animal.getPhotoUrl());
        }

        return animalRepository.save(animalExists);
    }

    public Animal markAsAdopted(Integer id, Boolean adopted) {
        Animal animalExists = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        animalExists.setAdopted(adopted);

        return animalRepository.save(animalExists);
    }

    public List<Animal> getAvailableAnimals() {
        return animalRepository.findAll().
                stream().
                filter(a -> Boolean.FALSE.equals(a.getAdopted())).
                toList();
    }

    public List<Animal> getAdoptedAnimals() {
        return animalRepository.findAll()
                .stream()
                .filter(a -> Boolean.TRUE.equals(a.getAdopted()))
                .toList();
    }
}