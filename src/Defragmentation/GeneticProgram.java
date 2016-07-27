package Defragmentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 6/24/2016.
 */
public class GeneticProgram {
    private List<LinkTable> chromosomes, temp;
    private int P = 100;
    LinkTable linkTable;
    FragmentationTester fragmentationTester = new FragmentationTester();

    public GeneticProgram(LinkTable linkTable) {
        this.linkTable = linkTable;
        initializePop();
        //test fitness for each individual
        testFitness(chromosomes);
        //displayChromosomes();
        //pairwise roulette wheel selection for crossover
        crossoverSelection();
        //crossover until P individuals are created
        //Test fitness of new individuals
        testFitness(temp);
        //merge populations
        mergePopulations();
        //semi-elitist selection of P/2P individuals for mutation
        mutationSelection();
        mutate();
        testFitness(temp);
        mergePopulations();
        //select alpha best individuals
        selectBestIndividuals();
        //select (P-alpha) random individuals
        selectRandomIndividuals();
        displayChromosomes();
    }

    int alpha = P / 4;

    private void selectBestIndividuals() {
        temp = sortLinkTableList(chromosomes);
        chromosomes = new ArrayList<>();
        while (chromosomes.size() < alpha) {
            LinkTable selected = temp.get(0);
            chromosomes.add(selected);
            temp.remove(selected);
        }
    }

    private void selectRandomIndividuals() {
        LinkTable random = selectRandom(temp);
        chromosomes.add(random);
        temp.remove(random);
    }

    private List<LinkTable> sortLinkTableList(List<LinkTable> list) {
        Collections.sort(list);
        return list;
    }

    private void mutate() {
        for (LinkTable table : temp) {
            table.mutate();
        }
    }

    private void mutationSelection() {
        temp = new ArrayList<>();
        while (temp.size() < P) {
            temp.add(selectSemiElite());
        }
    }

    private LinkTable selectSemiElite() {
        Random random = new Random();
        if (random.nextDouble() > 0.5)
            return selectBest(chromosomes);
        else
            return selectRandom(chromosomes);
    }

    private LinkTable selectBest(List<LinkTable> list) {
        double min = 999;
        LinkTable best = null;
        for (LinkTable table : list) {
            if (table.getFragmentation() < min) {
                best = table;
                min = best.getFragmentation();
            }
        }
        return best;
    }

    private LinkTable selectRandom(List<LinkTable> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    private void mergePopulations() {
        for (LinkTable c : temp) {
            chromosomes.add(c);
        }
    }

    private void crossoverSelection() {
        Random random = new Random();
        temp = new ArrayList<>();
        while (temp.size() < 100) {
            crossover(selectRandom(chromosomes),selectRandom(chromosomes));
        }
    }

    private void crossover(LinkTable A, LinkTable B) {
        temp.add(A.crossover(B));
    }

    private void initializePop() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            chromosomes.add(linkTable.getRandomTable());
        }
    }

    private void testFitness(List<LinkTable> test) {
        for (LinkTable table : test) {
            fragmentationTester.calculateFragmentation(table);
        }
    }

    private void displayChromosomes() {
        for (LinkTable table : chromosomes) {
            table.displayFitness();
        }
    }
}
