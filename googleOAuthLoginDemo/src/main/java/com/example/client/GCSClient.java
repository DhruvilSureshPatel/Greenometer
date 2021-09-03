package com.example.client;
import com.example.LoginDemoApplication;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.commons.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GCSClient {

    public static void main(final String[] args) throws Exception {
        System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        detectProperties();
    }
    public static void detectProperties() throws IOException {
        String filePath = "/Users/vaibhavkumar/Desktop/Cactus.jpeg";
        detectProperties(filePath);
    }

    // Detects image properties such as color frequency from the specified local image.
    public static void detectProperties(String filePath) throws IOException {
        byte[] imageData = Base64.encodeBase64(filePath.getBytes());
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
                //DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();

                if (res.getLocalizedObjectAnnotationsList().isEmpty()) {
                    float score_green = 0;
                    for (ColorInfo color : colors.getColorsList()) {
                        score_green += color.getColor().getGreen();

                        System.out.println(score_green);
                    }
                    System.out.println("score_green");
                }
            }
        }
    }

    private void detectViaColor() {

    }
}