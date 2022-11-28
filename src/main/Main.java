package main;

/**
 *
 * @author Kayak
 */
public class Main {

    /**
     * A sample tree.
     * @return 
     */
    public static Node sampleTree() {
        Node A = new Node("A"),
                AA = new Node(A, "2A"),
                    AAA = new Node(AA, "3A"),
                        AAAA = new Node(AAA, "4A"),
                            AAAAA = new Node(AAAA, "5A"),
                        AAAB = new Node(AAA, "3AB"),
                            AAABA = new Node(AAAB, "3ABA"),
                            AAABB = new Node(AAAB, "3A2B"),
                        AAAC = new Node(AAA, "3AC"),
                    AAB = new Node(AA, "2AB"),
                        AABA = new Node(AAB, "2ABA aka v2"),
                            AABAA = new Node(AABA, "2AB2A"),
                                AABAAA = new Node(AABAA, "2AB3A"),
                                    AABAAAA = new Node(AABAAA, "2AB4A aka u2"),
                                        AABAAAAA = new Node(AABAAAA, "2AB5A"),
                    AAC = new Node(AA, "2AC"),
                        AACA = new Node(AAC, "2ACA aka w1"),
                            AACAA = new Node(AACA, "2AC2A"),
                            AACAB = new Node(AACA, "2ACAB"),
                                AACABA = new Node(AACAB, "2ACABA"),
                                    AACABAA = new Node(AACABA, "2ACAB2A aka v1"),
                                        AACABAAA = new Node(AACABAA, "2ACAB3A"),
                                            AACABAAAA = new Node(AACABAAA, "2ACAB4A"),
                                                AACABAAAAA = new Node(AACABAAAA, "2ACAB5A aka u1"),
                                                    AACABAAAAAA = new Node(AACABAAAAA, "2ACAB6A"),
                                                AACABAAAAB = new Node(AACABAAAA, "2ACAB4AB"),
                                       AACABAAB = new Node(AACABAA, "2ACAB2AB"),
                AB = new Node(A, "AB"),
                    ABA = new Node(AB, "ABA"),
                        ABAA = new Node(ABA, "AB2A"),
                        ABAB = new Node(ABA, "ABAB"),
                            ABABA = new Node(ABAB, "ABABA"),
                            ABABB = new Node(ABAB, "ABA2B"),
                    ABB = new Node(AB, "A2B"),
                    ABC = new Node(AB, "ABC"),
                        ABCA = new Node(ABC, "ABCA"),
                            ABCAA = new Node(ABCA, "ABC2A"),
                        ABCB = new Node(ABC, "ABCB"),
                            ABCBA = new Node(ABCB, "ABCBA"),
                    ABD = new Node(AB, "ABD"),
                        ABDA = new Node(ABD, "ABDA"),
                        ABDB = new Node(ABD, "ABDB"),
                            ABDBA = new Node(ABDB, "ABDBA"),
                            ABDBB = new Node(ABDB, "ABD2B"),
                        ABDC = new Node(ABD, "ABDC"),
                            ABDCA = new Node(ABDC, "ABDCA"),
                            ABDCB = new Node(ABDC, "ABDCB"),
                            ABDCC = new Node(ABDC, "ABD2C"),
                        ABDD = new Node(ABD, "AB2D"),
                AC = new Node(A, "AC aka C1");
           
        return A;
    }
    
    public static Node sampleTree2(){
        Node A = new Node(),
                AA = new Node(A),
                    AAA = new Node(AA),
                AB = new Node(A),
                    ABA = new Node(AB),
                        ABAA = new Node(ABA),
                            ABAAA = new Node(ABAA),
                AC = new Node(A),
                    ACA = new Node(AC),
                        ACAA = new Node(ACA),
                            ACAAA = new Node(ACAA);
                
        A.name.name();
           
        return A;
    }

    public static Node counterTree() {
        Node root = new Node();
        Node A = new Node(root),
                AA = new Node(A),
                AB = new Node(A),
                    ABA = new Node(AB),
                    ABB = new Node(AB),
                        ABBA = new Node(ABB),
                            ABBAA = new Node(ABBA),
                                ABBAAA = new Node(ABBAA),
                                    ABBAAAA = new Node(ABBAAA),
                                        ABBAAAAA = new Node(ABBAAAA),
                                           ABBAAAAAA = new Node(ABBAAAAA),
                                               ABBAAAAAAA = new Node(ABBAAAAAA),
                                                   ABBAAAAAAAA = new Node(ABBAAAAAAA),
                                                        ABBAAAAAAAAA = new Node(ABBAAAAAAAA),
                                                            ABBAAAAAAAAAA = new Node(ABBAAAAAAAAA),
                                                            ABBAAAAAAAAAB = new Node(ABBAAAAAAAAA),                
                        ABBB = new Node (ABB),
                            ABBBA = new Node (ABBB),
                                ABBBAA = new Node (ABBBA),
                                ABBBAB = new Node (ABBBA),
                                ABBBAC = new Node (ABBBA),
                                ABBBAD = new Node (ABBBA),
                                ABBBAE = new Node (ABBBA),
                                ABBBAF = new Node (ABBBA),
                                ABBBAG = new Node (ABBBA),
                                ABBBAH = new Node (ABBBA),
                                ABBBAI = new Node (ABBBA),
                                ABBBAJ = new Node (ABBBA),
                                ABBBAK = new Node (ABBBA),
                                ABBBAL = new Node (ABBBA),
                                ABBBAM = new Node (ABBBA),
                    ABC = new Node(AB),
                AC = new Node(A);

        root.name.name();
           
        return root;
    }

    
    
    /**
     * @param args This code does not currently use command line arguments.
     */
    public static void main(String[] args) {
        Node tree = sampleTree();//sampleTree();
        
        tree.buildMinFailureSet(3, 2);
        
        System.out.println(tree.failureSet());

    }

}