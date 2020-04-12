import java.nio.file.Paths;
import java.util.Iterator;

import static SimulatedAnnealing.ColoringGraph.*;

public class Main {

    public static void main(String[] args) throws Exception {

//        String[] myColors = generateColors(138);
//        testGraphColor(myColors);

        String filePath;
        if (args.length > 0) {
            filePath = args[0];
        } else {
            filePath = Paths.get("put the file path for the graph").toAbsolutePath().toString();
        }

        SimulatedAnnealing.Graph graph = readGraphFromTxt(filePath);
//        fillGraph(g1_adj, graph);
        System.out.println("Load graph from : " + filePath);
        System.out.println(graph);

        //assign color based on maximum degree
//        int maxDegree = graph.getMaxDegree();
//        String[] colorList = generateColors(maxDegree + 1);
        String[] colorList = generateColors(4);
        System.out.println(colorList.length + " color used for graph at max.");

        while (true) {
            try {
                graph = assignColor(graph, colorList);
                break;
            } catch (Exception e) {
                //NOP
            }
        }
//        System.out.println("First assigned Color:");
//        for (Iterator it = graph.iterator(); it.hasNext(); ) {
//            int node = (int) it.next();
//            System.out.println(node + " : " + graph.getVertexColor(node));
//        }

        int temperature = 100;
        int T0=temperature;
        int k=0;
        double firstAlpha = 0.85;
        double secondAlpha = 1.9;
        double thirdFourthAlpha = 0.6;
        graph = simulatedAnnealing(graph, colorList, temperature,T0,k,firstAlpha);
//        graph = simulatedAnnealing(graph, colorList, temperature,T0,k,secondAlpha);
//        graph = simulatedAnnealing(graph, colorList, temperature,T0,k,thirdFourthAlpha);


        System.out.println(isColoringValid(graph));
        System.out.println("loss=" + lossFunction(graph));
        System.out.println("Final assigned Color:");
        for (Iterator it = graph.iterator(); it.hasNext(); ) {
            int node = (int) it.next();
            System.out.print(node + " : " + graph.getVertexColor(node) + " ");
        }
        System.out.println();
        System.out.println(repatedState);
    }
}
