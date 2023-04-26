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

/**
 * Class that represent an aircraft state. This state is observable. It uses many JavaFX property to describe each
 * data of the state.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
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

    private long addProvokedMessTimeStamp = -1;


    public ObservableAircraftState(IcaoAddress icaoAddress) {
        lastMessageTimeStampNs = new SimpleLongProperty(0);
        category = new SimpleIntegerProperty(0);
        callSign = new SimpleObjectProperty<>(null);
        position = new SimpleObjectProperty<>(null);
        altitude = new SimpleDoubleProperty(0);
        velocity = new SimpleDoubleProperty(0);
        trackOrHeading = new SimpleDoubleProperty(0);
    }

    /**
     * Return the long property that represent the timestamp of the last message sent by the aircraft.
     * The property is only readable.
     *
     * @return (ReadOnlyLongProperty) : the property that represent the timestamp of the last message.
     */
    public ReadOnlyLongProperty lastMessageTimeStampNsProperty() {
        return lastMessageTimeStampNs;
    }

    /**
     * Return the integer property that represent the category of the aircraft. The property is only readable.
     *
     * @return (ReadOnlyIntegerProperty) : the property that represent the category of the aircraft
     */
    public ReadOnlyIntegerProperty categoryProperty() {
        return category;
    }

    /**
     * Return the object property that represent the callsign of the aircraft. The property only readable.
     *
     * @return (ReadOnlyObjectProperty) : the  property that represent the callsign of the aircraft
     */
    public ReadOnlyObjectProperty<CallSign> callSignProperty() {
        return callSign;
    }

    /**
     * Return the object property that represent the position of the aircraft. The property is only readable.
     *
     * @return (ReadOnlyProperty) : the property that represent the position of the aircraft
     */
    public ReadOnlyObjectProperty<GeoPos> positionProperty() {
        return position;
    }


    /**
     * Return the list of airborne positions of the aircraft. The list is observable.
     *
     * @return (ObservableList < AirbornePos >) : the list of airborne positions of the aircraft
     */
    public ObservableList<AirbornePos> airbornePosProperty() {

        return airbornePosSecond;
    }

    /**
     * Return the double property that represent the altitude of the aircraft. The property is only readable.
     *
     * @return (ReadOnlyDoubleProperty) : the property that represent the altitude of the aircraft
     */
    public ReadOnlyDoubleProperty altitudeProperty() {
        return altitude;
    }

    /**
     * Return the double property that represent the velocity of the aircraft. The property is only readable.
     *
     * @return (ReadOnlyDoubleProperty) : the property that represent the velocity of the aircraft
     */
    public ReadOnlyDoubleProperty velocityProperty() {
        return velocity;
    }

    /**
     * Return the double property that represent the track/heading of the aircraft. The property is only readable.
     *
     * @return (ReadOnlyDoubleProperty) : the property that represent the track/heading of the aircraft
     */
    public ReadOnlyDoubleProperty trackOrHeadingProperty() {
        return trackOrHeading;
    }

    /**
     * Get the value of the property containing the timestamp of the last message sent by the aircraft.
     *
     * @return (long) : the timestamp of the last message
     */
    public long getLastMessageTimeStampNs() {
        return lastMessageTimeStampNs.get();
    }

    /**
     * Get the value of the property containing the category of the aircraft.
     *
     * @return (int) : the category of the aircraft
     */
    public int getCategory() {
        return category.get();
    }

    /**
     * Get the value of the property containing the call sign of the aircraft.
     *
     * @return (CallSign) : the call sign of the aircraft
     */
    public CallSign getCallSign() {
        return callSign.get();
    }

    /**
     * Get the value of the property containing the position of the aircraft.
     *
     * @return (GeoPos) : the position of the aircraft
     */
    public GeoPos getPosition() {
        return position.get();
    }

    /**
     * Get the value of the property containing all the airborne positions. All this position is given is a list.
     *
     * @return (List < AirbornePos >) : the airborne positions
     */
    public List<AirbornePos> getAirbornePos() {
        return new ArrayList<>(airbornePos);
    }

    /**
     * Get the value of the property containing the altitude of the aircraft.
     *
     * @return (double) : the altitude of the aircraft
     */
    public double getAltitude() {
        return altitude.get();
    }

    /**
     * Get the value of the property containing the velocity of the aircraft.
     *
     * @return (double) : the velocity of the aircraft
     */
    public double getVelocity() {
        return velocity.get();
    }

    /**
     * Get the value of the property containing the track/heading of the aircraft.
     *
     * @return (double) : the track/heading of the aircraft
     */
    public double getTrackOrHeading() {
        return trackOrHeading.get();
    }

    /**
     * Set the timestamp of the last message received in the correct property
     *
     * @param timeStampNs (long) : the timestamp to update
     */
    @Override
    public void setLastMessageTimeStampNs(long timeStampNs) {
        this.lastMessageTimeStampNs.set(timeStampNs);
    }

    /**
     * Set the category of the aircraft in the correct property
     *
     * @param category (int) : the category to update
     */
    @Override
    public void setCategory(int category) {
        this.category.set(category);
    }

    /**
     * Set the call sign of the aircraft in the correct property
     *
     * @param callSign (CallSign) : the call sign to update
     */
    @Override
    public void setCallSign(CallSign callSign) {
        this.callSign.set(callSign);
    }

    /**
     * Set the position of the aircraft in the correct property
     *
     * @param position (GeoPos) : the position to update
     */
    @Override
    public void setPosition(GeoPos position) {

        if (lastMessageTimeStampNs.get() == addProvokedMessTimeStamp){
            airbornePos.set(airbornePos.size() - 1, new AirbornePos(position, altitude.get()));
            airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);
        }

        if ((this.getPosition() == null ||
                !(this.position.get().equals(position))) &&
                lastMessageTimeStampNs.get() != addProvokedMessTimeStamp) {

            airbornePos.add(new AirbornePos(position, altitude.get()));
            airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);
            addProvokedMessTimeStamp = lastMessageTimeStampNs.get();
        }

        this.position.set(position);

    }

    /**
     * Set the altitude of the aircraft in the correct property
     *
     * @param altitude (double) : the altitude to update
     */
    @Override
    public void setAltitude(double altitude) {

        if (addProvokedMessTimeStamp == lastMessageTimeStampNs.get()){
            airbornePos.set(airbornePos.size() - 1, new AirbornePos(position.get(), altitude));
            airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);
        }
        if (this.altitude.get() != altitude &&
                addProvokedMessTimeStamp != lastMessageTimeStampNs.get()) {

            airbornePos.add(new AirbornePos(position.get(), altitude));
            airbornePosSecond = FXCollections.unmodifiableObservableList(airbornePos);
            addProvokedMessTimeStamp = lastMessageTimeStampNs.get();
        }
        this.altitude.set(altitude);
    }

    /**
     * Set the velocity of the aircraft in the correct property
     *
     * @param velocity (double) : the velocity to update
     */
    @Override
    public void setVelocity(double velocity) {
        this.velocity.set(velocity);
    }

    /**
     * Set the track or heading of the aircraft in the correct property
     *
     * @param trackOrHeading (double) : the track or heading to update
     */
    @Override
    public void setTrackOrHeading(double trackOrHeading) {
        this.trackOrHeading.set(trackOrHeading);
    }

    /**
     * Record that represent an airborne position. It stores the position (in GeoPos) and the altitude
     * of the aircraft.
     *
     * @param pos      (GeoPos) : the position of the aircraft
     * @param altitude (double) : the altitude of the aircraft
     */
    public record AirbornePos(GeoPos pos, double altitude) {
    }
}
