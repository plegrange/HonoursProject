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
    }

    SecureRandom random = new SecureRandom();

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
        System.out.println("Fragmentation: " + fragmentation);
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
        int crossoverPoint = Math.round(wavelengths.size() / 2);
        table = new String[linkIDs.size()][wavelengths.size()];
        for (int i = 0; i < linkIDs.size(); i++) {
            for (int j = 0; j < crossoverPoint; j++) {
                table[i][j] = A.getTableEntry(i, j);
            }
            for (int k = crossoverPoint; k < wavelengths.size(); k++) {
                table[i][k] = B.getTableEntry(i, k);
            }
        }
        this.lightPaths = A.lightPaths;
    }

    public void mutate() {
        String[] temp = new String[linkIDs.size()];
        int j = getRandomWavelengthIndex();
        for (int i = 0; i < linkIDs.size(); i++) {
            temp[i] = table[i][j];
        }
        int k = getRandomWavelengthIndex();
        for (int i = 0; i < linkIDs.size(); i++) {
            table[i][k] = table[i][j];
            table[i][j] = temp[i];
        }
        updateLightPaths(j);
        updateLightPaths(k);
    }

    private void updateLightPaths(int j) {
        String cur = "-";
        for (int i = 0; i < linkIDs.size(); i++) {
            if (!table[i][j].equals(cur)) {
                changeLightPathWavelength(getTableEntry(i,j),wavelengths.get(j));
                //getLightPath(table[i][j]).setWavelength(wavelengths.get(j));
                cur = table[i][j];
            }
        }
    }

    private void changeLightPathWavelength(String id, double wavelength){
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
