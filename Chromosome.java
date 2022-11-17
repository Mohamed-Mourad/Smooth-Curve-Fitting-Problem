import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Chromosome {
    static int degree;
    float fitness;
    double totalError;
    double error = 0.0;
    ArrayList<Float> genes = new ArrayList<>(degree + 1);

    public Chromosome() {
        this.fitness = Float.valueOf(0);
    }

    public Chromosome(ArrayList<Float> genes) {
        this.genes = genes;
    }

    void setFitness(Dataset set) {
        for (int i = 0; i < set.numPoints; i++) {
            Float x = set.points.get(i).x;
            Float y = set.points.get(i).y;
            error = 0.0;
            for (int j = 0; j < degree; j++) {
                error += genes.get(j) * (Math.pow(x, j));
            }
            error = Math.pow(error - y, 2);
            totalError += error;
        }
        totalError = totalError / set.numPoints;
        this.fitness = (float) (1 / totalError);
    }

    void printChromosome(PrintWriter pw) throws IOException {
        pw.println("fitness: " + fitness);
        pw.println("mean squared error: " + totalError);
        String genome = "";
        for (int i = 0; i < degree + 1; i++) {
            genome = genome + genes.get(i).toString() + " , ";
        }
        pw.println("coefficients: " + genome);
    }
}
