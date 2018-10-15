package com.qylk.gold;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataRepository {

    public static List<Pair<Long, Float>> getData() {
        String myurl = "https://fundmobapi.eastmoney.com/FundMApi/FundNetDiagram.ashx?version=5.5.1&plat=Android&appType=ttjj&RANGE=6y&FCODE=000217&deviceid=11111&product=EFund";
        HttpURLConnection connection = null;
        List<Pair<Long, Float>> ret = new ArrayList<>();
        try {
            URL url = new URL(myurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            String data = response.toString();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("Datas");
            int sum = jsonArray.length();

            for (int i = 0; i < sum; i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String day = item.getString("FSRQ");
                float value = (float) item.getDouble("LJJZ");
                Pair<Long, Float> pair = new Pair<>(stringToDate(day).getTime() / 3600 / 24 / 1000, value);
                ret.add(pair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return ret;
    }

    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
