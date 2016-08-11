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

        for (int i = 0; i < counter.size(); i++) {

            wavelengths.add(counter.get(i));

        }
        Collections.sort(wavelengths);

        if (wavelengths.size() != lightPaths.size()) {
            System.out.print("");
        }
    }

    Random random = new Random();

    private double getRandomDouble() {
        return minWavelength + (maxWavelength - minWavelength) * random.nextDouble();
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
        LinkTable newLinkTable = new LinkTable();
        newLinkTable = initializeRandomTable(lightPaths, newLinkTable);
        return newLinkTable;
    }

    private LinkTable initializeRandomTable(List<LightPath> lightPaths, LinkTable newLinkTable) {
        newLinkTable.lightPaths = lightPaths;
        for (LightPath lightPath : newLinkTable.lightPaths) {
            lightPath.setWavelength(getRandomDouble());
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
        SecureRandom random = new SecureRandom();
        double bestWavelength = getSmallestAvailableSpace();
        if (bestWavelength == -1) return;
        lightPaths.get(random.nextInt(lightPaths.size())).setWavelength(bestWavelength);
        buildTable();
        if (wavelengths.size() != lightPaths.size()) {
            //  System.out.print(" ");
        }
    }

    private double getSmallestAvailableSpace() {
        double t = 0.5;
        double freeMin = 9999;
        int bestIndex = 0;
        double point1 = minWavelength;
        double point2, separation, bestSeparation = t;
        int i;
        for (i = 1; i <= wavelengths.size(); i++) {
            point2 = wavelengths.get(i - 1);
            separation = (point2 - t) - (point1 + t);

            if ((separation < freeMin) && (separation > 2 * t)) {
                freeMin = separation;
                bestIndex = i;
                bestSeparation = separation;
            }
            point1 = point2;
        }
        point2 = maxWavelength;
        separation = (point2 - t) - (point1 + t);
        if ((separation < freeMin) && (separation > 2 * t)) {
            //freeMin = separation;
           // bestIndex = wavelengths.size();
            bestSeparation = separation;
            return maxWavelength - bestSeparation / 2;
        }
        if(bestIndex==0){
            return minWavelength + bestSeparation/2;
        }
        return wavelengths.get(bestIndex-1) - bestSeparation / 2;

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
