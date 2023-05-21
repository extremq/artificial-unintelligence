package game.util;

public class Collisions {
    public static Vector solveCircleSquareCollision(Vector circlePos, double circleRadius, Vector squarePos, double squareSize) {
        Vector nearestPoint = new Vector();
        nearestPoint.x = Math.max(squarePos.x - squareSize / 2, Math.min(circlePos.x, squarePos.x + squareSize / 2));
        nearestPoint.y = Math.max(squarePos.y - squareSize / 2, Math.min(circlePos.y, squarePos.y + squareSize / 2));

        Vector rayToNearestPoint = new Vector();

        rayToNearestPoint.x = nearestPoint.x - circlePos.x;
        rayToNearestPoint.y = nearestPoint.y - circlePos.y;

        double distanceToNearestPoint = rayToNearestPoint.getLength();
        double overlap = circleRadius - distanceToNearestPoint;

        if (overlap < 0.1) {
            return circlePos;
        }

        circlePos.x -= rayToNearestPoint.x / distanceToNearestPoint * overlap;
        circlePos.y -= rayToNearestPoint.y / distanceToNearestPoint * overlap;

        return circlePos;
    }
}
