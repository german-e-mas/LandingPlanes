package mas.german.landingplanes;

/**
 * This class represents the position of something. Provides methods to determine if two positions
 * are the same, calculate distance between them, check if they are close to another position, etc.
 */
public class Position {
    private double mX;
    private double mY;

    public Position(double x, double y) {
        mX = x;
        mY = y;
    }

    public double distanceTo(Position pos) {
        double u = mX - pos.mX;
        double v = mY - pos.mY;
        return Math.sqrt(u * u + v * v);
    }

    public void add(Position position) {
        mX += position.mX;
        mY += position.mY;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(mX).append(",").append(mY).append(")");
        return stringBuilder.toString();
    }
}