import java.util.LinkedList;
import java.util.List;

//The Actual AVL Tree Class
public class AVLTreeUtil {
    //Individual TreeNode Structure
    TreeNode root;

    //------------------Tree Print Block Start------------------
    //Used for debugging
    private void inorder(TreeNode treeNode) {
        if (treeNode != null) {
            inorder(treeNode.left);
            System.out.print(treeNode.data+" ");
            inorder(treeNode.right);
        }
    }

    public void print(){
        inorder(this.root);
        System.out.println("\n--------");
    }
    //------------------Tree Print Block End------------------

    //------------------Insert Block Start------------------
    public TreeNode insert(int data) {
        this.root = insert(this.root, data);
        return this.root;
    }

    private TreeNode insert(TreeNode treeNode, int data) {
        if (treeNode == null) {
            return (new TreeNode(data));
        }
        //insert to the left
        else if (data < treeNode.data)
            treeNode.left = insert(treeNode.left, data);
            //insert to the right
        else if (data > treeNode.data)
            treeNode.right = insert(treeNode.right, data);
        else
            //if treeNode is already present simply return that treeNode
            return treeNode;

        //set height for ancestor treeNode
        treeNode.height = 1+Math.max(getHeight(treeNode.left),getHeight(treeNode.right));

        //get balance factor
        int currentBF = getBalanceFactor(treeNode);
        /* RR Rotation */
        if (currentBF > 1 && data < treeNode.left.data)
            return rotateRight(treeNode);

        /* LL Rotation */
        if (currentBF < -1 && data > treeNode.right.data)
            return rotateLeft(treeNode);

        /* LR Rotation */
        if (currentBF > 1 && data > treeNode.left.data) {
            treeNode.left = rotateLeft(treeNode.left);
            return rotateRight(treeNode);
        }

        /* RL Rotation */
        if (currentBF < -1 && data < treeNode.right.data) {
            treeNode.right = rotateRight(treeNode.right);
            return rotateLeft(treeNode);
        }

        return treeNode;
    }
    //------------------Insert Block End------------------
    public void delete(int data) {
        this.root = delete(this.root, data);
    }

    private TreeNode delete(TreeNode treeNode, int data) {
        if (treeNode == null) {
            return treeNode;
        }
        if (data < treeNode.data) {
            //lookup to the left for deletion
            treeNode.left = delete(treeNode.left, data);
        } else if (data > treeNode.data) {
            //lookup to the right for deletion
            treeNode.right = delete(treeNode.right, data);
        } else {
            //if one of the child is null replace the current treeNode with non-null child
            if ((treeNode.left == null) || (treeNode.right == null)) {
                TreeNode temp = null;
                if (temp == treeNode.left)
                    temp = treeNode.right;
                else
                    temp = treeNode.left;

                if (treeNode == null) {
                    treeNode = null;
                } else {
                    treeNode = temp;
                }

            } else {
                    /*
                    find predecessor to the current treeNode
                    replace current treeNode's value with the predecessor's
                    and delete the predecessor
                    */
                TreeNode temp = this.findReplaceableNode(treeNode.right);
                treeNode.data = temp.data;
                treeNode.right = delete(treeNode.right, temp.data);
            }
        }

        if (treeNode == null)
            return treeNode;

        //set the height
        treeNode.height = Math.max(this.getHeight(treeNode.left), this.getHeight(treeNode.right)) + 1;

        int balanceFactor = this.getBalanceFactor(treeNode);

        //LL Rotation
        if (balanceFactor > 1 && getBalanceFactor(treeNode.left) >= 0) {
            return rotateRight(treeNode);
        }
        //LR Rotation
        if (balanceFactor > 1 && getBalanceFactor(treeNode.left) < 0) {
            treeNode.left = rotateLeft(treeNode.left);
            return rotateRight(treeNode);
        }
        //RR Rotation
        if (balanceFactor < -1 && getBalanceFactor(treeNode.right) <= 0) {
            return rotateLeft(treeNode);
        }
        //LR Rotation
        if (balanceFactor < -1 && getBalanceFactor(treeNode.right) > 0) {
            treeNode.right = rotateRight(treeNode.right);
            return rotateLeft(treeNode);
        }

        return treeNode;
    }

    //AVL Helper function Block Start
    private int getHeight(TreeNode treeNode) {
        if (treeNode == null)
            return 0;
        return treeNode.height;
    }

    //calculates balance factor for the treeNode in question
    private int getBalanceFactor(TreeNode treeNode) {
        if (treeNode == null)
            return 0;
        return this.getHeight(treeNode.left) - this.getHeight(treeNode.right);
    }

    //finds the treeNode which is inorder successor of the treeNode marked for deletion
    private TreeNode findReplaceableNode(TreeNode treeNode) {
        TreeNode current = treeNode;
        while (current.left != null)
            current = current.left;
        return current;
    }
    private TreeNode rotateLeft(TreeNode treeNode) {
        TreeNode rightChild = treeNode.right;
        TreeNode grandChild = rightChild.left;

        rightChild.left = treeNode;
        treeNode.right = grandChild;

        treeNode.height = 1 + Math.max(this.getHeight(treeNode.left), this.getHeight(treeNode.right));
        rightChild.height = 1 + Math.max(this.getHeight(rightChild.left), this.getHeight(rightChild.right));

        return rightChild;
    }

    private TreeNode rotateRight(TreeNode treeNode) {
        TreeNode leftChild = treeNode.left;
        TreeNode grandChild = leftChild.right;

        leftChild.right = treeNode;
        treeNode.left = grandChild;

        treeNode.height = 1 + Math.max(this.getHeight(treeNode.left), this.getHeight(treeNode.right));
        leftChild.height = 1 + Math.max(this.getHeight(leftChild.left), this.getHeight(leftChild.right));

        return leftChild;
    }
    //AVL helper function end

    //--------search block start----------
    //Search a single value
    public String search(int val) {
        return this.search(this.root, val);
    }

    public String search(TreeNode treeNode, int val) {
        if (treeNode == null)
            return null;

        if (treeNode.data == val)
            return String.valueOf(treeNode.data);

        if (val < treeNode.data)
            return this.search(treeNode.left, val);

        return this.search(treeNode.right, val);
    }

    //Search for values in given range and stores it in a list
    public List<String> search(int val1, int val2) {
        List<String> result = new LinkedList<>();
        this.search(this.root, val1, val2, result);
        return result;
    }

    public void search(TreeNode treeNode, int val1, int val2, List<String> result) {
        if (treeNode == null)
            return;

        if(val1< treeNode.data){
            search(treeNode.left,val1,val2,result);
        }
        if (treeNode.data >= val1 && treeNode.data <= val2) {
            result.add(String.valueOf(treeNode.data));
        }

        search(treeNode.right,val1,val2,result);
    }
    //------------- search block end ------------

}