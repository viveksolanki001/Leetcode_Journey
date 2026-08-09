import java.util.Arrays;

class Solution {
    public int maxProduct(int[] nums, int k, int limit) {
        // Find the absolute max possible sum to size our `seen` array safely
        int maxSum = 0;
        for (int x : nums) {
            maxSum += Math.abs(x);
        }
        int offset = maxSum;
        
        // Versioned tracking arrays to achieve O(1) deduplication without clearing
        int[] seen0 = new int[2 * maxSum + 1];
        int[] seen1 = new int[2 * maxSum + 1];
        int version0 = 0;
        int version1 = 0;

        int[][] dp0 = new int[limit + 1][];
        int[][] dp1 = new int[limit + 1][];
        boolean[] active = new boolean[limit + 1];
        int[] activeP = new int[limit + 1];
        int activeCount = 0;

        int[] zero0 = new int[0];
        int[] zero1 = new int[0];
        int[] any0 = new int[0];
        int[] any1 = new int[0];

        // Shared buffers to temporarily hold unique sums for the current state
        int[] buf0 = new int[2 * maxSum + 1];
        int[] buf1 = new int[2 * maxSum + 1];

        for (int num : nums) {
            version0++; version1++;
            int b0Count = 0, b1Count = 0;

            // 1. Update anyDp (All possible subsequences)
            for (int s : any0) {
                if (seen0[s + offset] != version0) { seen0[s + offset] = version0; buf0[b0Count++] = s; }
            }
            for (int s : any1) {
                if (seen1[s + offset] != version1) { seen1[s + offset] = version1; buf1[b1Count++] = s; }
            }

            if (seen0[num + offset] != version0) { seen0[num + offset] = version0; buf0[b0Count++] = num; }
            for (int s : any1) {
                int ns = s + num;
                if (seen0[ns + offset] != version0) { seen0[ns + offset] = version0; buf0[b0Count++] = ns; }
            }
            for (int s : any0) {
                int ns = s - num;
                if (seen1[ns + offset] != version1) { seen1[ns + offset] = version1; buf1[b1Count++] = ns; }
            }
            int[] nextAny0 = Arrays.copyOf(buf0, b0Count);
            int[] nextAny1 = Arrays.copyOf(buf1, b1Count);

            // 2. Update zeroDp (Subsequences containing at least one zero)
            version0++; version1++;
            b0Count = 0; b1Count = 0;
            for (int s : zero0) {
                if (seen0[s + offset] != version0) { seen0[s + offset] = version0; buf0[b0Count++] = s; }
            }
            for (int s : zero1) {
                if (seen1[s + offset] != version1) { seen1[s + offset] = version1; buf1[b1Count++] = s; }
            }

            if (num == 0) {
                if (seen0[0 + offset] != version0) { seen0[0 + offset] = version0; buf0[b0Count++] = 0; }
                for (int s : any1) {
                    if (seen0[s + offset] != version0) { seen0[s + offset] = version0; buf0[b0Count++] = s; }
                }
                for (int s : any0) {
                    if (seen1[s + offset] != version1) { seen1[s + offset] = version1; buf1[b1Count++] = s; }
                }
            } else {
                for (int s : zero1) {
                    int ns = s + num;
                    if (seen0[ns + offset] != version0) { seen0[ns + offset] = version0; buf0[b0Count++] = ns; }
                }
                for (int s : zero0) {
                    int ns = s - num;
                    if (seen1[ns + offset] != version1) { seen1[ns + offset] = version1; buf1[b1Count++] = ns; }
                }
            }
            int[] nextZero0 = Arrays.copyOf(buf0, b0Count);
            int[] nextZero1 = Arrays.copyOf(buf1, b1Count);

            // 3. Update main product dp
            int[][] nextDp0 = new int[limit + 1][];
            int[][] nextDp1 = new int[limit + 1][];
            boolean[] nextActive = new boolean[limit + 1];
            int[] nextActiveP = new int[limit + 1];
            int nextActiveCount = 0;

            if (num > 0) {
                for (int p = 1; p <= limit; p++) {
                    boolean hasOld = active[p];
                    boolean isNum = (p == num);
                    boolean isMul = (p % num == 0) && active[p / num];

                    if (!hasOld && !isNum && !isMul) continue;

                    version0++; version1++;
                    b0Count = 0; b1Count = 0;

                    // Transition 1: Ignore current number
                    if (hasOld) {
                        if (dp0[p] != null) {
                            for (int s : dp0[p]) {
                                if (seen0[s + offset] != version0) { seen0[s + offset] = version0; buf0[b0Count++] = s; }
                            }
                        }
                        if (dp1[p] != null) {
                            for (int s : dp1[p]) {
                                if (seen1[s + offset] != version1) { seen1[s + offset] = version1; buf1[b1Count++] = s; }
                            }
                        }
                    }

                    // Transition 2: Start new subsequence with just this number
                    if (isNum) {
                        if (seen0[num + offset] != version0) { seen0[num + offset] = version0; buf0[b0Count++] = num; }
                    }

                    // Transition 3: Extend a previous valid product subsequence
                    if (isMul) {
                        int prevP = p / num;
                        if (dp1[prevP] != null) {
                            for (int s : dp1[prevP]) {
                                int ns = s + num;
                                if (seen0[ns + offset] != version0) { seen0[ns + offset] = version0; buf0[b0Count++] = ns; }
                            }
                        }
                        if (dp0[prevP] != null) {
                            for (int s : dp0[prevP]) {
                                int ns = s - num;
                                if (seen1[ns + offset] != version1) { seen1[ns + offset] = version1; buf1[b1Count++] = ns; }
                            }
                        }
                    }

                    if (b0Count > 0 || b1Count > 0) {
                        nextActive[p] = true;
                        nextActiveP[nextActiveCount++] = p;
                        if (b0Count > 0) nextDp0[p] = Arrays.copyOf(buf0, b0Count);
                        if (b1Count > 0) nextDp1[p] = Arrays.copyOf(buf1, b1Count);
                    }
                }
            } else {
                // If num == 0, we can't create products > 0, carry over active combinations unchanged
                for (int i = 0; i < activeCount; i++) {
                    int p = activeP[i];
                    nextActive[p] = true;
                    nextActiveP[nextActiveCount++] = p;
                    nextDp0[p] = dp0[p];
                    nextDp1[p] = dp1[p];
                }
            }

            dp0 = nextDp0;
            dp1 = nextDp1;
            active = nextActive;
            activeP = nextActiveP;
            activeCount = nextActiveCount;

            any0 = nextAny0;
            any1 = nextAny1;
            zero0 = nextZero0;
            zero1 = nextZero1;
        }

        for (int p = limit; p >= 1; p--) {
            if ((dp0[p] != null && contains(dp0[p], k)) || (dp1[p] != null && contains(dp1[p], k))) {
                return p;
            }
        }

        if (contains(zero0, k) || contains(zero1, k)) {
            return 0;
        }

        return -1;
    }

    private boolean contains(int[] arr, int val) {
        for (int x : arr) {
            if (x == val) return true;
        }
        return false;
    }
}