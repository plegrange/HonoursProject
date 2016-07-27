package Defragmentation;

import java.util.List;

/**
 * Created by FuBaR on 7/27/2016.
 */
public class Mutator {

    public Mutator() {

    }

    public List<LinkTable> mutate(List<LinkTable> list) {
        for (LinkTable table : list) {
            table.mutate();
        }
        return list;
    }

}
