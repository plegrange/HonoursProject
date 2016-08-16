package Defragmentation;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by FuBaR on 6/24/2016.
 */
public class LinkTable implements Comparable {

    private double maxWavelength = 1800.0, minWavelength = 1200.0, fragmentation, free, freeMax;
    List<LightPath> lightPaths;
    List<Double> wavelengths;
    List<String> linkIDs;
    String[][] table;

    public LinkTable(List<LightPath> lightPaths) {
        this.lightPaths = lightPaths;
    }

    public LinkTable() {

    }

    public double getFragmentation() {
        return fragmentation;
    }

    public void setFragmentation(double frag) {
        this.fragmentation = frag;
    }

    public void buildTable() {
        // populate arrays used as axis indices
        setAxes();        // initialize lightpath table
        table = new String[linkIDs.size()][wavelengths.size()];
        setTable(); // set all table entries as empty strings
        for (LightPath lightPath : lightPaths) {
            insertLightPathToTable(lightPath);
        }
    }

    private void setTable() {
        for (int i = 0; i < linkIDs.size(); i++) {
            for (int j = 0; j < wavelengths.size(); j++) {
                table[i][j] = "";
            }
        }
    }

    private void insertLightPathToTable(LightPath lightPath) {
        int i, j;
        for (String s : lightPath.getLinkIDs()) {
            i = getLinkIndex(s);
            j = getWavelengthIndex(lightPath.wavelength);
            table[i][j] = lightPath.id;
        }
    }

    private int getLinkIndex(String id) {
        // input : id of a given link
        // output : index of given link in index array
        //          OR -1 if link not found
        for (String s : linkIDs) {
            if (s.equals(id))
                return linkIDs.indexOf(s);
        }
        return -1;
    }

    private int getWavelengthIndex(double wavelength) {
        // input : value of a given wavelength
        // output : index of given wavelength in index array
        //          OR -1 if wavelength not found
        for (Double d : wavelengths) {
            if (d.equals(wavelength))
                return wavelengths.indexOf(d);
        }
        return -1;
    }

    private void setAxes() {
        // finds unique linkIDs and wavelength values and populates index arrays/axes
        linkIDs = new ArrayList<>();
        List<Double> counter = new ArrayList<>();
        for (LightPath lightPath : lightPaths) {
            for (String id : lightPath.getLinkIDs()) {
                if (linkIDs.size() == 0) {
                    linkIDs.add(id);
                } else if (!linkIDs.contains(id)) {
                    //addLinkID(id);
                    linkIDs.add(id);
                }
            }
            if (!counter.contains(lightPath.getWavelength()))
                counter.add(lightPath.getWavelength());
        }
        Collections.sort(linkIDs);
        wavelengths = new ArrayList<>();
        wavelengths.add(minWavelength);
        for (int i = 0; i < counter.size(); i++) {

            wavelengths.add(counter.get(i));

        }
        wavelengths.add(maxWavelength);
        Collections.sort(wavelengths);

        if (wavelengths.size() != lightPaths.size()) {
            System.out.print("");
        }
    }

    SecureRandom initialRandom;// = new SecureRandom();

    private double getInitialRandomDouble() {
        return minWavelength + (maxWavelength - minWavelength) * initialRandom.nextDouble();
    }

    public List<Double> getWavelengths() {
        return wavelengths;
    }

    public List<String> getLinkIDs() {
        return linkIDs;
    }

    public String[][] getTable() {
        return table;
    }

    public LinkTable getRandomTable() {
        LinkTable newLinkTable = new LinkTable(lightPaths);
        newLinkTable = initializeRandomTable(newLinkTable);
        return newLinkTable;
    }

    private LinkTable initializeRandomTable(LinkTable newLinkTable) {
        initialRandom = new SecureRandom();
        for (LightPath lightPath : newLinkTable.lightPaths) {
            lightPath.setWavelength(getInitialRandomDouble());
        }
        newLinkTable.buildTable();
        return newLinkTable;
    }

    public void displayFitness() {
        String s = String.format("%.2f", fragmentation);
        System.out.print(s + "% ");
    }

    public String getTableEntry(int i, int j) {
        return table[i][j];
    }

