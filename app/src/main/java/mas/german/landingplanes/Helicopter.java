package mas.german.landingplanes;

import android.util.Log;

/**
 * Represents an helicopter, which is an Aircraft.
 * It can only land on helipads.
 */
public class Helicopter extends Aircraft {
    private static final String TAG = Helicopter.class.getSimpleName();

    public boolean land(LandingSite site) {
        return site.accept(this);
    }
}
