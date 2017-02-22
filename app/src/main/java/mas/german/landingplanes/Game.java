package mas.german.landingplanes;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import mas.german.landingplanes.aircrafts.*;
import mas.german.landingplanes.landingsites.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Game class contains the logic of all the game. It is a singleton because only one instance should
 * exist.
 *
 * This class does the following tasks:
 * - Generates Landing Sites and store them on a array.
 * - Detects collisions of aircraft, and finishes the game if they happen.
 * - Detects landings and increases the score.
 * - Checks if an aircraft has an invalid position and takes it off the array.
 */
public class Game implements AircraftGenerator.OnAircraftGenerated {
    private static final String TAG = Game.class.getSimpleName();
    private static final int UPDATE_MS = 200;

    private static Game sInstance = null;

    /**
     * Various game-related events that a listener may want to react upon.
     */
    public interface EventsListener {
        /**
         * Let the listener know the game is finished.
         */
        void onGameOver();

        /**
         * A new Long Runway was created.
         *
         * @param longRunway  The long runway created.
         */
        void onLongRunwayCreated(LongRunway longRunway);

        /**
         * A new Short Runway was created.
         *
         * @param shortRunway  The short runway created.
         */
        void onShortRunwayCreated(ShortRunway shortRunway);

        /**
         * A new Helipad was created.
         *
         * @param helipad   The helipad created.
         */
        void onHelipadCreated(Helipad helipad);

        /**
         * A new Large Plane was created.
         *
         * @param largePlane  The large plane that was generated.
         */
        void onLargePlaneGenerated(LargePlane largePlane);

        /**
         * A new Light Plane was created.
         *
         * @param lightPlane  The large plane that was generated.
         */
        void onLightPlaneGenerated(LightPlane lightPlane);

        /**
         * A new Helicopter was created.
         *
         * @param helicopter  The large plane that was generated.
         */
        void onHelicopterGenerated(Helicopter helicopter);

        /**
         * All aircraft were moved. Notify the listeners in order to reflect changes in position of
         * the Aircraft on the view.
         */
        void onAircraftPositionChanged();

        /**
         * An aircraft has landed.
         *
         * @param id    ID of the Aircraft that landed.
         */
        void onLand(int id);

        /**
         * An aircraft has left the game's area.
         *
         * @param id    ID of the Aircraft that left the aerodrome.
         */
        void onAircraftOutsideAerodrome(int id);

        /**
         * A crash happened.
         *
         * @param firstId   ID of the first Aircraft involved in the crash.
         * @param secondId  ID of the second Aircraft involved in the crash.
         */
        void onCrash(int firstId, int secondId);
    }

    public void setListener(EventsListener eventsListener) {
        mEventsListener = eventsListener;
        setStartingSites();
    }

    private ArrayList<Aircraft> mAircraftList;
    private ArrayList<LandingSite> mSites;
    private int mScore;
    private Aerodrome mAerodrome;
    private EventsListener mEventsListener;

    private ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mUpdateTask;

    private AircraftGenerator mGenerator;

    /**
     * Get the unique instance of the Game class.
     *
     * @return Game instance.
     */
    public static Game getInstance() {
        if (sInstance == null) {
            sInstance = new Game();
        }
        return sInstance;
    }

