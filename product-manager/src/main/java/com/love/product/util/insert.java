package com.love.product.util;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class insert {
    private static final int mainCode = 100000;
    private static final String mainName = "中华人民共和国";

    private static class Location {
        int code;
        String name;
        List<Location> list;

        Location(int code, String name) {
            this.code = code;
            this.name = name;
            this.list = new ArrayList<>();
        }
    }

    private static Location getList(int rootCode, String rootName, int lv) throws IOException {
        Location location = new Location(rootCode, rootName);
        String api = "https://geo.datav.aliyun.com/areas/bound/geojson?code=" + rootCode + "_full";
        URL url = new URL(api);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray features = jsonResponse.getJSONArray("features");

        for (int i = 0; i < features.length(); i++) {
            JSONObject item = features.getJSONObject(i).getJSONObject("properties");
            int mycode = item.getInt("adcode");
            String myname = item.getString("name");
            System.out.println("|" + "========="+lv + ">" + myname);

            int childrenNum = item.getInt("childrenNum");
            if (mycode != rootCode && mycode != 0 && !myname.isEmpty() && childrenNum != 0) {
                Location childLocation = getList(mycode, myname, lv + 1);
                location.list.add(childLocation);
            } else {
                location.list.add(new Location(mycode, myname));
            }
        }
        return location;
    }

    private static void writeToFile(String content) throws IOException {
        FileWriter writer = new FileWriter("./省市区.json");
        writer.write(content);
        writer.close();
    }

    public static void main(String[] args) {
        try {
            Location location = getList(mainCode, mainName, 1);
            String jsonData = locationToJson(location);
            writeToFile(jsonData);
            System.out.println("省市区数据已写入到省市区.json文件中！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String locationToJson(Location location) {
        JSONObject json = new JSONObject();
        json.put("code", location.code);
        json.put("name", location.name);
        JSONArray jsonArray = new JSONArray();
        for (Location child : location.list) {
            jsonArray.put(locationToJson(child));
        }
        json.put("list", jsonArray);
        return json.toString();
    }
}
