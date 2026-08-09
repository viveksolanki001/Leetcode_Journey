class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums.length==0) return 0;
        int i=0;
        int j=i+1;
        int unique = 1;
        int k=(nums.length);
        while(j<k)
        {
         if(nums[i]==nums[j])
         {
            ++j;
         }
         else if (nums[i]!=nums[j])
         {
            nums[i+1]=nums[j];
            ++unique;
            ++i;
            ++j;
         }
        }
        return(unique);
    }
}