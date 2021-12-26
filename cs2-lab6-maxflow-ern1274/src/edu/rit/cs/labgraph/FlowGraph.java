package edu.rit.cs.labgraph;

import java.io.*;
import java.util.*;

/**
 * A Graph representation where nodes are just strings that contain no data,
 * and edges are named by their node endpoints, their flow, and their flow
 * capacity.
 * This graph class was custom-designed for the Max Flow Problem.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class FlowGraph {

    /**
     * The starting node for the {@link #doBFS()} method
     */
    private final String source;

    /**
     * The destination node for the {@link #doBFS()} method
     */
    private final String sink;

    /**
     * The internal graph data structure: a map where each node name
     * is associated with the set of edges connected to it
     */
    private final LinkedHashMap< String, LinkedHashSet< Edge > > adjList;

    /**
     * <em>This constant is only used for testing.</em>
     */
    public static final long CAP = 4L;

    /**
     * Build a fixed graph for testing.
     * <em>This method is here only for testing.</em>
     * <pre>
     *        B
     *      / | \
     *     A  |  D
     *      \ | /
     *        C
     * </pre>
     */
    public FlowGraph(){
        final String A = "A";
        final String B = "B";
        final String C = "C";
        final String D = "D";

        Edge eAB = new Edge( A, B, CAP );
        Edge eAC = new Edge( A, C, CAP );
        Edge eBC = new Edge( B, C, CAP );
        Edge eDB = new Edge( D, B, CAP );
        Edge eDC = new Edge( D, C, CAP );

        this.source = A;
        this.sink = D;
        this.adjList = new LinkedHashMap<>();

        for ( String node: List.of( A, B, C, D ) ) {
            this.adjList.put( node, new LinkedHashSet<>() );
        }
        this.adjList.get( A ).add( eAB );
        this.adjList.get( B ).add( eAB );
        this.adjList.get( A ).add( eAC );
        this.adjList.get( C ).add( eAC );
        this.adjList.get( B ).add( eBC );
        this.adjList.get( C ).add( eBC );
        this.adjList.get( D ).add( eDB );
        this.adjList.get( B ).add( eDB );
        this.adjList.get( D ).add( eDC );
        this.adjList.get( C ).add( eDC );
    }

    /**
     * Constructor of FlowGraph for files
     * @param graphFileName String: file name to be open and read
     * @param source String: The "Starting" node
     * @param sink String: The "Ending" node
     * @throws GraphException
     * @throws IOException
     */
    public FlowGraph(String graphFileName,String source,String sink)throws GraphException, IOException {
        System.out.println("hey");
        BufferedReader reader =
                new BufferedReader(new FileReader((graphFileName)));

        this.source = source;
        this.sink = sink;
        this.adjList = new LinkedHashMap<>();


        String thisline = reader.readLine();
        while (thisline != null) {
            String[] line = thisline.split(" ");
            try {
                int cap = Integer.parseInt(line[2]);
                Edge ed = new Edge(line[0], line[1], cap);
                if (!this.adjList.containsKey(line[0])) {
                    this.adjList.put(line[0], new LinkedHashSet<>());
                    this.adjList.get(line[0]).add(ed);
                } else {
                    this.adjList.get(line[0]).add(ed);
                }
                if (!this.adjList.containsKey(line[1])) {
                    this.adjList.put(line[1], new LinkedHashSet<>());
                    this.adjList.get(line[1]).add(ed);
                } else {
                    this.adjList.get(line[1]).add(ed);
                }
                thisline = reader.readLine();
            }
            catch(IOException ioe){
                System.err.println( "File IO Problem" );
                System.out.println( ioe.getMessage() );
            }
        }
        reader.close();
    }



    /**
     * What edges are connected to this node?
     *
     * @param node the node's name
     * @return A Set of {@link Edge} objects whose in or out node equals
     *         the parameter node
     * @rit.pre node is not null and is in the graph.
     */
    public Set< Edge > getEdgesAt( String node ) {
        return this.adjList.get( node );
    }

    /**
     *The Breadth First Search, looks for the shortest path without confronting full edges
     * @return Optional List<String>: Returns a list of either edges or nothing
     */
    public Optional<List<String>> doBFS(){
        List<String> queue = new LinkedList<>();
        queue.add(source);
        Map<String ,String> predecessors = new HashMap<>();
        predecessors.put(source, source);
        while(!queue.isEmpty()){
            String current = queue.remove(0);
            if(current.equals(sink)){
                break;
            }
            for(Object ed: adjList.get(current)){
                Edge ed2 = (Edge) ed;
                String other = ed2.getOtherEnd(current);
                Edge yo = getEdge(current,other);
                if(!predecessors.containsKey(other)&& yo.getFlow(current,other)!= yo.getCapacity()){
                    predecessors.put(other,current);
                    queue.add(other);
                }
            }
        }
        List<String> path = new LinkedList<>();
        if(predecessors.containsKey(sink)){
            String curr = sink;
            while(!curr.equals(source)){
                path.add(0,curr);
                curr = predecessors.get(curr);
            }
            path.add(0,source);
        }
        return Optional.of(path);
    }

    /**
     * gets the amount flow available from one node to another
     * @param previous String: The "front/first" node
     * @param current String: The "Back/second" node
     * @return long: the available achieved by subtracting current flow
     * from capacity from previous and currents node
     */
    public long getAvailableFlow(String previous, String current){
        LinkedHashSet egg = adjList.get(previous);
        for (Object ed:egg) {
            Edge ed2 =(Edge)ed;
            if(ed2.getOtherEnd(previous).equals(current)){
                return ed2.getCapacity()-ed2.getFlow(previous,current);
            }


        }
        return 0;
    }

    /**
     * gets the edge of two strings and returns it
     * @param a String: the first junction
     * @param b String: second Junction
     * @return Edge: Returns Edge by comparing it with every edge in one of the junctions hashset
     */
    public Edge getEdge(String a, String b){
        LinkedHashSet set = adjList.get(a);
        for (Object ed:set) {
            Edge ed2 = (Edge)ed;
            if(ed2.getOtherEnd(a).equals(b)){
                return ed2;
            }
        }
        return null;
    }

    /**
     * gets the sink junction and returns it
     * @return String: sink junctions name
     */
    public String getSink(){
        return sink;
    }

    /**
     * Gets the source junction and returns it
     * @return String: Source junctions name
     */
    public String getSource(){
        return source;
    }

    /**
     * Based on the boolean, shows each junctions connections and capacity
     * or shows each junctions flow into each other
     * @param showMax
     */
    public void show(boolean showMax){
        if(showMax){
            for (String k:adjList.keySet()) {
                System.out.print(k+": ");
                for (Object ed:adjList.get(k)) {
                    Edge ed2 = (Edge)ed;
                    System.out.print(ed2.toString()+", ");
                }
                System.out.println();
            }
        }
        else{
            for (String k:adjList.keySet()) {
                System.out.print(k+": ");
                for (Object ed:adjList.get(k)) {
                    Edge ed2 = (Edge)ed;
                    System.out.print(ed2.toStringNoMax()+", ");
                }
                System.out.println();
            }
        }
    }

}
