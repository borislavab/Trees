import java.util.*;

public class BinarySearchTree {
    private static class Node{
        private int key;
        private Node left;
        private Node right;

        public Node(int item) {
            this.key = item;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    public BinarySearchTree() {
        this.root = null;
    }

    public BinarySearchTree(Node root) {
        this.root = root;
    }

    public int getHeight() {
        return height(this.root);
    }

    private static int height(Node root) {
        if(root == null) {
            return 0;
        }
        int leftHeight = height(root.left);
        int rightHeight = height(root.right);
        int maxChildHeight = leftHeight > rightHeight ? leftHeight : rightHeight;
        return 1 + maxChildHeight;
    }

    public void insert(int key){
        this.root = insert(this.root, key);
    }

    private static Node insert(Node root, int key) {
        if (root == null) {
            return new Node(key);
        }
        if(key < root.key) {
            root.left = insert(root.left, key);
            return root;
        }
        if(key > root.key) {
            root.right = insert(root.right, key);
            return root;
        }
        throw new IllegalArgumentException("Key already exists in tree!");
    }

    public boolean find(int key){
        return find(this.root, key);
    }

    private static boolean find(Node node, int key) {
        if(node == null) {
            return false;
        }
        if(node.key == key) {
            return true;
        }
        if(key < node.key) {
            return find(node.left, key);
        }
        else {
            return find(node.right, key);
        }
    }

    public void delete(int key){
        this.root = delete(this.root, key);
    }

    private static Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (key < node.key) {
            node.left = delete(node.left, key);
            return node;
        }
        if (key > node.key) {
            node.right = delete(node.right, key);
            return node;
        }
        // key == node.key
        if (node.left == null) {
            return node.right;
        }
        if (node.right == null) {
            return node.left;
        }
        deleteRootWithBothChildren(node);
        return node;
    }

    private static void deleteRootWithBothChildren(Node root) {
        int key = getMinValue(root.right);
        root.key = key;
        root.right = delete(root.right, key);
    }

    private static int getMinValue(Node root) {
        if(root == null) {
            throw new IllegalArgumentException("Empty tree!");
        }
        while(root.left != null) {
            root = root.left;
        }
        return root.key;
    }

    public void printInOrder() {
        printInOrderHelper(this.root);
        System.out.println();
    }

    private static void printInOrderHelper(Node root) {
        if(root == null) {
            return;
        }
        printInOrderHelper(root.left);
        System.out.print(root.key + " ");
        printInOrderHelper(root.right);
    }

    public void printPreOrder() {
        printPreOrderHelper(this.root);
        System.out.println();
    }

    private static void printPreOrderHelper(Node root) {
        if(root == null) {
            return;
        }
        System.out.print(root.key + " ");
        printPreOrderHelper(root.left);
        printPreOrderHelper(root.right);
    }

    public void printPostOrder() {
        printPostOrderHelper(this.root);
        System.out.println();
    }

    private static void printPostOrderHelper(Node root) {
        if(root == null) {
            return;
        }
        printPostOrderHelper(root.left);
        printPostOrderHelper(root.right);
        System.out.print(root.key + " ");
    }

    public List<Integer> inOrderList() {
        List<Integer> list = new ArrayList<>();
        inOrderListHelper(root, list);
        return list;
    }

    private static void inOrderListHelper(Node root, List<Integer> list) {
        if(root == null) {
            return;
        }
        inOrderListHelper(root.left, list);
        list.add(root.key);
        inOrderListHelper(root.right, list);
    }


    public List<Integer> preOrderList() {
        List<Integer> list = new ArrayList<>();
        preOrderListHelper(root, list);
        return list;
    }

    private static void preOrderListHelper(Node root, List<Integer> list) {
        if(root == null) {
            return;
        }
        list.add(root.key);
        preOrderListHelper(root.left, list);
        preOrderListHelper(root.right, list);
    }

    public List<Integer> postOrderList() {
        List<Integer> list = new ArrayList<>();
        postOrderListHelper(root, list);
        return list;
    }

    private static void postOrderListHelper(Node root, List<Integer> list) {
        if(root == null) {
            return;
        }
        postOrderListHelper(root.left, list);
        postOrderListHelper(root.right, list);
        list.add(root.key);
    }

    public List<Integer> bfsList() {
        List<Integer> list = new ArrayList<>();
        if(this.root == null) {
            return list;
        }

        Deque<Node> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);

        while(!nodeQueue.isEmpty()) {
            Node node = nodeQueue.pop();
            list.add(node.key);
            if(node.left != null) {
                nodeQueue.add(node.left);
            }
            if(node.right != null) {
                nodeQueue.add(node.right);
            }
        }

        return list;
    }

    // accepts two binary search trees with no requirement for them to be balanced
    // produces a perfectly balanced binary search tree consisting of the keys of the two trees
    public static BinarySearchTree mergeTreesIntoBalancedBST(BinarySearchTree left, BinarySearchTree right) {
        List<Integer> leftSortedValues = left.inOrderList();
        List<Integer> rightSortedValues = right.inOrderList();
        List<Integer> allSortedValues = mergeSortedLists(leftSortedValues, rightSortedValues);
        Node newRoot = getBalancedBSTFromSortedList(allSortedValues, 0, allSortedValues.size() - 1);
        BinarySearchTree tree = new BinarySearchTree(newRoot);
        return tree;
    }

