package main;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Dov Neimand
 */
public class Node {

    /**
     * A class that lets this node have neigbors, and controlls access to them.
     */
    private class Neighbors {

        /**
         * The neighbors of this node.
         */
        private ArrayList<Node> children;
        /**
         * The parent of this node.
         */
        private Node parent;

        /**
         * A constructor.
         * @param parent the parent of this node.
         */
        public Neighbors(Node parent) {
            this.parent = parent;
            this.children = new ArrayList<>();
        }

        /**
         * A constructor for the root node.
         */
        public Neighbors() {
            this(null);
        }
        
        /**
         * Sets the parent of this node.
         * @param parent 
         */
        public void setParent(Node parent){
            this.parent = parent;
        }
        /**
         * Adds a child to this node.
         * @param child 
         */
        public void addChild(Node child){
            children.add(child);
        }
        /**
         * The children of this node.
         * @return 
         */
        public Stream<Node> children(){
            return children.stream();
        }
        /**
         * the neighbors of this node.
         * @return 
         */
        public Stream<Node> all(){
            if(isRoot()) return children();
            return Stream.concat(Stream.of(parent), children());
        }
        
        /**
         * Gets the i'th child
         * @param i the index of the child.
         * @return 
         */
        public Node getChild(int i){
            return children.get(i);
        }
        
        /**
         * The number of children.
         * @return 
         */
        public int numChildren(){
            return children.size();
        }
        
        /**
         * Does this node have children.
         * @return 
         */
        public boolean hasChildren(){
            return numChildren() > 0;
        }
        
        /**
         * Is this the root node.
         * @return 
         */
        public boolean isRoot(){
            return parent == null;
        }
    }
    
    /**
     * The neighbors of this node.
     */
    Neighbors neighbors;
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
     * A constructor. The caller of this constructor will need to set the name
     * and neighbors manually.
     */
    public Node() {
        neighbors = new Neighbors();
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
        neighbors.setParent(parent);
        parent.neighbors.addChild(this);
    }
    
    /**
     * A constructor.
     */
    public Node(Node parent){
        this(parent, null);
    }

