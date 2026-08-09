import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            if (num < min) min = num;
            if (num > max) max = num;
        }
        
        // If all numbers are positive or all are negative, sum can never be 0
        if (min > 0 || max < 0) return result;
        
        // Frequency table for constant-time lookups
        int[] counts = new int[max - min + 1];
        for (int num : nums) {
            counts[num - min]++;
        }
        
        // Collect unique elements in sorted order
        int[] unique = new int[nums.length];
        int uniqueCount = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0) {
                unique[uniqueCount++] = i + min;
            }
        }
        
        // Case 1: Three zeros (0, 0, 0)
        if (-min >= 0 && -min <= max - min && counts[-min] >= 3) {
            result.add(Arrays.asList(0, 0, 0));
        }
        
        // Case 2 & 3: Iterate through unique pairs
        for (int i = 0; i < uniqueCount; i++) {
            int a = unique[i];
            if (a > 0) break; // First number must be <= 0
            
            // Check for pairs of identical numbers: (a, a, -2a)
            if (counts[a - min] >= 2 && a != 0) {
                int c = -(a << 1); // equivalent to -2 * a
                if (c >= min && c <= max && counts[c - min] > 0) {
                    result.add(Arrays.asList(a, a, c));
                }
            }
            
            // Check for distinct triplets: (a, b, c) where a < b < c
            for (int j = i + 1; j < uniqueCount; j++) {
                int b = unique[j];
                int c = -(a + b);
                
                if (c < b) break; // Maintain order a < b <= c to prevent duplicates
                
                if (c == b) {
                    if (counts[b - min] >= 2) {
                        result.add(Arrays.asList(a, b, b));
                    }
                    break;
                } else if (c <= max && counts[c - min] > 0) {
                    result.add(Arrays.asList(a, b, c));
                }
            }
        }
        
        return result;
    }
}