package Defragmentation;

import java.util.List;

/**
 * Created by FuBaR on 8/9/2016.
 */
public class LinkTableValidator {
    LinkTable _initial, _final;

    public LinkTableValidator(LinkTable A, LinkTable B) {
        _initial = A;
        _final = B;
    }

    public boolean check() {
        List<LightPath> lightPathsInitial = _initial.lightPaths;
        List<LightPath> lightPathsFinal = _final.lightPaths;
        for (LightPath lightPath : lightPathsInitial) {
            if (contains(lightPathsFinal, lightPath)) {
                continue;
            } else
                return false;
        }
        displayImprovement();
        return true;
    }

    private void displayImprovement() {
        String s = String.format("%.2f", _initial.getFragmentation() - _final.getFragmentation());
        System.out.println(s + "%");
    }

    private boolean contains(List<LightPath> list, LightPath item) {
        for (LightPath lightPath : list) {
            if (lightPath.id == item.id) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTable() {
        String[][] table = _final.getTable();
        for (int y = 0; y < _final.wavelengths.size(); y++) {
            String temp = "";
            for (int x = 0; x < _final.linkIDs.size(); x++) {
                if (temp == "") {
                    temp = table[x][y];
                } else if (temp != table[x][y] && table[x][y] != "") {
                    return false;
                }
            }
        }
        displayImprovement();
        return true;
    }
}
