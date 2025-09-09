package br.com.luzdosbichos.repository.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.TypeAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {
    @Query("""
    SELECT a FROM Animal a
    WHERE (:name IS NULL OR a.name LIKE %:name%)
      AND (:typeAnimal IS NULL OR a.typeAnimal = :typeAnimal)
      AND (:gender IS NULL OR a.gender = :gender)
      AND (:size IS NULL OR a.size = :size)
      AND (:color IS NULL OR a.color LIKE %:color%)
      AND (:age IS NULL OR a.age = :age)
""")
    List<Animal> filterAnimals(
            @Param("name") String name,
            @Param("typeAnimal") TypeAnimal typeAnimal,
            @Param("gender") Gender gender,
            @Param("size") Size size,
            @Param("color") String color,
            @Param("age") Integer age
    );
}
