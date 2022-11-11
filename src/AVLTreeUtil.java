import java.util.LinkedList;
import java.util.List;

//The Actual AVL Tree Class
public class AVLTreeUtil {
    //Individual Node Structure
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

    //------------------Tree Print Block Start------------------
    //Used for debugging
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
    //------------------Tree Print Block End------------------

    //------------------Insert Block Start------------------
    public Node insert(int data) {
        this.root = insert(this.root, data);
        return this.root;
    }


    private Node insert(Node node, int data) {
        if (node == null) {
            return (new Node(data));
        }
        //insert to the left
        else if (data < node.data)
            node.left = insert(node.left, data);
            //insert to the right
        else if (data > node.data)
            node.right = insert(node.right, data);
        else
            //if node is already present simply return that node
            return node;

        //set height for ancestor node
        node.height = 1+Math.max(getHeight(node.left),getHeight(node.right));

        //get balance factor
        int currentBF = getBalanceFactor(node);
        /* RR Rotation */
        if (currentBF > 1 && data < node.left.data)
            return rotateRight(node);

        /* LL Rotation */
        if (currentBF < -1 && data > node.right.data)
            return rotateLeft(node);

        /* LR Rotation */
        if (currentBF > 1 && data > node.left.data) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        /* RL Rotation */
        if (currentBF < -1 && data < node.right.data) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }
    //------------------Insert Block End------------------
    public void delete(int data) {
        this.root = delete(this.root, data);
    }

    private Node delete(Node node, int data) {
        if (node == null) {
            return node;
        }
        if (data < node.data) {
            //lookup to the left for deletion
            node.left = delete(node.left, data);
        } else if (data > node.data) {
            //lookup to the right for deletion
            node.right = delete(node.right, data);
        } else {
            //if one of the child is null replace the current node with non-null child
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
                    /*
                    find predecessor to the current node
                    replace current node's value with the predecessor's
                    and delete the predecessor
                    */
                Node temp = this.findReplaceableNode(node.right);
                node.data = temp.data;
                node.right = delete(node.right, temp.data);
            }
        }

        if (node == null)
            return node;

        //set the height
        node.height = Math.max(this.getHeight(node.left), this.getHeight(node.right)) + 1;

        int balanceFactor = this.getBalanceFactor(node);

        //LL Rotation
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        //LR Rotation
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        //RR Rotation
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        //LR Rotation
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    //AVL Helper function Block Start
    private int getHeight(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    //calculates balance factor for the node in question
    private int getBalanceFactor(Node node) {
        if (node == null)
            return 0;
        return this.getHeight(node.left) - this.getHeight(node.right);
    }

    //finds the node which is inorder successor of the node marked for deletion
    private Node findReplaceableNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }
    private Node rotateLeft(Node node) {
        Node rightChild = node.right;
        Node grandChild = rightChild.left;

        rightChild.left = node;
        node.right = grandChild;

        node.height = 1 + Math.max(this.getHeight(node.left), this.getHeight(node.right));
        rightChild.height = 1 + Math.max(this.getHeight(rightChild.left), this.getHeight(rightChild.right));

        return rightChild;
    }

    private Node rotateRight(Node node) {
        Node leftChild = node.left;
        Node grandChild = leftChild.right;

        leftChild.right = node;
        node.left = grandChild;

        node.height = 1 + Math.max(this.getHeight(node.left), this.getHeight(node.right));
        leftChild.height = 1 + Math.max(this.getHeight(leftChild.left), this.getHeight(leftChild.right));

        return leftChild;
    }
    //AVL helper function end

    //--------search block start----------
    //Search a single value
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

    //Search for values in given range
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
    //------------- search block end ------------

}