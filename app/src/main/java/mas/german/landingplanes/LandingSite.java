package mas.german.landingplanes;

import android.util.Log;

/**
 * Represents a Landing Site. Specific landing sites should extend from this class.
 * A landing site has a size (width and height), color, and can only accept certain Aircrafts.
 */
public abstract class LandingSite {
    private static final String TAG = LandingSite.class.getSimpleName();

    private int mWidth;
    private int mHeight;

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public abstract boolean accept(LargePlane largePlane);

    public abstract boolean accept(LightPlane lightPlane);

    public abstract boolean accept(Helicopter helicopter);
}
