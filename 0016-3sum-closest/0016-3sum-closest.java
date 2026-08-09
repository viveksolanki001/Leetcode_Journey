class Solution {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int cs = nums[0]+nums[1]+nums[2];
        for (int i = 0; i < nums.length - 2; i++){
        int left = i + 1;
        int right = (nums.length) -1;
        
        while (left < right){
            int cls = nums[i] + nums[left] + nums[right];

            if(Math.abs(target - cls) < Math.abs(target - cs)){
                cs = cls;
            }
            if (cls > target){
                right--;
            }
            else if(cls < target){
                left++;
            }
            else {
                return cls;
            }
        }
        }return cs;
    }
}
