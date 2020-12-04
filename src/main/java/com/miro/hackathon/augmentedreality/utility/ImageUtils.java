package com.miro.hackathon.augmentedreality.utility;

import org.springframework.core.io.InputStreamResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public static Image makeItTransparent(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                return (rgb << 8) & 0xFF000000;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static BufferedImage applyMask(BufferedImage image, Image mask) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resultImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
        g2.setComposite(ac);
        g2.drawImage(mask, 0, 0, null);
        g2.dispose();
        return resultImage;
    }

    public static InputStreamResource convertToResource(BufferedImage scaledMask) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(scaledMask, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return new InputStreamResource(is);
    }
}
