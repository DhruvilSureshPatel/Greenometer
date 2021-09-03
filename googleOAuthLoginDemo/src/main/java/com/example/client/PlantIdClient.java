package com.example.client;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

public class PlantIdClient {

    private static byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }

    private static String base64EncodeFromFile(String fileString) {
        File file = new File(fileString);
        return Base64.getEncoder().encodeToString(readContentIntoByteArray(file));
    }

    public static String sendPostRequest(String urlString, JSONObject data) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        OutputStream os = con.getOutputStream();
        os.write(data.toString().getBytes());
        os.close();

        InputStream is = con.getInputStream();
        String response = new String(IOUtils.toByteArray(is));

        System.out.println("Response code : " + con.getResponseCode());
        System.out.println("Response : " + response);
        con.disconnect();
        return response;
    }

    public static Map<String, Object> getDetails(String base64Data) throws Exception {
        String apiKey = "jRRET776Jopf5dLCskNWxcXyP4aEzmKUPKKf5xNZwlpRs1dKq3";
        JSONObject data = new JSONObject();
        data.put("api_key", apiKey);

        JSONArray images = new JSONArray();
        images.put(base64Data);
        data.put("images", images);

        // add modifiers
        JSONArray modifiers = new JSONArray()
                .put("crops_fast")
                .put("similar_images");
        data.put("modifiers", modifiers);

        // add language
        data.put("plant_language", "en");

        // add details
        JSONArray plantDetails = new JSONArray()
                .put("common_names")
                .put("url")
                .put("name_authority")
                .put("wiki_description")
                .put("taxonomy")
                .put("synonyms");
        data.put("plant_details", plantDetails);

        String response = sendPostRequest("https://api.plant.id/v2/identify", data);
        JSONObject json = new JSONObject(response);
        return json.toMap();
    }

    public static void main(String[] args) throws Exception {

        String apiKey = "jRRET776Jopf5dLCskNWxcXyP4aEzmKUPKKf5xNZwlpRs1dKq3";

        // read image from local file system and encode
        String [] flowers = new String[] {"/Users/dhruvil/Greenometer/googleOAuthLoginDemo/src/main/java/com/example/client/images.jpeg"};


        JSONObject data = new JSONObject();
        data.put("api_key", apiKey);

        // add images
        JSONArray images = new JSONArray();
        for(String filename : flowers) {
            String fileData = base64EncodeFromFile(filename);
            images.put(fileData);
        }
        data.put("images", images);


        // add modifiers
        JSONArray modifiers = new JSONArray()
                .put("crops_fast")
                .put("similar_images");
        data.put("modifiers", modifiers);

        // add language
        data.put("plant_language", "en");

        // add details
        JSONArray plantDetails = new JSONArray()
                .put("common_names")
                .put("url")
                .put("name_authority")
                .put("wiki_description")
                .put("taxonomy")
                .put("synonyms");
        data.put("plant_details", plantDetails);

        sendPostRequest("https://api.plant.id/v2/identify", data);
    }
}
