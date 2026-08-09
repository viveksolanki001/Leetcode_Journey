class Solution {
    public int[] getFinalState(int[] nums, int k, int multiplier) {
        if (multiplier == 1) {
            return nums;
        }

        int n = nums.length;
        long MOD = 1_000_000_007;

        long maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }

        java.util.PriorityQueue<long[]> pq = new java.util.PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return Long.compare(a[0], b[0]);
            }
            return Long.compare(a[1], b[1]);
        });

        for (int i = 0; i < n; i++) {
            pq.offer(new long[]{nums[i], i});
        }

        while (k > 0 && pq.peek()[0] * multiplier <= maxVal) {
            long[] curr = pq.poll();
            curr[0] *= multiplier;
            pq.offer(curr);
            k--;
        }

        long power = k / n;
        int rem = (int) (k % n);

        long multPower = powerMod(multiplier, power, MOD);

        while (!pq.isEmpty()) {
            long[] curr = pq.poll();
            long val = curr[0];
            int idx = (int) curr[1];

            long extra = (rem > 0) ? multiplier : 1;
            if (rem > 0) rem--;

            long totalMult = (multPower * extra) % MOD;
            nums[idx] = (int) ((val % MOD * totalMult) % MOD);
        }

        return nums;
    }

    private long powerMod(long base, long exp, long mod) {
        long res = 1;
        base %= mod;
        while (exp > 0) {
            if (exp % 2 == 1) {
                res = (res * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return res;
    }
}
