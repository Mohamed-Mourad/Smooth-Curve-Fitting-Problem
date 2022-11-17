import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GA {
    Dataset set;
    ArrayList<Chromosome> population = new ArrayList<>();
    ArrayList<Chromosome> parents = new ArrayList<>();
    ArrayList<Chromosome> offspring = new ArrayList<>();
    int popNum = 10;
    double pc = 0.7;
    double pm = 0.1;
    int t = 0;  // current generation
    int T = 4;  // maximum number of generations
    int b = 2;  // parameter that controls degree of non-uniformity belong [0.5,5]
    Chromosome optimalSolution = new Chromosome();

    void generatePopulation() {
        while (population.size() < popNum) {
            ArrayList<Float> genes = new ArrayList<>();
            for (int j = 0; j < (set.degree) + 1; j++) {
                float rd = (float) ((Math.random() * (10 - (-10))) + (-10));
                genes.add(rd);
            }
            Chromosome toAdd = new Chromosome(genes);
            toAdd.setFitness(set);
            population.add(toAdd);
        }
    }

    void selection() {
        if(t>0){
            Collections.shuffle(population);
        }
        for (int i = 0; i < population.size(); i += 3) {
            Float maxFitness = (float)0;
            int toAddIndex = 0;
            for (int j = 0; j < 2; j++) {
                if (population.get(j).fitness > maxFitness) {
                    maxFitness = population.get(j).fitness;
                    toAddIndex = j;
                }
            }
            parents.add(population.get(toAddIndex));
        }
    }

    void crossover() {
        for (int i = 0; i < parents.size() - 1; i += 2) {
            Chromosome offspringOne = new Chromosome();
            Chromosome offspringTwo = new Chromosome();
            if (Math.random() <= pc) {
                int crossoverPointOne = ThreadLocalRandom.current().nextInt(1, Chromosome.degree + 1);
                int crossoverPointTwo = ThreadLocalRandom.current().nextInt(1, Chromosome.degree + 1);
                ArrayList<Integer> crossPoints = new ArrayList<>();
                crossPoints.add(crossoverPointOne);
                crossPoints.add(crossoverPointTwo);
                crossPoints.sort((a, b) -> (b - a));
                if (crossoverPointOne != crossoverPointTwo) {
                    for (int j = 0; j < crossPoints.get(0); j++) {
                        offspringOne.genes.add(parents.get(i).genes.get(j));
                        offspringTwo.genes.add(parents.get(i + 1).genes.get(j));
                    }
                    for (int j = crossPoints.get(0); j < crossPoints.get(1); j++) {
                        offspringOne.genes.add(parents.get(i + 1).genes.get(j));
                        offspringTwo.genes.add(parents.get(i).genes.get(j));
                    }
                    for (int j = crossPoints.get(1); j < Chromosome.degree + 1; j++) {
                        offspringOne.genes.add(parents.get(i).genes.get(j));
                        offspringTwo.genes.add(parents.get(i + 1).genes.get(j));
                    }
                } else if (crossoverPointOne == crossoverPointTwo) {
                    for (int j = 0; j < crossoverPointOne; j++) {
                        offspringOne.genes.add(parents.get(i).genes.get(j));
                        offspringTwo.genes.add(parents.get(i + 1).genes.get(j));
                    }
                    for (int j = crossoverPointOne; j < Chromosome.degree + 1; j++) {
                        offspringTwo.genes.add(parents.get(i).genes.get(j));
                        offspringOne.genes.add(parents.get(i).genes.get(j));
                    }
                }
            }else {
                for (int j = 0; j < Chromosome.degree + 1; j++) {
                    offspringOne.genes.add(parents.get(i).genes.get(j));
                    offspringTwo.genes.add(parents.get(i+1).genes.get(j));
                }
            }
            offspringOne.setFitness(set);
            offspringTwo.setFitness(set);
            offspring.add(offspringOne);
            offspring.add(offspringTwo);
        }
    }

    void mutation() {
        for (Chromosome toMutate : offspring) {
            float LB = Collections.min(toMutate.genes);
            float UB = Collections.max(toMutate.genes);
            float Δ, Lxi, Uxi;
            for (int i = 0; i < Chromosome.degree + 1; i++) {
                if (Math.random() <= pm) {
                    float r1 = (int) (Math.random() * (1) + 0);
                    if (r1 <= 0.5) {
                        Lxi = toMutate.genes.get(i) - LB;
                        Δ = (float) (Lxi * (1 - Math.pow(r1, Math.pow(1 - t / T, b))));
                        toMutate.genes.set(i, toMutate.genes.get(i) - Δ); //xiNew
                    } else {
                        Uxi = UB - toMutate.genes.get(i);
                        Δ = (float) (Uxi * (1 - Math.pow(r1, Math.pow(1 - t / T, b))));
                        toMutate.genes.set(i, toMutate.genes.get(i) + Δ);
                    }
                }
            }
            toMutate.setFitness(set);
        }
    }

    void replacement() {
        ArrayList<Chromosome> allIndividuals = new ArrayList<>();
        for (Chromosome parent : parents) {
            allIndividuals.add(parent);
        }
        for (Chromosome offspring : offspring) {
            allIndividuals.add(offspring);
        }
        allIndividuals.sort((a, b) -> (int) (b.fitness - a.fitness));
        ArrayList<Chromosome> newGenerationList = new ArrayList<>();
        for (int i = 0; i < parents.size(); i++) {
            newGenerationList.add(allIndividuals.get(i));
        }
        this.optimalSolution = allIndividuals.get(0);
        population = newGenerationList;
    }

    void run(Dataset set, PrintWriter pw) throws IOException {
        this.set = set;
        Chromosome.degree = set.degree;
        generatePopulation();
        for (int i = 0; i < T; i++) {
            t = +1;
            selection();
            crossover();
            mutation();
            replacement();
        }
        pw.println("best solution reached: ");
        optimalSolution.printChromosome(pw);
    }
}
