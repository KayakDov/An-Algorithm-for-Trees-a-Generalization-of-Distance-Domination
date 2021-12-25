package main;

/**
 *
 * @author Kayak
 */
public class Main {

    public static Node sampleTree() {
//        Node A = new Node("A"),
//                AA = new Node(A, "AA"),
//                    AAA = new Node(AA, "AAA"),
//                        AAAA = new Node(AAA, "AAAA"),
//                            AAAAA = new Node(AAAA, "AAAAA"),
//                        AAAB = new Node(AAA, "AAAB"),
//                            AAABA = new Node(AAAB, "AAABA"),
//                            AAABB = new Node(AAAB, "AAABB"),
//                        AAAC = new Node(AAA, "AAAC"),
//                    AAB = new Node(AA, "AAB"),
//                        AABA = new Node(AAB, "AABA aka v2"),
//                            AABAA = new Node(AABA, "AABAA"),
//                                AABAAA = new Node(AABAA, "AABAAA"),
//                                    AABAAAA = new Node(AABAAA, "AABAAAA aka u2"),
//                                        AABAAAAA = new Node(AABAAAA, "AABAAAAA"),
//                    AAC = new Node(AA, "AAC"),
//                        AACA = new Node(AAC, "AACA aka w1"),
//                            AACAA = new Node(AACA, "AACAA"),
//                            AACAB = new Node(AACA, "AACAB"),
//                                AACABA = new Node(AACAB, "AACABA"),
//                                    AACABAA = new Node(AACABA, "AACABAA aka v1"),
//                                        AACABAAA = new Node(AACABAA, "AACABAAA"),
//                                            AACABAAAA = new Node(AACABAAA, "AACABAAAA"),
//                                                AACABAAAAA = new Node(AACABAAA, "AACABAAAAA aka u1"),
//                                                    AACABAAAAAA = new Node(AACABAAAA, "AACABAAAAAA"),
//                                                AACABAAAAB = new Node(AACABAAAA, "AACABAAAAB"),
//                                       AACABAAB = new Node(AACABAA, "AACABAAB"),
//                AB = new Node(A, "AB"),
//                    ABA = new Node(AB, "ABA"),
//                        ABAA = new Node(ABA, "ABAA"),
//                        ABAB = new Node(ABA, "ABAB"),
//                            ABABA = new Node(ABAB, "ABABA"),
//                            ABABB = new Node(ABAB, "ABABB"),
//                    ABB = new Node(AB, "ABB"),
//                    ABC = new Node(AB, "ABC"),
//                        ABCA = new Node(ABC, "ABCA"),
//                            ABCAA = new Node(ABCA, "ABCAA"),
//                        ABCB = new Node(ABC, "ABCB"),
//                            ABCBA = new Node(ABCB, "ABCBA"),
//                    ABD = new Node(AB, "ABD"),
//                        ABDA = new Node(ABD, "ABDA"),
//                        ABDB = new Node(ABD, "ABDB"),
//                            ABDBA = new Node(ABDB, "ABDBA"),
//                            ABDBB = new Node(ABDB, "ABDBB"),
//                        ABDC = new Node(ABD, "ABDC"),
//                            ABDCA = new Node(ABDC, "ABDCA"),
//                            ABDCB = new Node(ABDC, "ABDCB"),
//                            ABDCC = new Node(ABDC, "ABDCC"),
//                        ABDD = new Node(ABD, "ABDD"),
//                AC = new Node(A, "AC aka C1");

        Node A = new Node("A"),
                AA = new Node(A, "AA"),
                    AAA = new Node(AA, "AAA"),
                AB = new Node(A, "AB"),
                    ABA = new Node(AB, "ABA"),
                        ABAA = new Node(ABA, "ABAA"),
                            ABAAA = new Node(ABAA, "ABAAA"),
                AC = new Node(A, "AC"),
                    ACA = new Node(AC, "ACA"),
                        ACAA = new Node(ACA, "ACAA"),
                            ACAAA = new Node(ACAA, "ACAAA");
                
        A.setAsRoot();
           
        return A;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Node tree = sampleTree();
        
        tree.setSelections(1, 3);
        
        System.out.println(tree);
    }

}