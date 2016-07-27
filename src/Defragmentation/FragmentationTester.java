package Defragmentation;

/**
 * Created by FuBaR on 7/12/2016.
 */
public class FragmentationTester {
    LinkTable linkTable;

    public FragmentationTester() {
    }

    public void calculateFragmentation(LinkTable linkTable) {
        this.linkTable = linkTable;
        linkTable.calculateFragmentation();
    }
}
