package br.com.luzdosbichos.model.animal;

import br.com.luzdosbichos.model.animal.enums.Gender;
import br.com.luzdosbichos.model.animal.enums.Size;
import br.com.luzdosbichos.model.animal.enums.TypeAnimal;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAnimal typeAnimal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String story;

    @Column(nullable = true)
    private Boolean castrated;

    @Column(nullable = true)
    private Boolean veterinary_care;

    @Column(nullable = true)
    private Boolean neutered_spayed;

    @Column(nullable = true)
    private Boolean dewormed;

    @Column(nullable = true)
    private Boolean microchipped;

    @Column(nullable = true)
    private Boolean special_needs;

    @Column(nullable = true)
    private Boolean docile;

    @Column(nullable = true)
    private Boolean aggressive;

    @Column(nullable = true)
    private Boolean playful;

    @Column(nullable = true)
    private Boolean calm;

    @Column(nullable = true)
    private Boolean sociable;

    @Column(nullable = true)
    private Boolean shy;

    @Column(nullable = true)
    private Boolean independent;

    @Column(nullable = true)
    private Boolean needy;

    @Column(nullable = true)
    private Boolean good_with_children;

    @Column(nullable = true)
    private Boolean good_with_cats;

    @Column(nullable = true)
    private Boolean good_with_dogs;

    @Column(nullable = true)
    private Boolean good_with_strangers;

    @Column(nullable = true)
    private String photoUrl;

    @Column(nullable = true)
    private Boolean adopted = false;
}