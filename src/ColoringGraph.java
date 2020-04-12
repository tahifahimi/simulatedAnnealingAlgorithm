package SimulatedAnnealing;
//Tahere Fahimi 9539045
//AmirKabir University of Technology
//Solve coloring graph problem with simulated annealing
// report is attached to the codes
//use this link and  solve the problem:
//https://stackoverflow.com/questions/51635658/graph-coloring-with-using-simulated-annealing

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.DataFormatException;

public class ColoringGraph {
    public static int repatedState =0;
    //This function receives an integer, & generates that much random colors.;
    //     *
    //     * @param number_of_colors
    //     * @return
    //     * @throws Exception
    public static String[] generateColors(int number_of_colors) throws Exception {
//        String[] colorNames = {"Blue", "RED", "Green", "WHITE", "Red", "Yellow", "Pink", "Tomato", "Turquoise", "Violet", "LimeGreen", "AliceBlue", "AntiqueWhite", "Aquamarine", "Azure", "Beige", "Bisque", "BlanchedAlmond", "BlueViolet", "BurlyWood", "CadetBlue", "Chartreuse", "Chocolate", "Coral", "CornflowerBlue", "Cornsilk", "Cyan", "DarkBlue", "DarkCyan", "DarkGoldenRod", "DarkGray", "DarkGrey", "DarkGreen", "DarkKhaki", "DarkMagenta", "DarkOliveGreen", "Darkorange", "DarkOrchid", "DarkRed", "DarkSalmon", "DarkSeaGreen", "DarkSlateBlue", "DarkSlateGray", "DarkTurquoise", "DarkViolet", "DeepPink", "DeepSkyBlue", "DimGray", "DimGrey", "DodgerBlue", "FireBrick", "FloralWhite", "ForestGreen", "Gainsboro", "GhostWhite", "Gold", "GoldenRod", "Gray", "Grey", "GreenYellow", "HoneyDew", "HotPink", "IndianRed", "Ivory", "Khaki", "Lavender", "LavenderBlush", "LawnGreen", "LemonChiffon", "LightBlue", "LightCoral", "LightCyan", "LightGoldenRodYellow", "LightGray", "LightGrey", "LightGreen", "LightPink", "LightSalmon", "LightSeaGreen", "LightSkyBlue", "LightSlateGray", "LightSlateGrey", "LightSteelBlue", "LightYellow", "MediumAquaMarine", "MediumBlue", "MediumOrchid", "MediumPurple", "MediumSeaGreen", "MediumSlateBlue", "MediumSpringGreen", "MediumTurquoise", "MediumVioletRed", "MidnightBlue", "MintCream", "MistyRose", "Moccasin", "NavajoWhite", "Navy", "OldLace", "OliveDrab", "OrangeRed", "Orchid", "PaleGoldenRod", "PaleGreen", "PaleTurquoise", "PaleVioletRed", "PapayaWhip", "PeachPuff", "Peru", "Plum", "PowderBlue", "RosyBrown", "RoyalBlue", "SaddleBrown", "Salmon", "SandyBrown", "SeaGreen", "SeaShell", "Sienna", "SkyBlue", "SlateBlue", "SlateGray", "SlateGrey", "Snow", "SpringGreen", "SteelBlue", "Tan", "Thistle", "Wheat", "White", "WhiteSmoke"};
        //maximum number of colors are 4:
        String[] colorNames = {"RED","BLUE","GREEN","YELLOW","WHITE"};
        String[] list_of_colors = new String[number_of_colors];
        if (colorNames.length < number_of_colors) {
            throw new Exception("not enough defined color");
        }
        for (int i = 0; i < number_of_colors; i++) {
            list_of_colors[i] = colorNames[i];
        }

        return list_of_colors;
    }


