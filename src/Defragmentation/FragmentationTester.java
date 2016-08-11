package Defragmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 7/12/2016.
 */
public class FragmentationTester {
    LinkTable linkTable;
    private double maxWavelength = 1800.0, minWavelength = 1200.0, fragmentation, free, freeMax;

    public FragmentationTester() {
    }

    public void calculateFragmentation(LinkTable linkTable) {
        this.linkTable = linkTable;
        calculateFragmentation();
    }

    private void calculateFragmentation() {
        //
        fragmentation = 0;
        try {
            for (int i = 0; i < linkTable.linkIDs.size(); i++) {
                List<Double> linkWavelengths = getLinkWavelengths(i);
                if (linkWavelengths.size() == 0) {

                } else
                    fragmentation += calculateFragmentation(linkWavelengths);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        linkTable.setFragmentation(fragmentation / linkTable.linkIDs.size() * 100);
        //linkTable.displayFitness();
        //System.out.println(fragmentation);
    }

    private List<Double> getLinkWavelengths(int i) {
        // input : link index
        // output : list containing all occupied wavelengths on given link
        List<Double> linkWavelengths = new ArrayList<>();
        for (int j = 0; j < linkTable.wavelengths.size(); j++) {
            if (linkTable.table[i][j] != "") {
                linkWavelengths.add(linkTable.wavelengths.get(j));
            }
        }
        return linkWavelengths;
    }

    private double calculateFragmentation(List<Double> linkWavelengths) {
        // input : list of a given link's occupied wavelengths
        // output : fragmentation coefficient
        freeMax = -999;
        free = 0;
        double t = 0.1;
        double point1 = minWavelength;
        double point2;
        double separation;
        for (int i = 0; i < linkWavelengths.size(); i++) {
            point2 = linkWavelengths.get(i);
            separation = (point2 - t) - (point1 + t);
            free += separation;
            if (separation > freeMax)
                freeMax = separation;
            point1 = point2;
        }
        point2 = maxWavelength;
        separation = (point2 - t) - (point1 + t);
        free += separation;
        if (separation > freeMax)
            freeMax = separation;

        return (free - freeMax) / free;
    }
}
