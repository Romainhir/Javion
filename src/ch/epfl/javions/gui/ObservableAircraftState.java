package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.adsb.AircraftStateSetter;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public final class ObservableAircraftState implements AircraftStateSetter {
    private LongProperty lastMessageTimeStampNs;
    private IntegerProperty category;
    private ObjectProperty<CallSign> callSign;
    private ObjectProperty<GeoPos> position;
    private final ObservableList<AirbornePos> airbornePos = FXCollections.observableArrayList();
    private ObservableList<AirbornePos> airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);


    private DoubleProperty altitude;
    private DoubleProperty velocity;
    private DoubleProperty trackOrHeading;


    public ObservableAircraftState(IcaoAddress icaoAddress) {
    }

    public ReadOnlyLongProperty lastMessageTimeStampNsProperty() {
        return lastMessageTimeStampNs;
    }

    public ReadOnlyIntegerProperty categoryProperty() {
        return category;
    }

    public ReadOnlyObjectProperty<CallSign> callSignProperty() {
        return callSign;
    }

    public ReadOnlyObjectProperty<GeoPos> positionProperty() {
        return position;
    }

    public ObservableList<AirbornePos> airbornePosProperty() {
        return airbornePosSecond;
    }

    public ReadOnlyDoubleProperty altitudeProperty() {
        return altitude;
    }

    public ReadOnlyDoubleProperty velocityProperty() {
        return velocity;
    }

    public ReadOnlyDoubleProperty trackOrHeadingProperty() {
        return trackOrHeading;
    }

    public long getLastMessageTimeStampNs() {
        return lastMessageTimeStampNs.get();
    }

    public int getCategory() {
        return category.get();
    }

    public CallSign getCallSign() {
        return callSign.get();
    }

    public GeoPos getPosition() {
        return position.get();
    }

    public List<AirbornePos> getAirbornePos() {
        return new ArrayList<>(airbornePos);
    }

    public double getAltitude() {
        return altitude.get();
    }

    public double getVelocity() {
        return velocity.get();
    }

    public double getTrackOrHeading() {
        return trackOrHeading.get();
    }

    @Override
    public void setLastMessageTimeStampNs(long timeStampNs) {
        if(this.lastMessageTimeStampNs.get() == timeStampNs){
            airbornePos.set( airbornePos.size() - 1, new AirbornePos(position.get(), altitude.get()));
            airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);
        }

        this.lastMessageTimeStampNs.set(timeStampNs);
    }

    @Override
    public void setCategory(int category) {
        this.category.set(category);
    }

    @Override
    public void setCallSign(CallSign callSign) {
        this.callSign.set(callSign);
    }

    @Override
    public void setPosition(GeoPos position) {
        if (!(this.position.get().equals(position))){
            airbornePos.add(new AirbornePos(position, altitude.get()));
            airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);
        }
        this.position.set(position);

    }

    @Override
    public void setAltitude(double altitude) {
        if (this.altitude.get() != altitude){
            airbornePos.add(new AirbornePos(position.get(), altitude));
            airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);
        }
        this.altitude.set(altitude);
    }

    @Override
    public void setVelocity(double velocity) {
        this.velocity.set(velocity);
    }

    @Override
    public void setTrackOrHeading(double trackOrHeading) {
        this.trackOrHeading.set(trackOrHeading);
    }

    public record AirbornePos(GeoPos pos, double altitude) {
    }
}