    /**
     * This function receives a graph & a list of colors as input, & assigns a valid color in the list to the;
     * nodes of the graph & returns the result graph.;
     *
     * @param graph
     * @param color
     * @return
     * @throws Exception
     */
    public static SimulatedAnnealing.Graph assignColor(SimulatedAnnealing.Graph graph, String[] color) throws Exception {

        for (Iterator it = graph.iterator(); it.hasNext(); ) {
            int node = (int) it.next();
            graph.addVertexColor(node, "0");
        }

        for (Iterator it = graph.iterator(); it.hasNext(); ) {
            int node = (int) it.next();
            ArrayList<String> adjacent_colors = new ArrayList<>();

            graph.getVertexNeighbour(node).stream().forEach(n -> adjacent_colors.add(graph.getVertexColor(n)));

            int cnt = 0;

            while ((graph.getVertexColor(node).equals("0")) || (adjacent_colors.contains(graph.getVertexColor(node)))) {
                graph.addVertexColor(node, color[new Random().nextInt(color.length - 1)]);
                cnt++;
                if (cnt > 1000) {
                    throw new Exception("Assignment is not possible");
                }
            }
        }
        return graph;
    }

    /**
     * Assign a random color for given node.
     * this assignment maybe not valid you should check with {@link #isColoringValid} method.
     *
     * @param graph
     * @param color
     * @param node
     * @return
     * @throws Exception
     */
    public static SimulatedAnnealing.Graph assignColor(SimulatedAnnealing.Graph graph, String[] color, int node) {
        graph.addVertexColor(node, color[new Random().nextInt(color.length)]);
        return graph;
    }

    /**
     * check validity of colors for graph nodes.
     *
     * @param graph
     * @return
     */
    public static boolean isColoringValid(SimulatedAnnealing.Graph graph) {
        for (Iterator it = graph.iterator(); it.hasNext(); ) {
            int node = (int) it.next();
            ArrayList<String> adjacent_colors = new ArrayList<>();

            graph.getVertexNeighbour(node).stream().forEach(n -> adjacent_colors.add(graph.getVertexColor(n)));

            if ((graph.getVertexColor(node).equals("0")) || (adjacent_colors.contains(graph.getVertexColor(node)))) {
                return false;
            }
        }

        return true;
    }


    /**
     * This function calculates the loss-function for each state;
     * Loss value is public voidined to be the number of colors used for coloring a graph
     */
    public static int lossFunction(SimulatedAnnealing.Graph graph) {
        Set<String> usedColors = new HashSet<>();
        for (Iterator it = graph.iterator(); it.hasNext(); ) {
            int node = (int) it.next();
            usedColors.add(graph.getVertexColor(node));
        }

        return usedColors.size();
    }


    public static double getProbability(int oldValue, int newValue, double t) {
        double changed_energy_level = newValue - oldValue;

        if (changed_energy_level <= 0) {
            return changed_energy_level;
        } else {
            return Math.E * (changed_energy_level / t);
        }
    }

    /**
     * This function receives a graph, a node of that graph, a color & current temperature;
     * & returns the probability of accepting the next state.;
     * Next state is the input graph with colored input node with input color;
     */
    public double assign_probability(SimulatedAnnealing.Graph graph_, int node_, String color_, int t) {
        int current_value = (-1) * lossFunction(graph_);
//        graph_.nodes[node_]['color'] = color_;
        int next_value = (-1) * lossFunction(graph_);
        int changed_energy_level = next_value - current_value;
        if ((changed_energy_level >= 0)) {
            return 1;
        } else ;
        {
            return Math.E * (changed_energy_level / t);
        }
    }


