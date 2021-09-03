package com.example.client;

import com.example.services.PlantService;
import com.google.cloud.vision.v1.*;
import com.google.inject.Inject;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interacts with Google Cloud Systems and fetches the response.
 */
public class GCSClient {

    //TODO : For Testing Replace with method {@processBase64Image} later.
    public static void main(String[] args) throws IOException {

        processImageFromFilePath("/Users/vaibhavkumar/Desktop/Cactus.jpeg");
    }

    public static void processBase64Image(byte[] base64Image) throws IOException {
        ByteString imgBytesBase64 = ByteString.copyFrom(base64Image);
        Image img = Image.newBuilder().setContent(imgBytesBase64).build();
        detectProperties(img);
    }

    public static void processImageFromFilePath(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] imageData = readContentIntoByteArray(file);
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString byteString = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(byteString).build();
        detectProperties(img);
    }


    public static void detectProperties(Image img) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        Feature feat = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);


        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                if (res.getLocalizedObjectAnnotationsList().isEmpty()) {
                    DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
                    System.out.println(colors.getColors(1));
                }
            }
        }
    }

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

}