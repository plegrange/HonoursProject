package Defragmentation;

import Test.Link;
import Test.Node;
import Test.Path;
import Test.Signal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by FuBaR on 7/11/2016.
 */
public class LightPath {
    double wavelength;
    Path route;
    String id;
    List<Signal> signals;

    public LightPath(List<Signal> signals, String id) {
        this.signals = signals;
        this.route = signals.get(0).get_Route();
        this.id = id;
        this.wavelength = signals.get(0).get_Wavelength();
    }

    public double getWavelength() {
        return wavelength;
    }

    public void setWavelength(double wavelength) {
        this.wavelength = wavelength;
    }

    public List<Node> getPath() {
        return route._Path;
    }

    public List<String> getLinkIDs() {
        List<String> ids = new ArrayList<>();
        String newID;
        List<Node> nodes = route._Path;
        for (int i = 0; i < nodes.size() - 1; i++) {
            int newInt = nodes.get(i).getID();
            newID = String.valueOf(newInt);
            newInt = nodes.get(i + 1).getID();
            if (String.valueOf(newInt).compareTo(newID) > 0)
                newID = newID + String.valueOf(newInt);
            else
                newID = String.valueOf(newInt) + newID;
            ids.add(newID);
        }
        return ids;
    }

}
