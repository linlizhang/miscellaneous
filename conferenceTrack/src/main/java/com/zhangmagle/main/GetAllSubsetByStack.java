package com.zhangmagle.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GetAllSubsetByStack {

    /** Set a value for target sum */
   private int targetNum = 180;

    private Stack<Integer> stack = new Stack<Integer>();

    /** Store found sub-array equals to TARGET_SUM in stack */
    private List<List<Integer>> allSub = new ArrayList<List<Integer>>();

    public void populateSubset(Integer[] data, int fromIndex, int endIndex, int sumInStack) {

        /*
        * Check if sum of elements stored in Stack is equal to the expected
        * target sum.
        *
        * If so, call print method to print the candidate satisfied result.
        */
        if (sumInStack == targetNum) {
            print(stack);
        }

        for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {

            if (sumInStack + data[currentIndex] <= targetNum) {
                stack.push(data[currentIndex]);
                sumInStack += data[currentIndex];

                /*
                * Make the currentIndex +1, and then use recursion to proceed
                * further.
                */
                populateSubset(data, currentIndex + 1, endIndex, sumInStack);
                sumInStack -= (Integer) stack.pop();
            }
        }
    }

    /**
     * Store all sub-arrays that satisfy condition
     */

    private void print(Stack<Integer> stack) {
    	List<Integer> sub = new ArrayList<Integer>();
    	for(Integer i : stack) {
    		 sub.add(i);
    	}
    	allSub.add(sub);
//        StringBuilder sb = new StringBuilder();
//        sb.append(TARGET_SUM).append(" = ");
//        for (Integer i : stack) {
//            sb.append(i).append("+");
//        }
//        System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
    }
    
    public List<List<Integer>> getAllSub() {
    	return allSub;
    }
    
    public void setTargetSum(int targetNum) {
    	this.targetNum = targetNum;
    }

    public static void main(String[] args) {
    	Integer[] DATA = {5, 30, 30, 30,30,30,45, 45, 45, 45,45,45,60, 60, 60, 60};
    	int targetNum = 180;
        GetAllSubsetByStack get = new GetAllSubsetByStack();
        get.populateSubset(DATA, 0, DATA.length, 0);
        List<List<Integer>> allSub = get.getAllSub();
        for(List<Integer> stack1: allSub) {
          StringBuilder sb = new StringBuilder();
          sb.append(targetNum).append(" = ");
          for (Integer i : stack1) {
              sb.append(i).append("+");
          }
          System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
        }
    }
}