    public static SimulatedAnnealing.Graph simulatedAnnealing(SimulatedAnnealing.Graph inputGraph, String[] colors, double t, int T0, int k, double alpha) {

        SimulatedAnnealing.Graph tmpGraph, graph;
        System.out.println("SA t=" + t + " ColorNumber="+ lossFunction(inputGraph));
        repatedState =repatedState+1;
        if (t < 1) {
            return inputGraph;
        }
        SimulatedAnnealing.Graph initialColoredGraph = (SimulatedAnnealing.Graph) inputGraph.copy();
        graph = (SimulatedAnnealing.Graph) inputGraph.copy();

//        System.out.println("isvalid(copy)="+isColoringValid(initialColoredGraph));
        for (Iterator it = graph.iterator(); it.hasNext(); ) {
            int node = (int) it.next();
            int oldValue = lossFunction(graph);

            //new neighborhood
            for (int i = 0; i < 20; i++) {
                tmpGraph = assignColor(graph, colors, node);
                if (!isColoringValid(tmpGraph)) {
                    graph = (SimulatedAnnealing.Graph) initialColoredGraph.copy();
                    continue;
                }
                graph = tmpGraph;
                int newValue = lossFunction(graph);

                double probability = getProbability(oldValue, newValue, t);

                if (probability <= 0) {
                    //Accept better result
//                    System.out.println("Accept better result.");
                    initialColoredGraph = (SimulatedAnnealing.Graph) graph.copy();
                    oldValue = newValue;
                } else if (probability < 0.0500) {
                    // Accept with if Delta_E < 0.13820
                    initialColoredGraph = (SimulatedAnnealing.Graph) graph.copy();
                    oldValue = newValue;
//                    System.out.println("accept result with probability=" + probability);
                }
                else
                {
                    graph = (SimulatedAnnealing.Graph) initialColoredGraph.copy();
                }

            }
        }

//        t -= 1;
        k = k+1;
        t = changeTempreture(T0,k,alpha);

        graph = simulatedAnnealing(initialColoredGraph, colors, t,T0,k,alpha);

        return graph;
    }

    private static double changeTempreture(int t0, int k , double alpha) {
        //first implementation
        if (alpha>=0.8 && alpha<=0.9)
            return t0*Math.pow(alpha,k);
        else {
            System.out.println("errrorrrr");
            return t0-1;
        }

        //second implementation
//        if (alpha>1)
//            return t0/(1+Math.log10(1+k)*alpha);
//        else {
//            System.out.println("Error !!!!");
//            return t0-1;
//        }

        //third implementation
//        if (alpha>0)
//            return t0/(1+alpha*k);
//        else {
//            System.out.println("error detected!!!");
//            return t0-1;
//        }

        //fourth implementation
//        if (alpha>0)
//            return t0/(1+alpha*k*k);
//        else {
//            System.out.println("unreachable!!!!!");
//            return t0-1;
//        }

    }

    /**
     * Fill graph with adj matrix
     *
     * @param adjMatrix
     * @param graph
     * @return
     */
    public static SimulatedAnnealing.Graph fillGraph(int[][] adjMatrix, SimulatedAnnealing.Graph graph) {
        ArrayList<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            nodes.add(i);
        }
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                if (adjMatrix[i][j] == 1) {
                    graph.addEdge(nodes.get(i), nodes.get(j));
                }
            }
        }
        return graph;
    }

    public static SimulatedAnnealing.Graph readGraphFromTxt(String filePath) throws IOException, DataFormatException {
        SimulatedAnnealing.Graph graph = new SimulatedAnnealing.Graph();

        File readFile = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(readFile.getCanonicalPath()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                String[] vertices = line.split("\\s");
                if (vertices.length != 2) {
                    throw new DataFormatException(line + "  should contain exactly 2 integers");
                }
                try {

                    int vertexV = Integer.parseInt(vertices[0]);
                    int vertexW = Integer.parseInt(vertices[1]);

                    graph.addEdge((vertexV), (vertexW));

                } catch (NumberFormatException exception) {
                    String msg = "One of the following is not intepretible as an integer: "
                            + vertices[0] + " " + vertices[1];
                    throw new NumberFormatException(msg);
                }
            }
        }
        return graph;
    }


}