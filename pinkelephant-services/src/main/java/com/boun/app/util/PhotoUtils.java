package com.boun.app.util;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.app.exception.PinkElephantValidationException;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PhotoUtils {

    private static final Locale LOCALE = Locale.forLanguageTag("tr-TR");

    private static final Map<Character, Character> CHARACTER_REPLACEMENT_MAP;
    static {
       CHARACTER_REPLACEMENT_MAP = new HashMap<>();
       CHARACTER_REPLACEMENT_MAP.put('ç', 'c');
       CHARACTER_REPLACEMENT_MAP.put('ğ', 'g');
       CHARACTER_REPLACEMENT_MAP.put('ı', 'i');
       CHARACTER_REPLACEMENT_MAP.put('ö', 'o');
       CHARACTER_REPLACEMENT_MAP.put('ş', 's');
       CHARACTER_REPLACEMENT_MAP.put('ü', 'u');
    }

    private PhotoUtils() {}

    public static InputStream checkAndConvertToJpeg(byte[] imageBytes) {
        String mimeType = "";

        try {
            mimeType = Magic.getMagicMatch(imageBytes, false).getMimeType();
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
            throw new PinkElephantValidationException("Uploaded file is not recognized");
        }

        if (! mimeType.startsWith("image/")) {
            throw new PinkElephantValidationException("Uploaded file is not an image file");
        }

        InputStream is = new ByteArrayInputStream(imageBytes);
        return resize(is);
    }

    private static InputStream resize(InputStream is) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(is);
            int width = image.getWidth();
            int height = image.getHeight();
            //crop
            if (width > height) {
                int diff = width - height;
                int leftCrop = diff / 2;
                image = Scalr.crop(image, leftCrop, 0, height, height);
            } else if (width < height) {
                int diff = height - width;
                int topCrop = diff / 2;
                image = Scalr.crop(image, 0, topCrop, width, width);
            }
            //scale
            image = Scalr.resize(image, 80);
            //convert
            ImageIO.write(image, "jpg", baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception e) {
            throw new PinkElephantRuntimeException(400, "", "Internal Server Error", "");
        }
    }

    public static String normalizeString(String teamName) {
        StringBuilder sb = new StringBuilder(teamName.length());
        for (Character c : teamName.toLowerCase(LOCALE).toCharArray()) {
            Character replacement = CHARACTER_REPLACEMENT_MAP.get(c);
            sb.append( replacement == null ? c : replacement);
        }
        return sb.toString();
    }
}
