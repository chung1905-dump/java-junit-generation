package app.signature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TgtReader {
    public static Map<Integer, Set<Integer>> readTargetFile(String filePath) {
        Map<Integer, Set<Integer>> ret = new HashMap<>();
        try {
            String s;
            Pattern p = Pattern.compile("^([\\w.]*)\\((.*)\\):(.*)$");
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            while ((s = in.readLine()) != null) {
                Matcher m = p.matcher(s);
                if (!m.find()) continue;

                String g1 = m.group(1);
                String[] a1 = g1.split("\\.");
                String method = a1[a1.length - 1];

                String g2 = m.group(2);
                String[] params = g2.split(",");

                String g3 = m.group(3);
                String[] a3 = g3.split(",");

                Set<Integer> branch = new HashSet<>();
                for (String value : a3) {
                    branch.add(Integer.parseInt(value.trim()));
                }

                ret.put(hashMethodSignature(method, params), branch);
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong format file: " + filePath);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO error: " + filePath);
            System.exit(1);
        }
        return ret;
    }

    public static Integer hashMethodSignature(String method, Object[] params) {
        for (Object p : params) {
            method = method.concat(p.toString());
        }
        return method.hashCode();
    }
}

