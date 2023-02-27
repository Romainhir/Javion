package ch.epfl.javions.aircraft;

public record AircraftData(AircraftRegistration registration,
                           AircraftTypeDesignator typeDesignator,
                           String model,
                           AircraftDescription description,
                           WakeTurbulenceCategory wakeTurbulenceCategory) {
    public AircraftData {
        if (registration == null || typeDesignator == null ||
                model == null || description == null || wakeTurbulenceCategory == null) {
            throw new NullPointerException("One or more of the arguments is null");
        }
    }
}
