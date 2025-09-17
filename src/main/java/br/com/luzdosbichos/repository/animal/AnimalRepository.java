package br.com.luzdosbichos.repository.animal;

import br.com.luzdosbichos.model.animal.Animal;
import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {
    @Query("""
        SELECT a FROM Animal a
        WHERE (:name IS NULL OR lower(a.name) LIKE lower(concat('%', :name, '%')))
          AND (:type IS NULL OR a.type = :type)
          AND (:gender IS NULL OR a.gender = :gender)
          AND (:size IS NULL OR a.size = :size)
          AND (:color IS NULL OR lower(a.color) LIKE lower(concat('%', :color, '%')))
          AND (:age IS NULL OR a.age = :age)
    """)
    Page<Animal> filterAnimals(
            @Param("name") String name,
            @Param("type") Type type,
            @Param("gender") Gender gender,
            @Param("size") Size size,
            @Param("color") String color,
            @Param("age") Integer age,
            Pageable pageable
    );

    @Query("select a from Animal a where a.adopted = :adopted")
    Page<Animal> findByAdoptedStatus(@Param("adopted") Boolean adopted, Pageable pageable);
}
