import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.File;

public class LabM2_1 {

    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static void segmentar(String input, String output, String method, Double threshold) {

        if (!output.toLowerCase().endsWith(".png")) {
            System.err.println("Erro: A saida da mascara deve ser em formato .png (exigencia do M2)");
            System.exit(1);
        }

        File arquivoEntrada = new File(input);
        if (!arquivoEntrada.exists()) {
            System.err.println("Erro: Arquivo de entrada nao encontrado.");
            System.exit(1);
        }


        Mat img = Imgcodecs.imread(input, Imgcodecs.IMREAD_GRAYSCALE);
        if (img.empty()) {
            System.err.println("Erro: Falha ao ler a imagem ou formato incompativel.");
            System.exit(1);
        }

        Mat mask = new Mat();


        if (method.equals("fixed")) {
            Imgproc.threshold(img, mask, threshold, 255, Imgproc.THRESH_BINARY);
        } else if (method.equals("otsu")) {
            Imgproc.threshold(img, mask, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        } else if (method.equals("adaptive")) {
            Imgproc.adaptiveThreshold(img, mask, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2);
        } else {
            System.err.println("Erro: Metodo invalido. Use 'fixed', 'otsu' ou 'adaptive'.");
            System.exit(1);
        }

        int totalPixels = mask.rows() * mask.cols();
        int foregroundPixels = Core.countNonZero(mask);
        double proporcao = ((double) foregroundPixels / totalPixels) * 100.0;

        System.out.println("=== Resultados da Segmentacao ===");
        System.out.println("Metodo escolhido: " + method);
        System.out.println("Pixels de objeto (255): " + foregroundPixels);
        System.out.println("Pixels de fundo (0): " + (totalPixels - foregroundPixels));
        System.out.println("Proporcao do objeto: " + String.format("%.2f", proporcao) + "%");

        Imgcodecs.imwrite(output, mask);
        System.out.println("Mascara salva com sucesso em: " + output);
    }
}