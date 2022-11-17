import java.util.ArrayList;

public class Dataset {
    int numPoints;
    int degree;
    ArrayList<Point> points = new ArrayList<>();

    public Dataset(int p, int d) {
        this.numPoints = p;
        this.degree = d;
    }
}
