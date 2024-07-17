package com.teamChallenge.entity.figure.sections;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubCategory {

    //Constructor sub categories
    CITIES(Category.CONSTRUCTORS),
    SPACE_STATIONS(Category.CONSTRUCTORS),
    ROBOFARMS(Category.CONSTRUCTORS),
    SCIENCE_LABS(Category.CONSTRUCTORS),

    //Dolls and Robots sub categories
    ROBOTS(Category.DOLLS_AND_ROBOTS),
    DOLLS(Category.DOLLS_AND_ROBOTS),

    //Vehicle sub categories
    SPORT_CARS(Category.VEHICLE),
    FLYING_CARS(Category.VEHICLE),
    AIRPLANES(Category.VEHICLE),
    DRONES(Category.VEHICLE),
    INTERGALACTIC_SHIPS(Category.VEHICLE),
    TRAINS(Category.VEHICLE),

    //Weapon sub categories
    HANDGUNS(Category.WEAPON),
    SWORDS(Category.WEAPON),
    DAGGERS(Category.WEAPON),
    BOW_AND_ARROWS(Category.WEAPON);

    private final Category category;
}
