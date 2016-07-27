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

    public void calculateFragmentation() {
        fragmentation = 0;
        try {
            for (int i = 0; i < linkIDs.size(); i++) {
                List<Double> linkWavelengths = getLinkWavelengths(i);
                if (linkWavelengths.size() == 0) {

                } else
                    fragmentation += calculateFragmentation(linkWavelengths);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private List<Double> getLinkWavelengths(int i) {
        List<Double> linkWavelengths = new ArrayList<>();
        for (int j = 0; j < wavelengths.size(); j++) {
            if (table[i][j] != null) {
                linkWavelengths.add(wavelengths.get(j));
            }
        }
        return linkWavelengths;
    }

    private double calculateFragmentation(List<Double> linkWavelengths) {
        freeMax = -999;
        free = 0;
        double t = 0.1;
        for (int i = 0; i < linkWavelengths.size() - 1; i++) {
            double point1 = linkWavelengths.get(i);
            double point2 = linkWavelengths.get(i + 1);
            double separation = (point2 - t) - (point1 + t);
            free += separation;
            if (separation > freeMax)
                freeMax = separation;
        }

        return (free - freeMax) / free;
    }

    public double getFragmentation() {
        return fragmentation;
    }

    public void buildTable() {
        setAxes();
        table = new String[linkIDs.size()][wavelengths.size()];
        for (LightPath lightPath : lightPaths) {
            insertLightPathToTable(lightPath);
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
        for (String s : linkIDs) {
            if (s.equals(id))
                return linkIDs.indexOf(s);
        }
        return -1;
    }

    private int getWavelengthIndex(double wavelength) {
        for (Double d : wavelengths) {
            if (d.equals(wavelength))
                return wavelengths.indexOf(d);
        }
        return -1;
    }

    private void setAxes() {
        linkIDs = new ArrayList<>();
        List<Double> counter = new ArrayList<>();
        for (LightPath lightPath : lightPaths) {
            for (String id : lightPath.getLinkIDs()) {
                if (linkIDs.size() == 0) {
                    linkIDs.add(id);
                } else if (!linkIDs.contains(id)) {
                    addLinkID(id);
                }
            }
            if (!counter.contains(lightPath.getWavelength()))
                counter.add(lightPath.getWavelength());
        }
        wavelengths = new ArrayList<>();

        for (int i = 0; i < counter.size(); i++) {

            wavelengths.add(counter.get(i));

        }
        Collections.sort(wavelengths);
    }

    private void addWavelengthID(double wavelength) {
        if (wavelengths.size() == 1) {
            if (wavelength > wavelengths.get(0))
                wavelengths.add(1, wavelength);
            else
                wavelengths.add(0, wavelength);
        } else {
            for (int i = 0; i < wavelengths.size(); i++) {
                if (i == wavelengths.size() - 1) {
                    wavelengths.add(i + 1, wavelength);
                    return;
                } else if (wavelength > wavelengths.get(i) && wavelength < wavelengths.get(i + 1)) {
                    wavelengths.add(i + 1, wavelength);
                    return;
                }
            }
        }
    }

    private void addLinkID(String id) {
        if (linkIDs.size() == 1) {
            if (id.compareTo(linkIDs.get(0)) > 0) {
                linkIDs.add(1, id);

            } else linkIDs.add(0, id);
        } else {
            for (int i = 0; i < linkIDs.size(); i++) {
                if (i == linkIDs.size() - 1) {
                    linkIDs.add(i + 1, id);
                    return;
                } else if (id.compareTo(linkIDs.get(i)) > 0 && id.compareTo(linkIDs.get(i + 1)) < 0) {
                    linkIDs.add(i + 1, id);
                    return;
                }
            }
        }
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
        System.out.println(fragmentation);
    }

    public String getTableEntry(int i, int j) {
        return table[i][j];
    }

    public LinkTable crossover(LinkTable other) {
        LinkTable newLinkTable = new LinkTable();
        return newLinkTable;
    }

    public LinkTable(LinkTable A, LinkTable B) {
        int crossoverPoint = Math.round(A.wavelengths.size() / 2);
        table = new String[A.linkIDs.size()][A.wavelengths.size()];
        for (int i = 0; i < A.linkIDs.size(); i++) {
            for (int j = 0; j < crossoverPoint; j++) {
                table[i][j] = A.getTable()[i][j];
            }
            for (int k = crossoverPoint; k < wavelengths.size(); k++) {
                table[i][k] = B.getTable()[i][k];
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
        String cur = "";
        for (int i = 0; i < linkIDs.size(); i++) {
            if (!table[i][j].equals(cur)) {
                getLightPath(table[i][j]).setWavelength(wavelengths.get(j));
                cur = table[i][j];
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
        Random random = new Random(wavelengths.size());
        return random.nextInt();
    }

    @Override
    public int compareTo(Object o) {
        if (this.fragmentation < ((LinkTable) o).fragmentation) {
            return 1;
        } else if (this.fragmentation > ((LinkTable) o).fragmentation)
            return -1;
        else return 0;
    }
}
