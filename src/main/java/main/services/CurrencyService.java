/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.services;

/**
 *
 * @author hemalgondaliya
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyService {

    private static String currencyURL = "http://api.fixer.io/latest";

    public static float getCurrentRate(String currency) throws IOException, JSONException {
        JSONObject json = readJsonFromUrl();
        JSONObject rateJson = json.getJSONObject("rates");
        String rate = rateJson.get(currency).toString();
        return Float.parseFloat(rate);
    }

    private static JSONObject readJsonFromUrl() throws IOException, JSONException {
        InputStream is = new URL(currencyURL).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
