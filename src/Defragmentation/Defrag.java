package Defragmentation;

import ExcelWriter.LinkTableOutput;
import Test.Link;
import Test.Node;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.List;

/**
 * Created by FuBaR on 7/12/2016.
 */
public class Defrag {

    LightPathBuilder lightPathBuilder;
    List<LightPath> lightPaths;
    TableBuilder tableBuilder;
    LinkTable linkTable;
    GeneticProgram geneticProgram;
    LinkTableOutput tableWriter = new LinkTableOutput("C://Users//FuBaR//Documents//ACO-New Pheromone/tableOutput.xls");

    public Defrag(List<Node> nodes) {

        lightPathBuilder = new LightPathBuilder(nodes);
        lightPaths = lightPathBuilder.build();
        tableBuilder = new TableBuilder(lightPaths);
        tableBuilder.buildTable();
        linkTable = tableBuilder.linkTable;
        /*try {
            tableWriter.write(linkTable);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }*/
        geneticProgram = new GeneticProgram(linkTable);
        linkTable = geneticProgram.linkTable;
    }
}