    /**
     * Sets this node, and its neighbors, as being near a failed node This
     * method may redundantly set nodes as nearFailedNode. This redundancy can
     * be avoided by passing a hashset of visited nodes, if the present Node is
     * in the set and has already been visited, then stop. If not, then add the
     * present node to the list of visited nodes.However for the human reader's
     * sake we accept the redundancy.
     *
     * @param d This node is considered to be near a failed node if the distance
     * from the failed node is less than d.
     */
    private void setNearSelected(int d) {
        if (d < 0) return;
        isNearFailedNode = true;
        neighbors.all().forEach(node -> node.setNearSelected(d - 1));
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

    private int compSize;

    /**
     * Sets and returns the component size of all elements in the subtree.
     *
     * @return the component size of this node.
     */
    private int setComponentSize() {
        if (isNearFailedNode) return compSize = 0;
        if (!neighbors.hasChildren()) return compSize = 1;

        return compSize = 1
                + neighbors.children().mapToInt(child -> child.setComponentSize()).sum();
    }

    /**
     * Gets the components size of this element. Take care that this information
     * is up to date, as it is not renewed internally. To update, call
     * setComponentSize().
     *
     * @return
     */
    public int getComponentSize() {
        return compSize;
    }

    /**
     * Sets the height of this node, and all of its descendants, this should be
     * the distance from the root.
     *
     * @param height the height of this node.
     */
    private void setHeight(int height) {
        this.height = height;
        neighbors.children().forEach(child -> child.setHeight(height + 1));

    }

    /**
     * Sets this node as the root. This will establish all of the heights in the
     * tree.
     */
    public void setAsRoot() {
        setHeight(0);
    }

    /**
     * Sets this node as the root, and names all the nodes in the tree.
     */
    public void setAsRootAndName() {
        setAsRoot();
        setNames();
    }

    
    
    /**
     * adds 1 to the digit immediately preceding the last letter of the proffered string. 
     * @param input the proffered string
     * @return a new string.
     */
    private static String doubleLetter(String input) {
        int i = input.length() - 1;
        while(i > 0 && Character.isDigit(input.charAt(i - 1))) i--;
        String numString = input.substring(i, input.length()-1);
        int numberFound = numString.isEmpty()?1: Integer.parseInt(numString);        
        return input.substring(0,i) + (numberFound+1) + input.charAt(input.length()-1);
    }
    
    /**
     * Sets the names of every node in the tree.
     */
    private void setNames() {
        if (name == null) name = "a";
        IntStream.range(0, neighbors.numChildren())
                .forEach(i -> {
                    char addOn = (char)(i + (int)'a');
                    if(addOn == name.charAt(name.length()-1)){
                        neighbors.getChild(i).name = doubleLetter(name);
                    }
                    else neighbors.getChild(i).name = name + addOn;
                });
        neighbors.children().forEach(Node::setNames);
    }

    /**
     * Is this node designated as a root node?
     *
     * @return true if the node is designated as a root node, false otherwise
     */
    public boolean isRoot() {
        return getHeight() == 0;
    }

    /**
     * Finds a minimal set of nodes for failure.
     *
     * @param badCompSize components of this size are forbidden.
     * @param neighborDist the distance of a node from a failed node to be
     * considered near to it, called "l" in the paper.
     */
    public void setSelections(int badCompSize, int neighborDist) {

        neighbors.children.forEach(child -> child.setSelections(badCompSize, neighborDist));

        setComponentSize();

        if (!parentCanHandleThis(badCompSize, neighborDist)
                || (isRoot() && containsIllegalComp(badCompSize, -1)))
            selectNode(neighborDist);

    }

    /**
     * When deciding if a node can be a failed node, we consider if the parent
     * being a failed node would similarly prevent component sizes from being
     * too big. .
     *
     * @param neighborDistance the distance of a node from a failed node so that
     * it is considered nearby.
     * @param badCompSize components of this size are forbidden.
     * @return if the parent being a failed node would remove the need for this
     * node to be a failed node, true. Otherwise false.
     */
    private boolean parentCanHandleThis(int badCompSize, int neighborDistance) {
        return !containsIllegalComp(badCompSize, neighborDistance - 1);
    }

    /**
     *
     * @param badCompSize components of this size are forbidden.
     * @param searchDistance How much farther needs to be searched for an
     * illegal component.
     * @return true if any of the children have an illegal component size, false
     * otherwise.
     */
    private boolean hasIllegalChild(int badCompSize, int searchDistance) {
        return neighbors.children().anyMatch(
                child -> child.containsIllegalComp(
                        badCompSize,
                        searchDistance - 1)
        );
    }

    /**
     * Does this sub tree contain any components that exceed the max component
     * size.
     *
     * @param badCompSize components of this size are forbidden. requiring
     * without requiring additional nodes to fail.
     * @param searchDist How much farther needs to be searched for an illegal
     * component
     * @return true if this node's sub tree contains components greater than the
     * max component size, false otherwise.
     */
    private boolean containsIllegalComp(int badCompSize, int searchDist) {

        if (searchDist < 0 && isNearFailedNode) return false;

        if (searchDist >= 0 && hasIllegalChild(badCompSize, searchDist))
            return true;

        return searchDist < 0 && getComponentSize() >= badCompSize;
    }

    /**
     * The number of nodes in a smallest node failure set.
     *
     * @return The number of nodes in a smallest node failure set.
     */
    public int numSelected() {
        return neighbors.children()
                .mapToInt(node -> node.numSelected())
                .sum()
                + (isSelectedForFailureSet ? 1 : 0);
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


    @Override
    public String toString() {
        return toString("");
    }

    /**
     * The names of the children.
     *
     * @return
     */
    private String childNames() {
        return neighbors.children().map(child -> child.name != null? child.name:"").reduce((s1, s2) -> s1
                + ", " + s2).orElse("");
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
        if (neighbors.hasChildren()) {
            local += indent + "children: " + childNames() + "\n";
            return local
                    + neighbors.children().map(child -> child.toString(indent + "\t")).reduce((s1, s2) -> s1
                    + s2).get();
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
