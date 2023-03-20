package game.utils;

/***
 * From <a href="https://gist.github.com/gunvirranu/6816d65c0231981787ebefd3bdb61f98">here</a>.
 */
public class Vector {
    public double x;
    public double y;

    public Vector() { }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector v) {
        set(v);
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void setZero() {
        x = 0;
        y = 0;
    }

    public double[] getComponents() {
        return new double[]{x, y};
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public double getLengthSq() {
        return (x * x + y * y);
    }

    public double distanceSq(double vx, double vy) {
        vx -= x;
        vy -= y;
        return (vx * vx + vy * vy);
    }

    public double distanceSq(Vector v) {
        double vx = v.x - this.x;
        double vy = v.y - this.y;
        return (vx * vx + vy * vy);
    }

    public double distance(double vx, double vy) {
        vx -= x;
        vy -= y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    public double distance(Vector v) {
        double vx = v.x - this.x;
        double vy = v.y - this.y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    public double getAngle() {
        return Math.atan2(y, x);
    }

    public void normalize() {
        double magnitude = getLength();
        x /= magnitude;
        y /= magnitude;
    }

    public Vector getNormalized() {
        double magnitude = getLength();
        return new Vector(x / magnitude, y / magnitude);
    }

    public static Vector toCartesian(double magnitude, double angle) {
        return new Vector(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    public void add(Vector v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void add(double vx, double vy) {
        this.x += vx;
        this.y += vy;
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y);
    }

    public Vector getAdded(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }

    public void subtract(Vector v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void subtract(double vx, double vy) {
        this.x -= vx;
        this.y -= vy;
    }

    public static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y);
    }

    public Vector getSubtracted(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }

    public void multiply(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    public Vector getMultiplied(double scalar) {
        return new Vector(x * scalar, y * scalar);
    }

    public void divide(double scalar) {
        x /= scalar;
        y /= scalar;
    }

    public Vector getDivided(double scalar) {
        return new Vector(x / scalar, y / scalar);
    }

    public Vector getPerp() {
        return new Vector(-y, x);
    }

    public double dot(Vector v) {
        return (this.x * v.x + this.y * v.y);
    }

    public double dot(double vx, double vy) {
        return (this.x * vx + this.y * vy);
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public double cross(Vector v) {
        return (this.x * v.y - this.y * v.x);
    }

    public double cross(double vx, double vy) {
        return (this.x * vy - this.y * vx);
    }

    public static double cross(Vector v1, Vector v2) {
        return (v1.x * v2.y - v1.y * v2.x);
    }

    public double project(Vector v) {
        return (this.dot(v) / this.getLength());
    }

    public double project(double vx, double vy) {
        return (this.dot(vx, vy) / this.getLength());
    }

    public static double project(Vector v1, Vector v2) {
        return (dot(v1, v2) / v1.getLength());
    }

    public Vector getProjectedVector(Vector v) {
        return this.getNormalized().getMultiplied(this.dot(v) / this.getLength());
    }

    public Vector getProjectedVector(double vx, double vy) {
        return this.getNormalized().getMultiplied(this.dot(vx, vy) / this.getLength());
    }

    public static Vector getProjectedVector(Vector v1, Vector v2) {
        return v1.getNormalized().getMultiplied(Vector.dot(v1, v2) / v1.getLength());
    }

    public void rotateBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double rx = x * cos - y * sin;
        y = x * sin + y * cos;
        x = rx;
    }

    public Vector getRotatedBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(x * cos - y * sin, x * sin + y * cos);
    }

    public void rotateTo(double angle) {
        set(toCartesian(getLength(), angle));
    }

    public Vector getRotatedTo(double angle) {
        return toCartesian(getLength(), angle);
    }

    public void reverse() {
        x = -x;
        y = -y;
    }

    public Vector getReversed() {
        return new Vector(-x, -y);
    }

    @Override
    public Vector clone() {
        return new Vector(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vector) {
            Vector v = (Vector) obj;
            return (x == v.x) && (y == v.y);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vector2d[" + x + ", " + y + "]";
    }
}