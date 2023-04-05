import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class App {
    public class ImageCompressor {

        public static void compressImage(String inputPath, String outputPath) throws IOException {
            File inputFile = new File(inputPath);
            BufferedImage image = ImageIO.read(inputFile);

            int maxSizeInBytes = 500000; // 500KB
            float quality = 0.8f;
            String formatName = "jpeg";
            int resolution = 72;
            int attempts = 0;

            while (true) {
                Dimension newSize = new Dimension(image.getWidth() * resolution / 72, image.getHeight() * resolution / 72);
                BufferedImage resizedImage = new BufferedImage(newSize.width, newSize.height, BufferedImage.TYPE_INT_RGB);
                resizedImage.createGraphics().drawImage(image, 0, 0, newSize.width, newSize.height, null);

                File tempFile = new File(outputPath + ".temp.jpg");
                ImageIO.write(resizedImage, formatName, tempFile);
                long dataSize = tempFile.length();

                if (dataSize <= maxSizeInBytes) {
                    tempFile.renameTo(new File(outputPath));
                    break;
                }

                quality -= 0.2f;
                resolution -= 10;
                if (quality <= 0 || resolution < 10) {
                    throw new IOException("can't compression");
                }
                attempts++;
                ImageIO.write(resizedImage, formatName, new File(outputPath + ".temp.jpg"));
                tempFile.delete();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        String inputPath = "./assets/input/input_4.jpg";
        String outputPath = "./assets/output/output_5.jpg";

        ImageCompressor.compressImage(inputPath, outputPath);
    }
}
