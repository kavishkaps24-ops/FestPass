package com.example.festpass.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class QRGenerator {
    /**
     * Generates a Base64‑encoded PNG image of a QR code for the supplied text.
     * The caller can embed the resulting string directly in an <img src="data:image/png;base64,..."/> tag.
     */
    public static String generateBase64QRCode(String text, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("QR generation failed", e);
        }
    }
}
