package mas.german.landingplanes;

import android.util.Log;

/**
 * Represents a light plane, which is an Aircraft.
 * It can land on both long and short runways.
 */
public class LightPlane extends Aircraft {
    private static final String TAG = LightPlane.class.getSimpleName();

    public boolean land(LandingSite site) {
        return site.accept(this);
    }
}
