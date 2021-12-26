package edu.rit.cs.labgraph;

/**
 * A class representing graph edges that are labeled with flow values.
 * This class was custom designed for the Max Flow problem.
 * @author YOUR NAME HERE
 */
public class Edge {

    /**
     * How much traffic, or flow, this edge can handle,
     * in either direction (but not in both)
     */
    private final long capacity;

    /**
     * The name of the first node.
     * When flow is assigned, it is positive if the flow comes
     * <em>from</em> this node.
     */
    private final String in;

    /**
     * The name of the second node.
     * When flow is assigned, it is positive if the flow goes
     * <em>toward</em> this node.
     */
    private final String out;

    /**
     * The flow in the direction from in to out.
     * Negative if the flow is in the opposite direction.
     */
    private long flow;

    /**
     * Create a new edge with an initial flow of 0.
     * @param in The name of the first node.
     * When flow is assigned, it is positive if the flow comes
     * <em>from</em> this node.
     * @param out The name of the second node.
     * When flow is assigned, it is positive if the flow comes
     * <em>towards</em> this node.
     * @param capacity the maximum flow that can travel through this edge
     *                 in either direction.
     */
    public Edge( String in, String out, long capacity ) {
        this.in = in;
        this.out = out;
        this.capacity = capacity;
        this.flow = 0;
    }

    /**
     * Returns the amount of flow available from the current flow
     * @param in String: first junction
     * @param out String: Second Junction
     * @return long: The available flow by subtraction capacity from flow and direction
     */
    public long availableFlow(String in, String out){
        return capacity - flow*direction(in,out);
    }

    /**
     * Adds to or Subtracts from the flow based on direction of flow
     * @param in String: the first junction
     * @param out String: the second junction
     * @param delta long: the subtraction or addition to flow
     */
    public void changeFlow(String in, String out, long delta){
        flow = (flow+(direction(in,out)*delta));

    }

    /**
     * Returns the capacity of the edge
     * @return long: Capacity of the edge
     */
    public long getCapacity(){
        return this.capacity;
    }

    /**
     * Determines the direction of the edge by using 1 and -1
     * If the inner junction matches the parameter in then the in junction is flowing towards out
     * so return one
     * the inverse if doesnt match
     * @param in String in: the first junction
     * @param out String out: the second junction
     * @return int: returns 1 or -1 depending if out is flowing towards in or in towards out
     */
    public int direction(String in, String out){
        if(this.in.equals(in)){
            return 1;
        }
        else{
            return -1;
        }
    }

    /**
     * Indicate all the information stored in this edge.
     * @return a String in the format "<code>[in==>flow/capacity==>out]</code>"
     */
    @Override
    public String toString() {
        return "[" + this.in +
               "==>" +
               this.direction( this.in, this.out ) * this.flow +
               '/' +
               this.capacity +
               "==>" +
               this.out + ']';
    }

    /**
     * Indicate node names and flow stored in this edge.
     * @return a String in the format "<code>[in==>flow==>out]</code>"
     */
    public String toStringNoMax() {
        return "[" + this.in +
               "==>" +
               this.direction( this.in, this.out ) * this.flow +
               "==>" +
               this.out + ']';
    }

    /**
     * gets the out junction
     * @return String: the name of the outer junction
     */
    public String getOutNode(){
        return this.out;
    }

    /**
     * Gets the other side of an edge using the first junction
     * @param thisEnd String: one side of the edge
     * @return String: the other junction
     */
    public String getOtherEnd(String thisEnd){
        if(this.in.equals(thisEnd)){
            return this.out;
        }
        else{
            return this.in;
        }
    }

    /**
     * Similar to the getoutnode() function
     * Gets the inner junction aka the first junction flowing towards the second
     * @return String: the inner junction name
     */
    public String getInNode(){
        return this.in;
    }

    /**
     * Gets the flow based on the directions its coming in using the parameters
     * @param in String: first junction
     * @param out String: second junction
     * @return long: the flow between the two junctions based on direction
     */
    public long getFlow(String in, String out){
        return this.flow*direction(in,out);
    }

}
