package com.slotvinskiy;
// Есть документ со списком URL:
//https://drive.google.com/open?id=1wVBKKxpTKvWwuCzqY1cVXCQZYCsdCXTl
//Вывести топ 10 доменов которые встречаются чаще всего.
//В документе могут встречатся пустые и недопустимые строки.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        String fileName = "./resources/urls.txt";
        Map<String, Integer> map = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {                      //skip empty string
                    continue;
                }
                for (int i = 0; i < line.length(); i++) {   //separate domains from URL and save it in sb
                    if (line.charAt(i) != '/') {
                        sb.append(line.charAt(i));
                    } else {
                        break;
                    }
                }
                putLineIn(map, sb.toString().trim());
                sb.delete(0, sb.length());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        showTop10(map);
    }

    private static void putLineIn(Map<String, Integer> map, String domain) {
        if (map.containsKey(domain)) {
            int oldNumberOfDomainLines = map.get(domain);
            map.put(domain, oldNumberOfDomainLines + 1);
        } else {
            map.put(domain, 1);
        }
    }

    private static void showTop10(Map<String, Integer> map) {
        Map<String, Integer> topTen =
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(10)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        print(topTen);
    }

    private static void print(Map<String, Integer> topTen) {
        for (Map.Entry<String, Integer> entry : topTen.entrySet()) {
            System.out.println(entry.getValue() + " - " + entry.getKey());
        }
    }
}
