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
    public class Neighbors {

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
         * The descendants of the proffered generation. 0 returns the current 
         * node, 1 children, 2 grandchildren, etc...
         * @param gen
         * @return 
         */
        public Stream<Node> descendentGen(int gen){
            if(gen == 0) return Stream.of(Node.this);
            return children().flatMap(child -> child.neighbors.descendentGen(gen - 1));
        }
        
        /**
         * All descendants closer than the given distance including this node
         * @param upToAndIncludingGen
         * @return 
         */
        public Stream<Node> descendents(int upToAndIncludingGen){
            if(upToAndIncludingGen < 0) return Stream.of();
            return Stream.concat(Stream.of(Node.this), children().flatMap(child -> child.neighbors.descendents(upToAndIncludingGen - 1)));
        }
        
        /**
         * The i'th grandparent of this node.  0 returns this node, 1 the parent,
         * 2 the grandparent, etc...
         * @param i
         * @return 
         */
        public Node ancestor(int i){
            if(i == 0) return Node.this;
            if(isRoot()) return null;
            return parent.neighbors.ancestor(i - 1);
        }
        
        public Stream<Node> siblings(){
            if(parent == null) return Stream.of();
            return parent.neighbors.children().filter(child -> child != Node.this);
        }
        
        /**
         * All the nodes within the closed neighborhood of distance l of this node.
         * @param dist
         * @return 
         */
        public Stream<Node> neighborhood(int dist){
            return Stream.concat(descendents(dist), 
                    IntStream.range(0, dist + 1).boxed()
                    .flatMap(i -> { 
                        Node n = ancestor(i);
                        if(n == null) return Stream.of();
                        return Stream.concat(Stream.of(n),
                            n.neighbors.siblings()
                                .flatMap(sibling -> sibling.neighbors
                                        .descendents(dist - i - 2)));
                    }));
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
    public Neighbors neighbors;
    
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
        neighbors.neighborhood(neighborDistance).forEach(n -> {n.isNearFailedNode = true; n.compSize = 0;});
    }

    private int compSize;

    /**
     * Sets and returns the component size of all elements in the subtree.
     *
     * @return the component size of this node.
     */
    private int setComponentSizes() {
        if (isNearFailedNode) return compSize = 0;
        if (!neighbors.hasChildren()) return compSize = 1;

        return compSize = 1
                + neighbors.children().mapToInt(child -> child.setComponentSizes()).sum();
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
     * Names this node and all descendants.
     */
    public void name() {
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
        return neighbors.isRoot();
    }

    /**
     * Marks each node that needs to be in a minimum failure set.  If called from 
     * outside Node, then it should be called on the root node.
     *
     * @param k components of this size are forbidden.
     * @param l the distance of a node from a failed node to be
     * considered near to it, called "l" in the paper.
     */
    public void setSelections(int k, int l) {

        neighbors.children.forEach(child -> child.setSelections(k, l));

        setComponentSizes();

        if (neighbors.descendentGen(l).anyMatch(n -> n.compSize >= k) || 
           isRoot() && neighbors.neighborhood(l).anyMatch(n -> n.compSize >= k))
            selectNode(l);
    }


    /**
     * The number of nodes in a minimum failure set.
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
        String local = indent + name + ": " + (isSelectedForFailureSet?"O":isNearFailedNode?"X":"") + "\n";
        if (neighbors.hasChildren()) {
            return local
                    + neighbors.children().map(child -> child.toString(indent + "\t")).reduce((s1, s2) -> s1
                    + s2).get();
        }
        return local;
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
