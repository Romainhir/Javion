package ch.epfl.javions.aircraft;

import java.util.Objects;

/**
 * Record that represent the data of an aircraft. We store the aircraft registration, the aircraft type designator,
 * the model, the description and the wake turbulence category of the aircraft.
 *
 * @param registration           (AircraftRegistration) : the aircraft registration
 * @param typeDesignator         (AircraftTypeDesignator) : the aircraft type designator
 * @param model                  (String) : the aircraft model
 * @param description            (AircraftDescription) : the aircraft description
 * @param wakeTurbulenceCategory (WakeTurbulenceCategory) : the aircraft wake turbulence category
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public record AircraftData(AircraftRegistration registration,
                           AircraftTypeDesignator typeDesignator,
                           String model,
                           AircraftDescription description,
                           WakeTurbulenceCategory wakeTurbulenceCategory) {

    /**
     * Constructor of the record.
     *
     * @param registration           (AircraftRegistration) : the aircraft registration
     * @param typeDesignator         (AircraftTypeDesignator) : the aircraft type designator
     * @param model                  (String) : the aircraft model
     * @param description            (AircraftDescription) : the aircraft description
     * @param wakeTurbulenceCategory (WakeTurbulenceCategory) : the aircraft wake turbulence category
     */
    public AircraftData {
        Objects.requireNonNull(registration);
        Objects.requireNonNull(typeDesignator);
        Objects.requireNonNull(model);
        Objects.requireNonNull(description);
        Objects.requireNonNull(wakeTurbulenceCategory);
    }
}
