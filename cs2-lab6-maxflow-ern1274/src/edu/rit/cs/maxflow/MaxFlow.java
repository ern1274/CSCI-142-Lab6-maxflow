package edu.rit.cs.maxflow;

import edu.rit.cs.labgraph.Edge;
import edu.rit.cs.labgraph.GraphException;
import edu.rit.cs.labgraph.FlowGraph;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * A program that reads in a graph and computes the maximum flow possible
 * from the designated source node to the designated sink node
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class MaxFlow {

    /**
     * Provided for strings that have new-line characters in them
     */
    private static final String NEW_LINE = System.lineSeparator();

    /**
     * The default name of the source node if not given on the command line
     */
    public static final String DEFAULT_SOURCE = "source";

    /**
     * The default name of the sink node if not given on the command line
     */
    public static final String DEFAULT_SINK = "sink";

    /**
     * A function that finds the bottleneck of the path, ignores full nodes
     * by iterating over each junctions connections and comparing the available flow between each
     * @param path List<String> :the path that will be used to take each edge and compare the flow
     * @param graph FlowGraph: the graph holding all the edges
     * @return long: the Maximum amount of flow that can be added to the current path
     */
    private static long bottleneck(List<String> path,FlowGraph graph){
        long bottleneck = graph.getAvailableFlow(path.get(0),path.get(1));
        for (int i = 1; i < path.size()-1; i++) {
            long edgeflow =graph.getAvailableFlow(path.get(i),path.get(i+1));
            if(edgeflow <bottleneck){
                bottleneck = edgeflow;
            }
        }
        return bottleneck;
    }

    /**
     * Attempts to find the maximum amount of flow through BFS and recursively doing it until BFS returns no path
     * Also uses the minimum amount of flow needed to transfer/ bottlneck
     * @param graph FlowGraph: the graph to be used for BFS and find max flow
     * @return long: the maximum amount of flow
     */
    private static long maxFlow(FlowGraph graph){
        long maxflow = 0;
        Optional<List<String>> pathfinder = graph.doBFS();
        List<String> path = pathfinder.get();
        if(path.isEmpty()){
            return 0;
        }
        else{
            System.out.print("Trying ");
            path.stream().forEach(s->System.out.print(s+"->"));
            long bottleneck = bottleneck(path,graph);
            System.out.print("; "+"adding "+bottleneck+" to path");
            System.out.println();
            for (int i = 0; i < path.size()-1; i++) {
                Edge edge = graph.getEdge(path.get(i),path.get(i+1));
                edge.changeFlow(path.get(i),path.get(i+1),bottleneck);
            }
            maxflow+=bottleneck;
        }
        long secondrun = maxFlow(graph);
        return maxflow+secondrun;
    }

    /**
     * Process the command line, create a FlowGraph, and perform the
     * Max Flow algorithm on it.
     * Details of output format are shown in the lab document.
     * @param args [0] graph file name; [1],[2] source and sink names (optional)
     */
    public static void main( String[] args ) {
        try {
            FlowGraph graph = readFlowGraph( args );

            System.out.println( "Initial Graph:" + NEW_LINE );
            graph.show( true );
            System.out.println();


            // Uncomment these lines after you have finished the in-lab part.
            final long maxFlow = maxFlow( graph );
            System.out.println( "Max flow is " + maxFlow );

            System.out.println( NEW_LINE + "Solution:" + NEW_LINE );
            graph.show( false );

        }
        catch( IOException ioe ) {
            System.err.println( "File IO Problem" );
            System.out.println( ioe.getMessage() );
        }
        catch( GraphException ge ) {
            System.err.println( ge.getMessage() );
        }
    }

    /**
     * Build a FlowGraph object from the command line arguments and
     * the named edge-list file.
     * @param args [0] graph file name; [1],[2] source and sink names (optional)
     * @return a fully built FlowGraph object
     * @throws GraphException if there is a problem with the command line
     *                         values or the contents of the file
     * @throws IOException if there is a problem opening or reading the file
     */
    private static FlowGraph readFlowGraph( String[] args ) throws
            GraphException, IOException {
        boolean nonStdEndpoints = args.length == 3;
        if ( !( args.length == 1 || nonStdEndpoints ) ) {
            System.err.println(
                    "Usage: java MaxFlow graph-file source sink"
            );
            System.exit( 1 );
        }

        // Read the source and sink nodes from the command line.
        String source = nonStdEndpoints ? args[ 1 ] : DEFAULT_SOURCE;
        String sink = nonStdEndpoints ? args[ 2 ] : DEFAULT_SINK;
        if ( source.equals( sink ) ) {
            throw new GraphException(
                    "Source and sink are the same: " + sink );
        }

        // Read the graph into a variable called graph.
        return new FlowGraph( args[ 0 ], source, sink );

    }

}
