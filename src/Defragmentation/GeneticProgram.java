package Defragmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 6/24/2016.
 */
public class GeneticProgram {
    private List<LinkTable> chromosomes, temp, crossoverList, mutationList, newList;
    private int P = 100;
    LinkTable linkTable;
    FragmentationTester fragmentationTester = new FragmentationTester();
    Selector selector = new Selector();
    Mutator mutator = new Mutator();
    Crosser crosser = new Crosser();
    Merger merger = new Merger();

    public GeneticProgram(LinkTable linkTable) {
        this.linkTable = linkTable;
        initializePop();

        //test fitness for each individual
        testFragmentation(chromosomes);

        //displayChromosomes();

        //pairwise roulette wheel selection for crossoverList
        crossover();
        //crossoverList until P individuals are created

        //Test fitness of new individuals
        testFragmentation(crossoverList);

        //merge populations
        chromosomes = merger.merge(chromosomes, crossoverList);

        //semi-elitist selection of P/2P individuals for mutation
        mutationList = selector.mutationSelection(chromosomes, P);
        mutationList = mutator.mutate(mutationList);
        testFragmentation(mutationList);

        //merge populations
        chromosomes = merger.merge(chromosomes, mutationList);

        //select alpha best individuals
        chromosomes = selector.newSelection(chromosomes, P, alpha);
        displayChromosomes();
    }

    private void crossover() {
        crossoverList = new ArrayList<>();
        while (crossoverList.size() < P) {
            temp = selector.crossoverSelection(chromosomes);
            crossoverList.add(temp.get(0).crossover(temp.get(1)));
        }
    }

    int alpha = P / 4;

    private void initializePop() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            chromosomes.add(linkTable.getRandomTable());
        }
    }

    private void testFragmentation(List<LinkTable> test) {
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
