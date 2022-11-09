import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class avltree {
    public static final String NULL_STRING = "NULL";

    enum Op {
        Initialize("Initialize"),
        Insert("Insert"),
        Delete("Delete"),
        Search("Search");

        private final String label;
        Op(String label){
            this.label = label;
        }

        public String toString(){
            return  this.label;
        }
    }

    AVLTree tree = null;
    List<Operation> operations;
    ResultWriter resultWriter;

    public avltree() {
        resultWriter = new ResultWriter();
        operations = new LinkedList<>();
    }

    private void initializeTree() {
        this.tree = new AVLTree();
    }

    private void getOperationsFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            Operation operation = this.readRecordFromLine(sc.nextLine());
            if (operation != null)
                this.operations.add(operation);
        }
    }

    private void writeResult(String text) {
        try {
            this.resultWriter.writeToFile(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWriter() {
        try {
            this.resultWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert(int val) {
        this.tree.insert(val);
    }

    private void delete(int val) {
        this.tree.delete(val);
    }

    private void search(int val) {
        String res = tree.search(val);
        if (res == null)
            this.writeResult(NULL_STRING);
        else
            this.writeResult(res);
    }

    private void search(int val1, int val2) {
        List<String> res = tree.search(val1, val2);
        if (res.isEmpty()) {
            this.writeResult(NULL_STRING);
        } else {
            this.writeResult(String.join(",", res));
        }
    }

    private static Operation readRecordFromLine(String line) {
        Operation operation = null;
        Pattern pattern = Pattern.compile("^([A-Za-z]+)\\(([0-9,]*)\\)$");
        Matcher matcher = pattern.matcher(line.trim());
        if (matcher.find()) {
            String tempOp = matcher.group(1);
            String data = matcher.group(2);
            switch (Op.valueOf(tempOp)) {
                case Initialize:
                    operation = new Operation(Op.Initialize);
                    break;

                case Insert:
                    operation = new Operation(Op.Insert, Integer.parseInt(data));
                    break;

                case Delete:
                    operation = new Operation(Op.Delete, Integer.parseInt(data));
                    break;

                case Search:
                    String temp[] = data.split(",");
                    Integer val1 = Integer.parseInt(temp[0]);
                    Integer val2 = null;
                    if (temp.length > 1) {
                        val2 = Integer.parseInt(temp[1]);
                    }
                    operation = new Operation(Op.Search, val1, val2);
                    break;
                default:
                    break;
            }
        }
        return operation;
    }

    private void performOperations() {
        for (Operation op : this.operations) {
            //System.out.println(op);
            switch (op.opcode) {
                case Initialize:
                    this.initializeTree();
                    break;
                case Insert:
                    this.insert(op.getVal1());
                    break;
                case Delete:
                    this.delete(op.getVal1());
                    break;
                case Search:
                    if (op.getVal2() != null)
                        this.search(op.getVal1(), op.getVal2());
                    else
                        this.search(op.getVal1());
                    break;
            }
            //this.tree.print();
        }

        closeWriter();
    }

    static class Operation {

        private Op opcode;
        private Integer val1;
        private Integer val2;

        public Operation(Op opcode, Integer val1, Integer val2) {
            this.opcode = opcode;
            this.val1 = val1;
            this.val2 = val2;
        }

        public Operation(Op opcode, Integer val1) {
            this.opcode = opcode;
            this.val1 = val1;
        }

        public Operation(Op opcode) {
            this.opcode = opcode;
        }

        public Op getOpcode() {
            return this.opcode;
        }

        public Integer getVal1() {
            return this.val1;
        }

        public Integer getVal2() {
            return this.val2;
        }

        public String toString() {
            String str = opcode.toString();
            if (val1 != null) {
                str = str + " " + val1;
            }
            if (val2 != null) {
                str = str + " " + val2;
            }

            return str;
        }
    }

    class AVLTree {
        class Node {
            int data;
            int height;
            Node left;
            Node right;

            public Node(int data) {
                this.height = 1;
                this.data = data;
            }
        }

        Node root;

        private void inorder(Node node) {
            if (node != null) {
                inorder(node.left);
                System.out.print(node.data+" ");
                inorder(node.right);
            }
        }

        public void print(){
            inorder(this.root);
            System.out.println("\n--------");
        }

        public Node insert(int data) {
            root = insert(root, data);
            return root;
        }

        private Node insert(Node node, int data) {
            if (node == null) {
                return (new Node(data));
            }
            else if (data < node.data)
                node.left = insert(node.left, data);
            else if (data > node.data)
                node.right = insert(node.right, data);
            else
                return node;

            node.height = 1+Math.max(getHeight(node.left),getHeight(node.right));

            int balance = getBalance(node);
            /* left left case */
            if (balance > 1 && data < node.left.data)
                return rightRotate(node);

            /* right right case */
            if (balance < -1 && data > node.right.data)
                return leftRotate(node);

            /* left right case */
            if (balance > 1 && data > node.left.data) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            /* right left case */
            if (balance < -1 && data < node.right.data) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        private int getHeight(Node node) {
            if (node == null)
                return 0;
            return node.height;
        }

        private int getBalance(Node node) {
            if (node == null)
                return 0;
            return this.getHeight(node.left) - this.getHeight(node.right);
        }

        private Node findMin(Node node) {
            Node temp = node;
            while (temp.left != null)
                temp = temp.left;
            return temp;
        }

        public void delete(int data) {
            this.root = delete(this.root, data);
        }

        private Node delete(Node node, int data) {
            if (node == null) {
                return node;
            }
            if (data < node.data) {
                node.left = delete(node.left, data);
            } else if (data > node.data) {
                node.right = delete(node.right, data);
            } else {
                if ((node.left == null) || (node.right == null)) {
                    Node temp = null;
                    if (temp == node.left)
                        temp = node.right;
                    else
                        temp = node.left;

                    if (node == null) {
                        node = null;
                    } else {
                        node = temp;
                    }

                } else {
                    Node temp = this.findMin(node.right);
                    node.data = temp.data;
                    node.right = delete(node.right, temp.data);
                }
            }

            if (node == null)
                return node;

            node.height = Math.max(this.getHeight(node.left), this.getHeight(node.right)) + 1;

            int balance = this.getBalance(node);

            if (balance > 1 && getBalance(node.left) >= 0) {
                return rightRotate(node);
            }
            if (balance > 1 && getBalance(node.left) < 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            if (balance < -1 && getBalance(node.right) <= 0) {
                return leftRotate(node);
            }
            if (balance < -1 && getBalance(node.right) > 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        private Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;

            y.left = x;
            x.right = T2;

            x.height = 1 + Math.max(this.getHeight(x.left), this.getHeight(x.right));
            y.height = 1 + Math.max(this.getHeight(y.left), this.getHeight(y.right));

            return y;
        }

        private Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;

            x.right = y;
            y.left = T2;

            y.height = 1 + Math.max(this.getHeight(y.left), this.getHeight(y.right));
            x.height = 1 + Math.max(this.getHeight(x.left), this.getHeight(x.right));

            return x;
        }

        public String search(int val) {
            return this.search(this.root, val);
        }

        public String search(Node node, int val) {
            if (node == null)
                return null;

            if (node.data == val)
                return String.valueOf(node.data);

            if (val < node.data)
                return this.search(node.left, val);

            return this.search(node.right, val);
        }

        public List<String> search(int val1, int val2) {
            List<String> result = new LinkedList<>();
            this.search(this.root, val1, val2, result);
            return result;
        }

        public void search(Node node, int val1, int val2, List<String> result) {
            if (node == null)
                return;

            if(val1<node.data){
                search(node.left,val1,val2,result);
            }
            if (node.data >= val1 && node.data <= val2) {
                result.add(String.valueOf(node.data));
            }

            search(node.right,val1,val2,result);
        }

    }

    class ResultWriter {
        private static final String OUTPUT_FILE_NAME = "output_file.txt";
        private FileWriter writer;

        public ResultWriter() {
            try {
                initializeFile();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        public void initializeFile() throws Exception {
            writer = new FileWriter(OUTPUT_FILE_NAME);
        }

        public void writeToFile(String txt) throws Exception {
            this.writer.write(String.format("%s\n",txt));
        }

        public void close() throws Exception {
            writer.close();
        }
    }

    public static void main(String[] args) {
        avltree at = new avltree();
        try {
            at.getOperationsFromFile(args[0]);
            at.performOperations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}