package Defragmentation;

import Test.Link;
import Test.Node;
import Test.RoutingTable;
import Test.Signal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 7/12/2016.
 */
public class LightPathBuilder {
    List<Node> nodes;
    List<Signal> signalsAll;
    List<LightPath> lightPaths;
    IdentifierGenerator idGenerator = new IdentifierGenerator();

    public LightPathBuilder(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<LightPath> build() {
        signalsAll = new ArrayList<>();
        extractSignals();
        lightPaths = new ArrayList<>();
        buildLightPaths();
        return lightPaths;
    }

    private void buildLightPaths() {
        while (signalsAll.size() > 0) {
            List<Signal> signals = getSimilarSignals(signalsAll.get(0));
            lightPaths.add(new LightPath(signals, idGenerator.nextID()));
        }
    }

    private List<Signal> getSimilarSignals(Signal signal) {
        List<Signal> list = new ArrayList<>();
        list.add(signal);
        signalsAll.remove(signal);
        for (Signal s : signalsAll) {
            if (s.isSameSignal(signal)) {
                list.add(s);
                signalsAll.remove(s);
            }
        }
        return list;
    }

    private void extractSignals() {
        for (Node n : nodes) {
            List<Signal> currentSignals = n.getCurrent();
            for (Signal s : currentSignals) {
                addSignal(s);
            }
        }
    }

    private void addSignal(Signal signal) {
        signalsAll.add(signal);
    }

    public List<LightPath> getLightPaths() {
        return lightPaths;
    }
}