    private static Node getBalancedBSTFromSortedList(List<Integer> sortedList, int left, int right) {
        if(left > right) {
            return null;
        }

        int midIndex = left + (right - left) / 2;
        Node root = new Node(sortedList.get(midIndex));
        root.left = getBalancedBSTFromSortedList(sortedList, left, midIndex - 1);
        root.right = getBalancedBSTFromSortedList(sortedList, midIndex + 1, right);
        return root;
    }

    private static List<Integer> mergeSortedLists(List<Integer> leftSortedValues, List<Integer> rightSortedValues) {
        List<Integer> list = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;
        while(leftIndex < leftSortedValues.size() && rightIndex < rightSortedValues.size()) {
            Integer leftMin = leftSortedValues.get(leftIndex);
            Integer rightMin = rightSortedValues.get(rightIndex);
            if(leftMin < rightMin) {
                list.add(leftMin);
                leftIndex++;
            } else {
                list.add(rightMin);
                rightIndex++;
            }
        }
        while(leftIndex < leftSortedValues.size()) {
            Integer value = leftSortedValues.get(leftIndex);
            list.add(value);
            leftIndex++;
        }
        while(rightIndex < rightSortedValues.size()) {
            Integer value = rightSortedValues.get(rightIndex);
            list.add(value);
            rightIndex++;
        }
        return list;
    }

    // same function as above in semantics but doesn't conserve the input trees
    // only rearranges pointers without creating new nodes
    public static BinarySearchTree inplaceMergeBSTsIntoBalancedBST(BinarySearchTree left, BinarySearchTree right) {
        List<Node> leftSortedNodes = new ArrayList<>();
        getListOfSortedNodes(left.root, leftSortedNodes);
        List<Node> rightSortedNodes = new ArrayList<>();
        getListOfSortedNodes(right.root, rightSortedNodes);
        List<Node> allSortedNodes = mergeListsOfSortedNodes(leftSortedNodes, rightSortedNodes);
        Node newRoot = getBalancedBSTFromSortedNodeList(allSortedNodes, 0, allSortedNodes.size() - 1);
        BinarySearchTree tree = new BinarySearchTree(newRoot);
        return tree;
    }

    private static void getListOfSortedNodes(Node root, List<Node> list) {
        if(root == null) {
            return;
        }
        getListOfSortedNodes(root.left, list);
        list.add(root);
        getListOfSortedNodes(root.right, list);
    }

    private static List<Node> mergeListsOfSortedNodes(List<Node> leftSortedNodes, List<Node> rightSortedNodes) {
        List<Node> fullList = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        while(leftIndex < leftSortedNodes.size() && rightIndex < rightSortedNodes.size()) {
            int leftValue = leftSortedNodes.get(leftIndex).key;
            int rightValue = rightSortedNodes.get(rightIndex).key;
            if(leftValue < rightValue) {
                fullList.add(leftSortedNodes.get(leftIndex));
                leftIndex++;
            } else {
                fullList.add(rightSortedNodes.get(rightIndex));
                rightIndex++;
            }
        }
        while(leftIndex < leftSortedNodes.size()) {
            fullList.add(leftSortedNodes.get(leftIndex));
            leftIndex++;
        }
        while(rightIndex < rightSortedNodes.size()) {
            fullList.add(rightSortedNodes.get(rightIndex));
            rightIndex++;
        }
        return fullList;
    }

    private static Node getBalancedBSTFromSortedNodeList(List<Node> sortedNodes, int left, int right) {
        if(left > right) {
            return null;
        }
        int midIndex = left + (right - left) / 2;
        Node root = sortedNodes.get(midIndex);
        root.left = getBalancedBSTFromSortedNodeList(sortedNodes, left, midIndex - 1);
        root.right = getBalancedBSTFromSortedNodeList(sortedNodes, midIndex + 1, right);
        return root;
    }

    public static void main(String[] args) {
        BinarySearchTree tree1 = new BinarySearchTree();
        tree1.insert(50);
        tree1.insert(30);
        tree1.insert(20);
        tree1.insert(40);
        tree1.insert(70);
        tree1.insert(60);
        tree1.insert(80);

        /* tree 1
              50
           /     \
          30      70
         /  \    /  \
       20   40  60   80 */

        BinarySearchTree tree2 = new BinarySearchTree();
        tree2.insert(75);
        tree2.insert(25);
        tree2.insert(81);
        tree2.insert(76);
        tree2.insert(86);
        tree2.insert(100);
        tree2.insert(90);
        /* tree 2
              75
           /     \
          25      81
                 /  \
                76   86
                      \
                       100
                       /
                      90    */

        BinarySearchTree merged = BinarySearchTree.mergeTreesIntoBalancedBST(tree1, tree2);
        System.out.println("BFS and preorder printing of the merged tree:");
        System.out.println(merged.bfsList());
        System.out.println(merged.preOrderList());

        // check that tree 1 and tree 2 are unaffected
        System.out.println("BFS of first tree:");
        System.out.println(tree1.bfsList());
        System.out.println("BFS of second tree:");
        System.out.println(tree2.bfsList());

        System.out.println("BFS and preorder printing of the merged in place tree:");
        BinarySearchTree mergedInplace = BinarySearchTree.mergeTreesIntoBalancedBST(tree1, tree2);
        System.out.println(mergedInplace.bfsList());
        System.out.println(mergedInplace.preOrderList());

        /* Result merged tree:
                      70
                /           \
              30              81
            /    \          /    \
          20      50      76      90
            \     / \     / \     /  \
             25  40  60  75  80  86  100
         */
    }
}
