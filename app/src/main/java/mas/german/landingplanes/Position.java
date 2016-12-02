package mas.german.landingplanes;

/**
 * This class represents the position of something. Provides methods to determine if two positions
 * are the same, calculate distance between them, check if they are close to another position, etc.
 */
public class Position {
    private static final String TAG = Position.class.getSimpleName();

    private int mX;
    private int mY;

    Position(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public void setX(int x) {
        mX = x;
    }

    public void setY(int y) {
        mY = y;
    }

    public double distanceTo(Position pos) {
        double u = mX - pos.getX();
        double v = mY - pos.getY();
        return Math.sqrt(Math.pow(u, 2) + Math.pow(v, 2));
    }
}
