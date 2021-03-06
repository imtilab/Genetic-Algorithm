
import java.util.Arrays;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mti
 */
public class Population {

    int populationSize = 6;
    int numberOfIteration = 4;
    int maxPop = 15, minPop = 0;
    int binarySizeLimit = 4;//15=1111

    int[] population_array;
    String[] binaryString_array;
    int fitness[];
    double ratio[];
    String pairs_binaryString[] = new String[6];//side by side index pair (0,1),(2,3),(4,5)
    int min, secondMin;//lowest index position to update after each mutation 

    //takes population size and initializes and generates random population
    public void initializePopulation() {
        population_array = new int[populationSize];
        binaryString_array = new String[populationSize];
        fitness = new int[populationSize];
        ratio = new double[populationSize];

        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            population_array[i] = random.nextInt((maxPop - minPop) + 1) + minPop;
            System.out.print(population_array[i] + " ");
        }
        System.out.println();
    }

    public void startGeneric() {
        int loopCount = 0;
        while (loopCount++ < numberOfIteration) {
            populationToBinaryString();
            double totalFitness = calcFitness();
            calcRatio(totalFitness);

            selectAndMakePairs();
            crossover();
            mutation();

            for (int i = 0; i < populationSize; i++) {
                System.out.print(population_array[i] + " ");
            }
            System.out.println();
        }
    }

    //mutation
    public void mutation() {
        String x1 = "", x2 = "", x3 = "";

        Random random = new Random();
        //select 3 unique random binaryString from pairs
        int i = 0, first = 0, second = 0;
        while (i < 3) {
            int index = random.nextInt(((pairs_binaryString.length - 1) - 0) + 1) + 0;//0 to 5
            System.out.println(index);
            if (i == 0) {
                x1 = pairs_binaryString[index];
                first = index;//to ensure unique random
                i++;
            } else if (i == 1 && index != first) {
                x2 = pairs_binaryString[index];
                second = index;//to ensure unique random
                i++;
            } else if (i == 2 && index != first && index != second) {
                x3 = pairs_binaryString[index];
                i++;
            }
        }
        System.out.println("selected for mutation: " + x1 + " " + x2 + " " + x3);

        //select random bit for mutation
        int bit = random.nextInt(((binarySizeLimit - 1) - 0) + 1) + 0;//0 to 3
        System.out.println("selected bit for mutation: " + bit);

        //x1 mutation
        char ch[] = x1.toCharArray();
        if (ch[bit] == '0') {
            ch[bit] = '1';
        } else {
            ch[bit] = '0';
        }
        x1 = new String(ch);

        //x2 mutation
        ch = x2.toCharArray();
        if (ch[bit] == '0') {
            ch[bit] = '1';
        } else {
            ch[bit] = '0';
        }
        x2 = new String(ch);

        //x3 mutation
        ch = x3.toCharArray();
        if (ch[bit] == '0') {
            ch[bit] = '1';
        } else {
            ch[bit] = '0';
        }
        x3 = new String(ch);

        System.out.println("after mutation: " + x1 + " " + x2 + " " + x3);

        //choose best two
        int mutated_array[] = {toInteger(x1), toInteger(x2), toInteger(x3)};//keeps int from binaryString
        Arrays.sort(mutated_array);//icreasing order sorted, last two are best two

        System.out.println(population_array[min] + " " + population_array[secondMin] + " updated to " + mutated_array[1] + " " + mutated_array[2]);

        //update the population_array lowest two with the new best two
        population_array[min] = mutated_array[1];//second best to lowest
        population_array[secondMin] = mutated_array[2];//best to second lowest

    }

    //crossover
    public void crossover() {
        Random random = new Random();

        int bit1 = random.nextInt(((binarySizeLimit - 2) - 0) + 1) + 0;//0 to 2
        int bit2 = bit1 + 1;//next bit
        System.out.println("crossover bits: " + bit1 + " " + bit2);

        for (int i = 0; i < 6; i += 2) {
            char ch1[] = pairs_binaryString[i].toCharArray();
            char ch2[] = pairs_binaryString[i + 1].toCharArray();

            char temp1 = ch1[bit1];
            char temp2 = ch1[bit2];

            ch1[bit1] = ch2[bit1];
            ch1[bit2] = ch2[bit2];

            ch2[bit1] = temp1;
            ch2[bit2] = temp2;

            pairs_binaryString[i] = new String(ch1);
            pairs_binaryString[i + 1] = new String(ch2);
        }

        System.out.println("after crossover");
        for (int i = 0; i < pairs_binaryString.length; i++) {
            System.out.print(pairs_binaryString[i] + " ");
        }
        System.out.println();
    }

    //select bests and make pairs
    public void selectAndMakePairs() {
        Node[] nodeArray = Priority.setPriority(ratio, binaryString_array);//get sorted node
        int max = nodeArray[nodeArray.length - 1].index;
        min = nodeArray[0].index;
        secondMin = nodeArray[1].index;

        for (int i = 0; i < nodeArray.length; i++) {
            System.out.print(nodeArray[i].ratioValue + " ");

        }
        System.out.println();
        for (int i = 0; i < nodeArray.length; i++) {
            System.out.print(nodeArray[i].binaryString + " ");

        }
        System.out.println();
//        System.out.println(max+" "+ min+" "+secondMin);

        int pairIndexCounter = 0;
        String x1 = nodeArray[nodeArray.length - 1].binaryString;
        pairs_binaryString[pairIndexCounter] = x1;//ensuring one max in pair

        //random select and random pair
        String x2 = "", x3 = "", x4 = "", x5 = "", x6 = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(((populationSize - 1) - 2) + 1) + 2;//not taking max, min, secondMin val
            System.out.println(index);
            if (i == 0) {
                x2 += nodeArray[index].binaryString;
                pairs_binaryString[++pairIndexCounter] = x2;//ensure a pair of max
            } else if (i == 1) {
                x3 += nodeArray[index].binaryString;
                pairs_binaryString[++pairIndexCounter] = x3;
            } else if (i == 2) {
                x4 += nodeArray[index].binaryString;
                pairs_binaryString[++pairIndexCounter] = x4;
            } else if (i == 3) {
                x5 += nodeArray[index].binaryString;
                pairs_binaryString[++pairIndexCounter] = x5;
            } else if (i == 4) {
                x6 += nodeArray[index].binaryString;
                pairs_binaryString[++pairIndexCounter] = x6;
            }
        }
        System.out.println("after pairing: " + x1 + " " + x2 + " " + x3 + " " + x4 + " " + x5 + " " + x6);
    }

    //takes total fitness ratioValue
    //calculate ratio for the fitness of each population
    public void calcRatio(double totalFitness) {
        for (int i = 0; i < populationSize; i++) {
            ratio[i] = (fitness[i] * 100) / totalFitness;
            System.out.print(ratio[i] + " ");
        }
        System.out.println();
    }

    //calculate fitness for each population and return total fitness
    public int calcFitness() {
        int totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            int x = population_array[i];
            fitness[i] = (15 * x) - (x * x);

            totalFitness += fitness[i];
            System.out.print(fitness[i] + " ");
        }
        System.out.println();
        return totalFitness;
    }

    //converts all decoded population to binary string
    public void populationToBinaryString() {
        for (int i = 0; i < populationSize; i++) {
            binaryString_array[i] = getBinaryString(population_array[i]);
        }
    }

    //takes an integer and return it's binary ratioValue in string format
    public String getBinaryString(int val) {
        String binaryString = Integer.toBinaryString(val);
        int len = binaryString.length();

        while (len < binarySizeLimit) {
            binaryString = 0 + binaryString;
            len++;
        }
        return binaryString;
    }

    //takes a binary in string format and return it's integer ratioValue
    public int toInteger(String binaryString) {
        int val = Integer.parseInt(binaryString, 2);
        return val;
    }
}
