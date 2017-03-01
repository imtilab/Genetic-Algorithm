
import java.util.Queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mti
 */
public class Priority {

    //takes an ratio_array<Double> and []binaryString_array
    //sort the ratio_array and return sorted node ratio_array

    public static Node[] setPriority(double[] ratio_array, String []binaryString_array) {

        //put ratio_array values and index in nodeArray
        Node[] nodeArray = new Node[ratio_array.length];
        for (int i = 0; i < ratio_array.length; i++) {
            String binSt = binaryString_array[i];
            double ratioValue = ratio_array[i];
            nodeArray[i] = new Node(binSt, ratioValue, i);
        }

        //sorting nodes based on ratio_array val
        for (int i = 0; i < nodeArray.length; i++) {
            for (int j = nodeArray.length - 1; j > i; j--) {
                if (nodeArray[j].ratioValue < nodeArray[j - 1].ratioValue) {
                    Node temp = nodeArray[j];
                    nodeArray[j] = nodeArray[j - 1];
                    nodeArray[j - 1] = temp;
                }
            }
        }

        return nodeArray;
    }
}
//Node class

class Node {
    String binaryString;
    double ratioValue;
    int index;

    public Node(String biString, double ratioValue, int index) {
        this.binaryString = biString;
        this.ratioValue = ratioValue;
        this.index = index;
    }
}
