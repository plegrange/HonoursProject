package Defragmentation;

import java.util.Collections;
import java.util.List;

/**
 * Created by FuBaR on 7/27/2016.
 */
public class Merger {
    public Merger() {
    }

    public List<LinkTable> merge(List<LinkTable> A, List<LinkTable> B) {
        for (LinkTable b : B) {
            A.add(b);
        }
        return A;
    }
}
