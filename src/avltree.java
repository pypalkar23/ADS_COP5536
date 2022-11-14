import java.util.LinkedList;
import java.util.List;

//The Wrapper Class
public class avltree {
    public static final String NULL_STRING = "NULL";
    AVLTreeUtil tree = null;
    List<Operation> operations; //List to store operations read from file
    ResultWriter resultWriter; //Output file writer
    InputReader inputReader;//input file reader

    public avltree() {
        inputReader = new InputReader();
        resultWriter = new ResultWriter();
        operations = new LinkedList<>();
    }

    private void initializeTree() {
        this.tree = new AVLTreeUtil();
    }

    //wrapper for writing result for the search operation
    private void writeResult(String text) {
        try {
            this.resultWriter.writeToFile(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //wrapper to close the file
    private void closeWriter() {
        try {
            this.resultWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //wrapper for inserting node into a tree
    private void insert(int val) {
        this.tree.insert(val);
    }

    //wrapper for deleting node from tree
    private void delete(int val) {
        this.tree.delete(val);
    }

    //wrapper for searching a single value in tree
    private void search(int val) {
        String res = tree.search(val);
        if (res == null)
            this.writeResult(NULL_STRING);
        else
            this.writeResult(res);
    }

    //wrapper for searching value that are in the given range
    private void search(int val1, int val2) {
        List<String> res = tree.search(val1, val2);
        if (res.isEmpty()) {
            this.writeResult(NULL_STRING);
        } else {
            this.writeResult(String.join(",", res));
        }
    }

    private void readOperations(String filename) throws Exception{
        this.operations = inputReader.getOperationsFromFile(filename);
    }

    private void performOperations() {
        //Perform Operations Stored in the list One By One
        for (Operation op : this.operations) {
            switch (op.getOpcode()) {
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
        }

        closeWriter();
    }

    public void execute(String filePath) throws Exception{
        this.readOperations(filePath);
        this.performOperations();
    }

    public static void main(String[] args) {
        avltree at = new avltree();
        String inputFilePath = args[0];
        try {
            at.execute(inputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}