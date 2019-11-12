package sven.phd.iot.students.bram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HelperFunctions {

    public static JSONArray sortJSONArray(JSONArray arr, String key) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for(int i = 0; i < arr.length(); i++) {
            list.add(arr.optJSONObject(i));
        }
        Collections.sort(list, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private final String KEY_NAME = key;

            @Override
            public int compare(JSONObject a, JSONObject b) {
                //Look if the values are numerical
                double dA, dB;
                try {
                    dA = Double.parseDouble((String) a.get(KEY_NAME));
                    dB = Double.parseDouble((String) b.get(KEY_NAME));

                    if(dA < dB) return 1;
                    if(dA > dB) return -1;
                    return 0;

                } catch (JSONException e) {

                }


                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                }
                catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });
        JSONArray sorted = new JSONArray();
        list.forEach((obj) -> {
            sorted.put(obj);
        });
        return sorted;
    }

    /**
     * Calculate the similarity of s1 and s2 (percentage)
     * @param s1
     * @param s2
     * @return the similarity
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    /**
     * Calculate the edit distance between two strings
     * @param s1
     * @param s2
     * @return
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