    private Game() {
        // Creates the periodic Tasks.
        mExecutor = Executors.newScheduledThreadPool(1);
        // Containers for all the active aircrafts and landing sites.
        mAircraftList = new ArrayList<>();
        mSites = new ArrayList<>();
        // Other game-related variables.
        mAerodrome = new Aerodrome(0,100,100,0);
        mGenerator = new AircraftGenerator(mAerodrome);
        mGenerator.setOnAircraftGeneratedListener(this);
        mGenerator.begin();
        mScore = 0;

        // Periodic task to update the game status.
        mUpdateTask = mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Aircraft iterator for safely handle the removal of aircrafts from the list.
                synchronized (mAircraftList) {
                    Iterator<Aircraft> iterator = mAircraftList.iterator();
                    while (iterator.hasNext()) {
                        Aircraft aircraft = iterator.next();
                        aircraft.moveForward();

                        // Check for any crash.
                        for (Aircraft otherAircraft : mAircraftList) {
                            if (aircraft.crashesWith(otherAircraft)) {
                                if (mEventsListener != null) {
                                    mEventsListener.onCrash(aircraft.getId(), otherAircraft.getId());
                                }
                                gameOver();
                                return;
                            }
                        }

                        // Check for any landing.
                        for (LandingSite site : mSites) {
                            if (aircraft.land(site)) {
                                if (mEventsListener != null) {
                                    mEventsListener.onLand(aircraft.getId());
                                }
                                mScore++;
                                iterator.remove();
                            }
                        }

                        // Delete any aircraft that is outside the aerodrome.
                        if (mAerodrome.isOutOfBounds(aircraft)) {
                            if (mEventsListener != null) {
                                mEventsListener.onAircraftOutsideAerodrome(aircraft.getId());
                            }
                            iterator.remove();
                        }
                    }

                    // All aircraft were moved.
                    if (mEventsListener != null) {
                        mEventsListener.onAircraftPositionChanged();
                    }
                }
            }
        }, 0, UPDATE_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates the initial Landing Sites. They are currently hardcoded in the given positions.
     */
    private void setStartingSites() {
        LongRunway longRunway = new LongRunway(new Position(50, 75), 0, Math.toRadians(90));
        mSites.add(longRunway);
        if (mEventsListener != null) {
            mEventsListener.onLongRunwayCreated(longRunway);
        }

        ShortRunway shortRunway = new ShortRunway(new Position(75, 25), Math.toRadians(90), Math.toRadians(90));
        mSites.add(shortRunway);
        if (mEventsListener != null) {
            mEventsListener.onShortRunwayCreated(shortRunway);
        }

        Helipad helipad = new Helipad(new Position(25, 25));
        mSites.add(helipad);
        if (mEventsListener != null) {
            mEventsListener.onHelipadCreated(helipad);
        }
    }

    /**
     * Game over procedure.
     */
    private void gameOver() {
        // Stop the Generator.
        mGenerator.stop();
        // Cancel the Update Task.
        mUpdateTask.cancel(true);
        // Notify the EventsListener about the event.
        if (mEventsListener != null) {
            mEventsListener.onGameOver();
        }
    }

    /**
     * Return a Map with the Aircraft ID and a clone of it's Position.
     */
    public Map<Integer, Position> getAircraftPositionMap() {
        Map<Integer, Position> positionMap = new HashMap<>();
        // The Map is populated in a synchronized block.
        synchronized (mAircraftList) {
            for (Aircraft aircraft : mAircraftList) {
                positionMap.put(aircraft.getId(), aircraft.getPosition().clone());
            }
        }
        return positionMap;
    }

    public Aerodrome getAerodrome() {
        return mAerodrome;
    }

    @Override
    public void onAircraftGenerated(Aircraft generatedAircraft) {
        // Synchronize the Aircraft list to prevent access during the operation.
        synchronized (mAircraftList) {
            mAircraftList.add(generatedAircraft);
        }
        // Make the Aircraft notify itself. It's subtypes will call the corresponding method.
        generatedAircraft.notifyCreation(this);
    }

    /**
     * Notify the listener that a Large Plane was created. This is called from the corresponding
     * subclass of Aircraft, after calling their notifyCreation method.
     */
    public void createdLargePlane(LargePlane largePlane) {
        if (mEventsListener != null) {
            mEventsListener.onLargePlaneGenerated(largePlane);
        }
    }

    /**
     * Notify the listener that a Light Plane was created. This is called from the corresponding
     * subclass of Aircraft, after calling their notifyCreation method.
     */
    public void createdLightPlane(LightPlane lightPlane) {
        if (mEventsListener != null) {
            mEventsListener.onLightPlaneGenerated(lightPlane);
        }
    }

    /**
     * Notify the listener that a Helicopter was created. This is called from the corresponding
     * subclass of Aircraft, after calling their notifyCreation method.
     */
    public void createdHelicopter(Helicopter helicopter) {
        if (mEventsListener != null) {
            mEventsListener.onHelicopterGenerated(helicopter);
        }
    }
}
