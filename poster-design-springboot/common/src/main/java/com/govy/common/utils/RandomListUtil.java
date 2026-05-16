package com.govy.common.utils;

import java.util.ArrayList;
import java.util.List;

public class RandomListUtil {

    public static List<Integer> getNumberList(List<Integer> now, int up) {
        List<Integer> ans = new ArrayList<Integer>();
        while (ans.size() < up) {
            boolean b = true;
            int n = (int) (Math.random() * now.size());
            for (int j = 0; j < ans.size(); j++) {
                if (ans.get(j) == now.get(n)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                ans.add(now.get(n));
            }
        }
        return ans;
    }
}