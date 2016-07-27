package Defragmentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 7/27/2016.
 */
public class Selector {
    List<LinkTable> listA, listB;

    public Selector() {

    }

    public List<LinkTable> crossoverSelection(List<LinkTable> chromosomes) {
        listA = chromosomes;
        listB = new ArrayList<>();
        listB.add(selectRandom(listA));
        listB.add(selectRandom(listA));
        return listB;
    }

    public List<LinkTable> mutationSelection(List<LinkTable> chromosomes, int P) {
        listA = chromosomes;
        listB = new ArrayList<>();
        while (listB.size() < P) {
            listB.add(selectSemiElite(listA));
        }
        return listB;
    }

    private LinkTable selectSemiElite(List<LinkTable> chromosomes) {
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

    public List<LinkTable> newSelection(List<LinkTable> chromosomes, int P, int alpha) {
        listA = chromosomes;
        List<LinkTable> bestList = selectBestIndividuals(alpha);
        List<LinkTable> randomList = selectRandomIndividuals(P - alpha);
        Merger merger = new Merger();
        return merger.merge(bestList, randomList);
    }

    public List<LinkTable> selectBestIndividuals(int alpha) {
        List<LinkTable> bestList = new ArrayList<>();
        while (bestList.size() < alpha) {
            double bestFrag = 999;
            LinkTable bestTable = null;
            for (LinkTable table : listA) {
                if (table.getFragmentation() < bestFrag) {
                    bestTable = table;
                    bestFrag = bestTable.getFragmentation();
                    listA.remove(table);
                }
            }
            bestList.add(bestTable);
        }
        return bestList;
    }

    public List<LinkTable> selectRandomIndividuals(int a) {
        List<LinkTable> randomList = new ArrayList<>();
        while (randomList.size() < a) {
            LinkTable randomTable = selectRandom(listA);
            listA.remove(randomTable);
            randomList.add(randomTable);
        }
        return randomList;
    }
}
