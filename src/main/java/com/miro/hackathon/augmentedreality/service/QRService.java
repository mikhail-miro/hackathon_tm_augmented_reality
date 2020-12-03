package com.miro.hackathon.augmentedreality.service;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.miro.hackathon.augmentedreality.utility.ImageUtils.convertToResource;
import static java.util.Objects.isNull;

@Service
public class QRService {

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    private static final int logoWidth = 120;
    private static final int logoHeight = 120;
    private static final int deltaWidth = WIDTH / 2 - logoWidth / 2;
    private static final int deltaHeight = HEIGHT / 2 - logoHeight / 2;
    private static final int frameSize = 8;
    private static final int coverOffset = 2;

    public Resource generate(String url) throws WriterException, IOException {
        final Map<EncodeHintType, Object> encodingHints = new HashMap<>();
        encodingHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCode code = Encoder.encode(url, ErrorCorrectionLevel.H, encodingHints);
        BufferedImage image = renderQRImage(code, WIDTH, HEIGHT, 4);
        return convertToResource(image);
    }

    @SneakyThrows
    private static BufferedImage renderQRImage(QRCode code, int width, int height, int quietZone) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, width, height);
        graphics.setColor(Color.black);

        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
        final int FINDER_PATTERN_SIZE = 7;
        final float CIRCLE_SCALE_DOWN_FACTOR = 21f / 30f;
        int circleSize = (int) (multiple * CIRCLE_SCALE_DOWN_FACTOR);

        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    if (!(inputX <= FINDER_PATTERN_SIZE && inputY <= FINDER_PATTERN_SIZE ||
                            inputX >= inputWidth - FINDER_PATTERN_SIZE && inputY <= FINDER_PATTERN_SIZE ||
                            inputX <= FINDER_PATTERN_SIZE && inputY >= inputHeight - FINDER_PATTERN_SIZE)) {
                        graphics.fillOval(outputX, outputY, circleSize, circleSize);
                    }
                }
            }
        }

        int circleDiameter = multiple * FINDER_PATTERN_SIZE;
        drawFinderPatternCircleStyle(graphics, leftPadding, topPadding, circleDiameter);
        drawFinderPatternCircleStyle(graphics, leftPadding + (inputWidth - FINDER_PATTERN_SIZE) * multiple, topPadding, circleDiameter);
        drawFinderPatternCircleStyle(graphics, leftPadding, topPadding + (inputHeight - FINDER_PATTERN_SIZE) * multiple, circleDiameter);

        return placeLogo(image);
    }

    private static BufferedImage placeLogo(BufferedImage image) throws IOException {

        BufferedImage logoImage = ImageIO.read(QRService.class.getResourceAsStream("/images/logo_ar_miro.png"));

        if (isNull(logoImage)) {
            return image;
        }

        final Graphics graphics = image.getGraphics();

        // place a white frame to cover center of QR
        graphics.setColor(Color.white);
        final int coverPositionX = deltaWidth - frameSize / 2 - coverOffset;
        final int coverPositionY = deltaHeight - frameSize / 2 - coverOffset;
        graphics.fillRect(coverPositionX, coverPositionY, logoWidth + frameSize, logoHeight + frameSize);

        // Initialize combined image
        BufferedImage combined = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) combined.getGraphics();

        // Write QR code to new image at position 0/0
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Write logo into combine image at center
        g.drawImage(logoImage, deltaWidth + frameSize / 2, deltaHeight + frameSize / 2, null);

        return combined;
    }

    private static void drawFinderPatternCircleStyle(Graphics2D graphics, int x, int y, int circleDiameter) {
        final int WHITE_CIRCLE_DIAMETER = circleDiameter * 5 / 7;
        final int WHITE_CIRCLE_OFFSET = circleDiameter / 7;
        final int MIDDLE_DOT_DIAMETER = circleDiameter * 3 / 7;
        final int MIDDLE_DOT_OFFSET = circleDiameter * 2 / 7;

        graphics.setColor(Color.black);
        graphics.fillOval(x, y, circleDiameter, circleDiameter);
        graphics.setColor(Color.white);
        graphics.fillOval(x + WHITE_CIRCLE_OFFSET, y + WHITE_CIRCLE_OFFSET, WHITE_CIRCLE_DIAMETER, WHITE_CIRCLE_DIAMETER);
        graphics.setColor(Color.black);
        graphics.fillOval(x + MIDDLE_DOT_OFFSET, y + MIDDLE_DOT_OFFSET, MIDDLE_DOT_DIAMETER, MIDDLE_DOT_DIAMETER);
    }
}
