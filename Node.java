import java.io.*;
import java.util.*;



public class Node {
    private String label ;
    private Node leftChild ;
    private Node rightChild;
    private boolean isLeaf ;
    private int value ;



    public Node(int value ,String label, Node leftChild, Node rightChild, boolean isLeaf) {
        this.label = label;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.isLeaf = isLeaf;
        this.value  = value ;
    }
    /*
    public Node() {
        this.label = "";
        this.leftChild = null;
        this.rightChild = null;
        this.isLeaf = true;
        this.value = 0;
    }
    */
    
    /*retour des valeurs */ 

    public int  getValue() {
        return value ;
    }

    public String getLabel() {
    return label;
    }
    
    public Node getLeft() {
        return leftChild;
    }
    
    public Node getRight() {
        return rightChild;
    }

    public boolean isLeafNode() {
    return isLeaf;
    }

    public void nodeSetvalue( int number){
    this .value = number;
    }

}





public class LeafNode extends Node {
    private String answer;

    public LeafNode(String answer, String label) {
        super(value, answer, null, null, true);
        this.answer = label;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}




public class AnswerNode extends Node {
    public AnswerNode(String label) {
        super(0, label, null, null, true);
    }
}









public class InternalNode extends Node {
    private String question;
    private Node yesNode;
    private Node noNode;








    public InternalNode(String question, Node yesNode, Node noNode) {
        super(0, "", yesNode, noNode, false);
        this.question = question;
        this.yesNode = yesNode;
        this.noNode = noNode;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Node getYesNode() {
        return yesNode;
    }

    public void setYesNode(Node yesNode) {
        this.yesNode = yesNode;
    }

    public Node getNoNode() {
        return noNode;
    }

    public void setNoNode(Node noNode) {
        this.noNode = noNode;
    }
}









public class GuessingGame {
    private Node root;
    private Scanner scanner;
    private int numNodes;

    public GuessingGame() {
        this.root = null;
        this.scanner = new Scanner(System.in);
        this.numNodes = 0;
    }


    public void play() {
        System.out.println("Welcome to the game!");
        System.out.println("Please choose a job, and then press <return> ");
        String input = scanner.nextLine();

        if (root == null) {
            root = new Node(0,"Please choose a job, and then press <return>",null,null,true );
            numNodes++;


        } else {
            Node current = root; 
            while (true) {


                if (current.isLeafNode()) {
                    if (current == root )
                        {
                            System.out.println("the tree containas only the root");
                        }
                    System.out.println("I am unable to guess; you have won!");
                         

                    System.out.println("What did you choose?");
                    String newjob = scanner.nextLine();
                    System.out.println("What question could I ask to distinguish a " + newjob + " from a " + ((LeafNode) current).getLabel() + "?");
                    String newQuestion = scanner.nextLine();
                    System.out.println("For a " + newjob + ", would you answer yes or no to this question (Y/N)?");
                    String newAnswer = scanner.nextLine();
                    Node oldNode = current;
                    current = new InternalNode(newQuestion, null, null);
                    if (newAnswer.equals("Y")) {
                        ((InternalNode) current).setYesNode(new Node(newjob));
                        ((InternalNode) current).setNoNode(oldNode);
                    } else {
                        ((InternalNode) current).setYesNode(oldNode);
                        ((InternalNode) current).setNoNode(new Node(newjob));
                    }
                    numNodes += 2;
                    break;


                } else {
                    System.out.println(current.getQuestion());
                    String answer = scanner.nextLine();
                    if (answer.equals("Y")) {
                        current = ((InternalNode) current).getYesNode();
                    } else {
                        current = ((InternalNode) current).getNoNode();
                    }
                }


            }
        }
    }

    public void saveToFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(numNodes + " Welcome to the game!\n");
        writeNode(root, writer);
        writer.close();
    }

    private void writeNode(Node node, FileWriter writer) throws IOException {
        if (node.isLeafNode()) {
            writer.write("= " + ((Node) node).getValue() + "\n");
        } else {
            writer.write("? ");
            writer.write(getValue(((InternalNode) node).getYesNode()) + " ");
            writer.write(getValue(((InternalNode) node).getNoNode()) + " ");
            writer.write(((InternalNode) node).getQuestion() + "\n");
            writeNode(((InternalNode) node).getYesNode(), writer);
            writeNode(((InternalNode) node).getNoNode(), writer);
        }
    }

    private String getNodevalue(Node node) {
        if (node == null) {
            return "0";
        } else {
            return Integer.toString(node.getValue());
        }
    }



    public void loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        String[] parts = line.split(" ");
        numNodes = Integer.parseInt(parts[0]);
        root = readNode(reader);
        reader.close();
    }

    private Node readNode(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line.charAt(0) == '=') {
            return new AnswerNode(line.substring(1));
        } else {
            Node node = new Node();
            node.setLabel(line.substring(1));
            node.setYesNode(readNode(reader));
            node.setNoNode(readNode(reader));
            return node;
        }
    }
}










public class Main {
    public static void main(String[] args) {
        GuessingGame game = new GuessingGame();

        // Uncomment this code if you want to load the tree from a file
        
        try {
            game.loadFromFile("tree.txt");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }

        // 



        while (true) {
            game.play();

            System.out.println("Do you want to play again? (Y/N)");
            String answer = game.scanner.nextLine();
            if (!answer.equals("Y")) {
                break;
            }
        }

        // Uncomment this code if you want to save the tree to a file
        
        try {
            game.saveToFile("tree.txt");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
         
    }
}
