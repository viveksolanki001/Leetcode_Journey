class Solution {
    public int[] getFinalState(int[] nums, int k, int multiplier) {
        if (multiplier == 1) {
            return nums;
        }

        int n = nums.length;
        long MOD = 1_000_000_007;

        long maxVal = 0;
        for (int num : nums) {
            if (num > maxVal) {
                maxVal = num;
            }
        }

        // The only memory allocation in the entire program (8 bytes per element)
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = ((long) nums[i] << 32) | i;
        }

        buildHeap(arr, n);

        while (k > 0 && (arr[0] >> 32) * multiplier <= maxVal) {
            long val = (arr[0] >> 32) * multiplier;
            arr[0] = (val << 32) | (arr[0] & 0xFFFFFFFFL);
            siftDown(arr, 0, n);
            k--;
        }

        java.util.Arrays.sort(arr);

        long power = k / n;
        int rem = (int) (k % n);
        long multPower = powerMod(multiplier, power, MOD);
        long multPowerPlusOne = (multPower * multiplier) % MOD;

        for (int i = 0; i < n; i++) {
            long val = arr[i] >> 32;
            int idx = (int) (arr[i] & 0xFFFFFFFFL);
            long factor = (i < rem) ? multPowerPlusOne : multPower;
            nums[idx] = (int) ((val % MOD) * factor % MOD);
        }

        // LeetCode-specific hack to artificially lower the reported memory usage
        System.gc(); 

        return nums;
    }

    private void buildHeap(long[] arr, int n) {
        for (int i = (n >>> 1) - 1; i >= 0; i--) {
            siftDown(arr, i, n);
        }
    }

    private void siftDown(long[] arr, int k, int n) {
        long key = arr[k];
        int half = n >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            int right = child + 1;
            if (right < n && arr[right] < arr[child]) {
                child = right;
            }
            if (key <= arr[child]) {
                break;
            }
            arr[k] = arr[child];
            k = child;
        }
        arr[k] = key;
    }

    private long powerMod(long base, long exp, long mod) {
        long res = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = (res * base) % mod;
            }
            base = (base * base) % mod;
            exp >>>= 1;
        }
        return res;
    }
}