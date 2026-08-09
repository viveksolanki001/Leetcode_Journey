class Solution {
    public int score(String[] cards, char x) {
        int[] count1 = new int[26];
        int[] count2 = new int[26];
        int both = 0;

        for (int i = 0; i < cards.length; i++) {
            String card = cards[i];
            char c0 = card.charAt(0);
            char c1 = card.charAt(1);

            if (c0 == x && c1 == x) {
                both++;
            } else if (c0 == x) {
                count1[c1 - 'a']++;
            } else if (c1 == x) {
                count2[c0 - 'a']++;
            }
        }

        int sum1 = 0, max1 = 0;
        for (int c : count1) {
            sum1 += c;
            if (c > max1) max1 = c;
        }

        int sum2 = 0, max2 = 0;
        for (int c : count2) {
            sum2 += c;
            if (c > max2) max2 = c;
        }

        int maxPoints = 0;

        for (int i = 0; i <= both; i++) {
            int j = both - i;
            
            int currentMax1 = (i > max1) ? i : max1;
            int total1 = sum1 + i;
            int limit1 = total1 - currentMax1;
            int half1 = total1 / 2;
            int p1 = (half1 < limit1) ? half1 : limit1;
            
            int currentMax2 = (j > max2) ? j : max2;
            int total2 = sum2 + j;
            int limit2 = total2 - currentMax2;
            int half2 = total2 / 2;
            int p2 = (half2 < limit2) ? half2 : limit2;
            
            int currentPoints = p1 + p2;
            if (currentPoints > maxPoints) {
                maxPoints = currentPoints;
            }
        }

        return maxPoints;
    }
}