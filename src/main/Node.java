package main;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author Dov Neimand
 */
public class Node {

    /**
     * The neighbors of this node.
     */
    private ArrayList<Node> neighbors;
    
    /**
     * The distance of this node from the root.
     */
    private int height;
    /**
     * Has this node been selected for the failure set, S.
     */
    private boolean isSelectedForFailureSet;
    /**
     * Is this node near a node in the failure set? If so, this list will
     * contain the ID of that failure.
     */
    private boolean isNearFailedNode;

    /**
     * The name of this node.
     */
    private String name;

    /**
     * A constructor. The caller of this constructor will need to set the name and
     * neighbors manually.
     */
    public Node() {
        neighbors = new ArrayList<>(2);
        height = Integer.MAX_VALUE;
        isSelectedForFailureSet = false;
        isNearFailedNode = false;
    }

    /**
     * A constructor. The caller will need to set the neighbors of this node.
     *
     * @param name the name of this node.
     */
    public Node(String name) {
        this();
        this.name = name;
    }

    /**
     * A constructor
     *
     * @param parent the node intended to be the parent of this node. It will
     * still be necessary to declare the root node.
     * @param name
     */
    public Node(Node parent, String name) {
        this(name);
        addNeigbor(parent);
    }

    /**
     * Sets this node, and its neighbors, as being near a failed node
     * This method may redundantly set nodes as nearFailedNode.  This redundancy 
     * can be avoided by passing a hashset of visited nodes, if the present 
     * Node is in the set and has already been visited, then stop.  If not,
     * then add the present node to the list of visited nodes.However for the 
     * human reader's sake we accept the redundancy.
     */
    private void setNearSelected(int d) {
        if (d < 0) return;
        isNearFailedNode = true;
        neighbors.forEach(node -> node.setNearSelected(d - 1));
    }

    /**
     * selects this node and its neighbors as failed nodes.
     *
     * @param neighborDistance the distance from this node that neighbors are to
     * be removed.
     * @return the failure's ID. Every failure generates a unique ID that needs
     * to be used in order to cancel the failure.
     */
    private void selectNode(int neighborDistance) {

        if (isSelectedForFailureSet)
            throw new RuntimeException("This node is already selected");

        isSelectedForFailureSet = true;
        setNearSelected(neighborDistance);
    }


    /**
     * The children of this node.
     *
     * @return a stream of children for this node.
     */
    public Stream<Node> children() {
        return neighbors.stream().parallel().filter(node -> node.getHeight() > getHeight());
    }

    private int componentSize;

    /**
     * Sets and returns the component size of all elements in the subtree.
     *
     * @return the component size of this node.
     */
    private int componentSize() {
        if (isNearFailedNode) return componentSize = 0;
        if (isLeaf()) return componentSize = 1;

        return componentSize = 1 + children().mapToInt(child -> child.componentSize()).sum();
    }

    /**
     * Gets the components size of this element. Take care that this information
     * is up to date, as it is not renewed internally. To update, call
     * setComponentSize().
     *
     * @return
     */
    public int getComponentSize() {
        return componentSize;
    }

    /**
     * Sets the components size of all elements in this subtree.
     */
    public void setComponentSizes() {
        componentSize();
    }

    /**
     * Sets the height of this node, and all of its descendants, this should be
     * the distance from the root.
     *
     * @param height the height of this node.
     */
    private void setHeight(int height) {
        this.height = height;
        children().forEach(child -> child.setHeight(height + 1));

    }

    /**
     * Sets this node as the root. This will establish all of the heights in the
     * tree.
     */
    public void setAsRoot() {
        setHeight(0);
    }
  
    /**
     * Is this node designated as a root node?
     * @return true if the node is designated as a root node, false otherwise
     */
    public boolean isRoot(){
        return getHeight() == 0;
    }

    /**
     * Is this node a leaf
     *
     * @return true if this node is a leaf, false otherwise
     */
    private boolean isLeaf() {
        return neighbors.size() == 1 && !isRoot() || neighbors.isEmpty();
    }


