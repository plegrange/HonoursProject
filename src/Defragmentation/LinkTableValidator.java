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
        return true;
    }

    private List<LightPath> removeLightPath(List<LightPath> list, LightPath item) {
        for (LightPath lightPath : list) {
            if (lightPath.id == item.id) {
                list.remove(lightPath);
                break;
            }
        }
        return list;
    }

    private boolean contains(List<LightPath> list, LightPath item) {
        for (LightPath lightPath : list) {
            if (lightPath.id == item.id) {
                return true;
            }
        }
        return false;
    }
}
