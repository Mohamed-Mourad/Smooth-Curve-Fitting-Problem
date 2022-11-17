import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    public static void main(String args[]) throws IOException {

        PrintWriter pw = new PrintWriter("optimalSolutions.txt");

        BufferedReader fileReader = new BufferedReader(new FileReader("D:/Mado/University/Year 4 - Sem 1/Soft Computing/Assignments/Assignment2v2/curve_fitting_input.txt"));
        int numTests = Integer.parseInt(fileReader.readLine());

        ArrayList<Dataset> sets = new ArrayList<>();

        for (int i=0; i<numTests; i++) {
            StringTokenizer st = new StringTokenizer(fileReader.readLine());
            int numPoints = Integer.parseInt(String.valueOf(st.nextToken()));
            int degree = Integer.parseInt(String.valueOf(st.nextToken()));
            sets.add(new Dataset(numPoints, degree));
            for (int j=0; j<numPoints; j++) {
                st = new StringTokenizer(fileReader.readLine());
                Float x = Float.parseFloat(String.valueOf(st.nextToken()));
                Float y = Float.parseFloat(String.valueOf(st.nextToken()));
                Point p = new Point(x, y);
                sets.get(i).points.add(p);
            }
        }
        for (int i=1; i<=sets.size(); i++) {
            GA ga = new GA();
            pw.println("Test " + i + ": ");
            System.out.println(i);
            ga.run(sets.get(i-1), pw);
        }
        pw.close();
    }
}
