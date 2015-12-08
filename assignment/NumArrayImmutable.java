import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Range Sum Query -- immutable
 * @author my
 *
 */
public class NumArrayImmutable {
    private int[] sumArr;
    private int[] nums;

    static Logger logger = Logger.getGlobal();
  
    public NumArrayImmutable(int[] nums) {
        if (null == nums || nums.length == 0) {
            return;
        }
        this.nums = nums;
        if (nums.length > 0) {
            sumArr = new int[nums.length];
            sumArr[0] = nums[0];
            for (int i = 1; i < sumArr.length; i++) {
                sumArr[i] = sumArr[i - 1] + nums[i];
            }
        }
    }
    
    public int sumRange(int i, int j) {
        if (null == nums || nums.length == 0) {
            return 0;
        }
        if (i == j) {
            return nums[j];
        } else if (i == 0){
            return sumArr[j];
        } else {
            return sumArr[j] - sumArr[i - 1];
        }
    }
    
    public static void main(String[] args) {
        int[] nums = {};
        long before = System.currentTimeMillis();
        NumArrayImmutable numArray = new NumArrayImmutable(nums);
        numArray.sumRange(0,2);numArray.sumRange(2,5);numArray.sumRange(0,5);
        System.out.println("wasted time: " + (System.currentTimeMillis() - before));
        logger.log(Level.INFO, "wasted time");
    }
}


// Your NumArray object will be instantiated and called as such:
// NumArray numArray = new NumArray(nums);
// numArray.sumRange(0, 1);
// numArray.sumRange(1, 2);