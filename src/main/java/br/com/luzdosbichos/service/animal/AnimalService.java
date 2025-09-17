package br.com.luzdosbichos.service.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.Type;
import br.com.luzdosbichos.model.animal.req.AnimalRequestBody;
import br.com.luzdosbichos.repository.animal.AnimalRepository;
import br.com.luzdosbichos.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final StorageService storageService;

    public Animal saveAnimal(AnimalRequestBody req) throws RuntimeException {
        Animal animal = Animal.builder()
                .name(req.getAnimal().getName())
                .type(req.getAnimal().getType())
                .gender(req.getAnimal().getGender())
                .color(req.getAnimal().getColor())
                .age(req.getAnimal().getAge())
                .size(req.getAnimal().getSize())
                .story(req.getAnimal().getStory())
                .castrated(req.getAnimal().getCastrated())
                .veterinaryCare(req.getAnimal().getVeterinaryCare())
                .neuteredSpayed(req.getAnimal().getNeuteredSpayed())
                .dewormed(req.getAnimal().getDewormed())
                .microchipped(req.getAnimal().getMicrochipped())
                .specialNeeds(req.getAnimal().getSpecialNeeds())
                .docile(req.getAnimal().getDocile())
                .aggressive(req.getAnimal().getAggressive())
                .playful(req.getAnimal().getPlayful())
                .calm(req.getAnimal().getCalm())
                .sociable(req.getAnimal().getSociable())
                .shy(req.getAnimal().getShy())
                .independent(req.getAnimal().getIndependent())
                .needy(req.getAnimal().getNeedy())
                .goodWithChildren(req.getAnimal().getGoodWithChildren())
                .goodWithCats(req.getAnimal().getGoodWithCats())
                .goodWithDogs(req.getAnimal().getGoodWithDogs())
                .goodWithStrangers(req.getAnimal().getGoodWithStrangers())
                .adopted(req.getAnimal().getAdopted())
                .build();

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            animal.setPhotoUrl(storageService.upload(req.getImage()));
        }

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

    public Page<Animal> getAnimalsWithFilter(
            Integer page,
            Integer pageSize,
            String name,
            Type type,
            Gender gender,
            Size size,
            String color,
            Integer age) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        return animalRepository.filterAnimals(name, type, gender, size, color, age, pageable);
    }

    public Animal updateAnimal(Integer id, AnimalRequestBody req) {
        Animal existingAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        existingAnimal.setName(req.getAnimal().getName());
        existingAnimal.setType(req.getAnimal().getType());
        existingAnimal.setGender(req.getAnimal().getGender());
        existingAnimal.setColor(req.getAnimal().getColor());
        existingAnimal.setAge(req.getAnimal().getAge());
        existingAnimal.setSize(req.getAnimal().getSize());
        existingAnimal.setStory(req.getAnimal().getStory());
        existingAnimal.setCastrated(req.getAnimal().getCastrated());
        existingAnimal.setVeterinaryCare(req.getAnimal().getVeterinaryCare());
        existingAnimal.setNeuteredSpayed(req.getAnimal().getNeuteredSpayed());
        existingAnimal.setDewormed(req.getAnimal().getDewormed());
        existingAnimal.setMicrochipped(req.getAnimal().getMicrochipped());
        existingAnimal.setSpecialNeeds(req.getAnimal().getSpecialNeeds());
        existingAnimal.setDocile(req.getAnimal().getDocile());
        existingAnimal.setAggressive(req.getAnimal().getAggressive());
        existingAnimal.setPlayful(req.getAnimal().getPlayful());
        existingAnimal.setCalm(req.getAnimal().getCalm());
        existingAnimal.setSociable(req.getAnimal().getSociable());
        existingAnimal.setShy(req.getAnimal().getShy());
        existingAnimal.setIndependent(req.getAnimal().getIndependent());
        existingAnimal.setNeedy(req.getAnimal().getNeedy());
        existingAnimal.setGoodWithChildren(req.getAnimal().getGoodWithChildren());
        existingAnimal.setGoodWithCats(req.getAnimal().getGoodWithCats());
        existingAnimal.setGoodWithDogs(req.getAnimal().getGoodWithDogs());
        existingAnimal.setGoodWithStrangers(req.getAnimal().getGoodWithStrangers());

        if(req.getImage() != null && !req.getImage().isEmpty()) {
            existingAnimal.setPhotoUrl(storageService.upload(req.getImage()));
        }

        return animalRepository.save(existingAnimal);
    }

    public Animal markAsAdopted(Integer id, Boolean adopted) {
        Animal animalExists = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        animalExists.setAdopted(adopted);

        return animalRepository.save(animalExists);
    }

    public Page<Animal> getByAdoptedStatus(Integer page, Integer size, Boolean adopted) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return animalRepository.findByAdoptedStatus(adopted, pageable);
    }
}