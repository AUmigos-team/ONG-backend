package br.com.luzdosbichos.model.animal;

import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.Type;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalBase {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    @Column(nullable = false, length = 1000)
    private String story;

    private Boolean castrated;
    private Boolean veterinaryCare;
    private Boolean neuteredSpayed;
    private Boolean dewormed;
    private Boolean microchipped;
    private Boolean specialNeeds;

    private Boolean docile;
    private Boolean aggressive;
    private Boolean playful;
    private Boolean calm;
    private Boolean sociable;
    private Boolean shy;
    private Boolean independent;
    private Boolean needy;

    private Boolean goodWithChildren;
    private Boolean goodWithCats;
    private Boolean goodWithDogs;
    private Boolean goodWithStrangers;

    @Builder.Default
    private Boolean adopted = false;
}

