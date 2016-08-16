package Defragmentation;

import ExcelWriter.LinkTableOutput;
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
    LinkTable linkTableInitial, linkTableFinal;
    GeneticProgram geneticProgram;

    LinkTableOutput initialTableWriter = new LinkTableOutput("initial.xls");
    LinkTableOutput finalTableWriter = new LinkTableOutput("final.xls");
    //LinkTableOutput initialTableWriter = new LinkTableOutput("C://Users//FuBaR//Documents//ACO-New Pheromone/initial.xls");
    //LinkTableOutput finalTableWriter = new LinkTableOutput("C://Users//FuBaR//Documents//ACO-New Pheromone/final.xls");
    LinkTableValidator validator;

    public Defrag(List<Node> nodes) {

        lightPathBuilder = new LightPathBuilder(nodes);
        lightPaths = lightPathBuilder.build();
        tableBuilder = new TableBuilder(lightPaths);
        tableBuilder.buildTable();
        linkTableInitial = tableBuilder.linkTable;
        try {
            initialTableWriter.write(linkTableInitial);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        geneticProgram = new GeneticProgram(linkTableInitial);
        linkTableFinal = geneticProgram.bestLinkTable;
        validator = new LinkTableValidator(linkTableInitial, linkTableFinal);
        try {
            finalTableWriter.write(linkTableFinal);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        System.out.println(validator.checkTable());
    }
}
