package Defragmentation;

import java.util.List;

/**
 * Created by FuBaR on 7/12/2016.
 */
public class TableBuilder {
    LinkTable linkTable;

    public TableBuilder(List<LightPath> lightPaths) {
        linkTable = new LinkTable(lightPaths);
    }

    public void buildTable() {
        linkTable.buildTable();
    }
}
