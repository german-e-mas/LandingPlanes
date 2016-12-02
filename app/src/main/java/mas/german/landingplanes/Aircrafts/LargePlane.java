package mas.german.landingplanes.Aircrafts;

import mas.german.landingplanes.LandingSites.LandingSite;

/**
 * Represents a large plane, which is an Aircraft.
 * It can only land on long runways.
 */
public class LargePlane extends Aircraft {
    private static final String TAG = LargePlane.class.getSimpleName();

    public boolean land(LandingSite site) {
        return site.accept(this);
    }
}