    /**
     * Finds a minimal set of nodes for failure.
     *
     * @param maxSurvivingComponentSize the maximum allowable size of a
     * surviving component, k.
     * @param neighborDistance the distance of a node from a failed node to be
     * considered near to it.
     */
    public void setSelections(int maxSurvivingComponentSize, int neighborDistance) {

        children().forEach(child -> child.setSelections(maxSurvivingComponentSize, neighborDistance));

        setComponentSizes();

        if(componentSize < maxSurvivingComponentSize) return;
        
        if (!parentCanHandleThis(neighborDistance, maxSurvivingComponentSize)
                || (isRoot() && containsIllegalComponent(maxSurvivingComponentSize, -1)))

            selectNode(neighborDistance);

    }

    /**
     * When deciding if a node can be a failed node, we consider if the parent
     * being a failed node would similarly prevent component sizes from being
     * too big. .
     *
     * @param neighborDistance the distance of a node from a failed node so that
     * it is considered nearby.
     * @param maxCompnonetSize the largest allowable component size.
     * @return if the parent being a failed node would remove the need for this
     * node to be a failed node, true. Otherwise false.
     */
    private boolean parentCanHandleThis(int neighborDistance, int maxCompnonetSize) {
        return !containsIllegalComponent(maxCompnonetSize, neighborDistance - 1);
    }

    /**
     * Does this sub tree contain any components that exceed the max component
     * size. 
     * 
     * @param maxComponentSize the maximum size a component may have without
     * requiring without requiring additional nodes to fail.
     * @param searchDistance How much farther needs to be searched for an illegal component
     * @return true if this node's sub tree contains components greater than the
     * max component size, false otherwise.
     */
    private boolean containsIllegalComponent(int maxComponentSize, int searchDistance) {

        if (searchDistance < 0 && isNearFailedNode) return false;

        if (searchDistance >= 0 && children().anyMatch(child -> child.containsIllegalComponent(maxComponentSize, searchDistance - 1)))
            return true;

        return searchDistance < 0 && getComponentSize() > maxComponentSize;
    }

    /**
     * The number of nodes in a smallest node failure set.
     *
     * @return The number of nodes in a smallest node failure set.
     */
    public int numSelected() {
        return children()
                .mapToInt(node -> node.numSelected())
                .sum()
                + (isSelectedForFailureSet ? 1 : 0);
    }

    /**
     * Adds a neighbor to this node.
     *
     * @param neighbor the neighbor to be added.
     * @return this node.
     */
    public final Node addNeigbor(Node neighbor) {
        neighbors.add(neighbor);
        neighbor.neighbors.add(this);
        return this;
    }

    /**
     * Sets the name of this node.
     *
     * @param name The new name.
     * @return this node.
     */
    public Node setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Does this node have children.
     *
     * @return true if this node has children, false otherwise.
     */
    public boolean hasChildren() {
        if (isRoot()) return !neighbors.isEmpty();
        return neighbors.size() > 1;
    }

    @Override
    public String toString() {
        return toString("");
    }

    /**
     * see @toString()
     *
     * @param indent the indentation to precede this toString.
     * @return an indented String.
     */
    public String toString(String indent) {
        String local = indent + name + ": " + "\n"
                + indent + "failed = " + isSelectedForFailureSet + "\n"
                + indent + "isNearFailed = " + isNearFailedNode + "\n";
//                + indent + "component size = " + getComponentSize() + "\n";
        if (hasChildren()) {
            local += indent + "children: " + children().map(child -> child.name).reduce((s1, s2) -> s1 + ", " + s2).get() + "\n";

            return local
                    + children().map(child -> child.toString(indent + "\t")).reduce((s1, s2) -> s1 + s2).get();
        }
        return local;
    }

    /**
     * The height of this node.
     *
     * @return the height of this node.
     */
    public int getHeight() {
        return height;
    }

    /**
     * The name of this node.
     *
     * @return the name of this node.
     */
    public String getName() {
        return name;
    }
}