    public LinkTable crossover(LinkTable other) {
        LinkTable newLinkTable = new LinkTable(this, other);
        return newLinkTable;
    }

    public LinkTable(LinkTable A, LinkTable B) {
        this.wavelengths = A.wavelengths;
        this.linkIDs = A.getLinkIDs();
        this.lightPaths = A.lightPaths;
        int crossoverPoint = (new Random()).nextInt(this.lightPaths.size());
        //table = new String[linkIDs.size()][wavelengths.size()];
        //setTable();
        int crossoverCounter = 0;
        boolean cross = false;
        for (LightPath lightPath : lightPaths) {
            if (crossoverCounter >= crossoverPoint)
                cross = true;
            lightPath.setWavelength(selectWavelength(A, B, lightPath.id, cross));
            crossoverCounter++;
            //insertLightPathToTable(lightPath);
        }
        buildTable();
    }

    private double selectWavelength(LinkTable A, LinkTable B, String id, boolean cross) {
        LightPath a, b;
        a = A.getLightPath(id);
        b = B.getLightPath(id);
        if (cross) {
            return b.getWavelength();
        } else
            return a.getWavelength();
    }

    public void mutate() {
        Random random = new Random();

        LightPath lightPath = lightPaths.remove(random.nextInt(lightPaths.size()));
        wavelengths.remove(lightPath.wavelength);
        lightPath.setWavelength(getSmallestAvailableSpace());
        lightPaths.add(lightPath);
        buildTable();
        if (wavelengths.size() != lightPaths.size()) {
            //  System.out.print(" ");
        }
    }

    private double getMutationWavelength(double min, double max) {
        Random random = new Random();
        double randomWavelength = min + (max - min) * random.nextDouble();
        while (wavelengths.contains(randomWavelength)) {
            randomWavelength = min + (max - min) * random.nextDouble();
        }
        return randomWavelength;
    }

    private double getLargestGap() {
        double largestGapMin = 0, largestGapMax = 0;
        double largestSeparation = -9999;
        for (int i = 0; i < wavelengths.size() - 1; i++) {
            double separation = wavelengths.get(i + 1) - wavelengths.get(i);
            if (separation > largestSeparation) {
                largestSeparation = separation;
                largestGapMin = wavelengths.get(i);
                largestGapMax = largestGapMin + separation;
            }
        }
        return getMutationWavelength(largestGapMin, largestGapMax);
    }

    private double getSmallestAvailableSpace() {
        double t = 0.1;
        double freeMin = 9999;
        double bestMin = 0, bestMax = 0;
        double separation, bestSeparation;
        for (int i = 0; i < wavelengths.size() - 1; i++) {
            separation = wavelengths.get(i + 1) - wavelengths.get(i);
            if (separation < freeMin && separation > 2.0 * t) {
                freeMin = separation;
                bestMin = wavelengths.get(i);
                bestMax = bestMin + separation;
            }
        }
        return getMutationWavelength(bestMin+2*t, bestMax-2*t);
    }

    private void updateLightPaths(int j) {
        String cur = "-";
        for (int i = 0; i < linkIDs.size(); i++) {
            if (!table[i][j].equals(cur)) {
                changeLightPathWavelength(getTableEntry(i, j), wavelengths.get(j));
                //getLightPath(table[i][j]).setWavelength(wavelengths.get(j));
                cur = table[i][j];
            }
        }
    }

    private void changeLightPathWavelength(String id, double wavelength) {
        for (LightPath lightPath : lightPaths) {
            if (Objects.equals(lightPath.id, id)) {
                lightPath.setWavelength(wavelength);
            }
        }
    }

    private LightPath getLightPath(String id) {
        for (LightPath lightPath : lightPaths) {
            if (Objects.equals(lightPath.id, id)) {
                return lightPath;
            }
        }
        return null;
    }

    private int getRandomWavelengthIndex() {
        Random random = new Random();
        return random.nextInt(wavelengths.size());
    }

    @Override
    public int compareTo(Object o) {
        if (this.fragmentation > ((LinkTable) o).fragmentation) {
            return 1;
        } else if (this.fragmentation < ((LinkTable) o).fragmentation)
            return -1;
        else return 0;
    }
}
