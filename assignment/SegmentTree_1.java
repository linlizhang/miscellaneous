import junit.framework.Assert;
/**
 * Range Sum Query - Mutable
 **/
public class SegmentTree_1 {

    private int[] nums;
    private Node root;

    class Node {
        int value;
        int from;
        int to;
        Node left;
        Node right;

        public String toString() {
            return "from: " + from + ";to:  " + to + ";value: " + value;
        }
    }

    public SegmentTree_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        this.nums = nums;
        root = put(0, nums.length - 1);
    }

    private Node put(int lo, int hi) {
        if (hi < lo)
            return null;

        if (hi == lo) {
            Node node = new Node();
            node.value = nums[lo];
            node.from = lo;
            node.to = hi;
            return node;
        }

        int mid = lo + (hi - lo) / 2;

        Node left = put(lo, mid);
        Node right = put(mid + 1, hi);
        Node parent = new Node();
        parent.from = lo;
        parent.to = hi;
        parent.left = left;
        parent.right = right;
        parent.value = left.value + right.value;
        return parent;
    }

    void update(int i, int val) {
        int dis = val - nums[i];
        nums[i] = val;
        updateTree(root, i, dis);
    }
    
    private void updateTree(Node node, int index, int dis) {
        
        int mid = node.from + (node.to - node.from)/2;
        if (mid > index) {
            node.value += dis;
            updateTree(node.left, index, dis);
        } else if (mid < index) {
            node.value += dis;
            updateTree(node.right, index, dis);
        }
    }

    public int sumRange(int i, int j) {
        if (i == j) {
            return nums[i];
        } else if (i == 0) {
            return getSum(j);
        } else {
            return getSum(j) - getSum(i - 1);
        }
    }

    private int getSum(int index) {
        Node node = root;
        while (node.to > index) {
            node = node.left;
        }
        return node.value;
    }

    public static void main(String[] args) {
        int[] nums = { 1, 3, 5, 7, 9, 11 };
        SegmentTree_1 tree = new SegmentTree_1(nums);
        Assert.assertEquals(4, tree.sumRange(0, 1));
        tree.update(1, 3);
        Assert.assertEquals(32, tree.sumRange(2, 5));
        tree.update(4, 5);
        Assert.assertEquals(28, tree.sumRange(2, 5));
    }
}
