import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class LabM2_2 {

    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static void analisar(String input, String prefixoOutput, int connectivity, int minArea) {

        if (connectivity != 4 && connectivity != 8) {
            System.err.println("Erro: Parametro --connectivity deve ser 4 ou 8.");
            System.exit(1);
        }

        File arquivo = new File(input);
        if (!arquivo.exists()) {
            System.err.println("Erro: Mascara de entrada nao encontrada.");
            System.exit(1);
        }

        new File(prefixoOutput).getParentFile().mkdirs();


        Mat mascaraInicial = Imgcodecs.imread(input, Imgcodecs.IMREAD_GRAYSCALE);
        if (mascaraInicial.empty()) {
            System.err.println("Erro: Falha ao ler a mascara.");
            System.exit(1);
        }


        Mat labelsTemp = new Mat();
        int count4 = Imgproc.connectedComponents(mascaraInicial, labelsTemp, 4, CvType.CV_32S) - 1;
        int count8 = Imgproc.connectedComponents(mascaraInicial, labelsTemp, 8, CvType.CV_32S) - 1;


        System.out.println("Processando mascara inicial...");
        int countInicial = extrairEstatísticasCSV(mascaraInicial, prefixoOutput + "_initial.csv", connectivity, minArea);


        Mat elementoEstruturante = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));

        Mat maskAberta = new Mat();
        Mat maskFechada = new Mat();


        Imgproc.morphologyEx(mascaraInicial, maskAberta, Imgproc.MORPH_OPEN, elementoEstruturante);
        Imgcodecs.imwrite(prefixoOutput + "_morph1_open.png", maskAberta);


        Imgproc.morphologyEx(maskAberta, maskFechada, Imgproc.MORPH_CLOSE, elementoEstruturante);
        Imgcodecs.imwrite(prefixoOutput + "_morph2_close.png", maskFechada);


        System.out.println("Processando mascara final filtrada...");
        int[] finalCounts = extrairEstatísticasEColorir(maskFechada, prefixoOutput + "_final.csv", prefixoOutput + "_labels.png", connectivity, minArea);

        int contagemBruta = finalCounts[0];
        int contagemFiltrada = finalCounts[1];
        long areaTotal = Core.countNonZero(maskFechada);


        gerarResumoJSON(prefixoOutput + "_summary.json", connectivity, minArea, count4, count8, contagemBruta, contagemFiltrada, areaTotal);

        System.out.println("Analise concluida! Arquivos gerados com o prefixo: " + prefixoOutput);
    }



    private static int extrairEstatísticasCSV(Mat mascara, String caminhoCsv, int connectivity, int minArea) {
        Mat labels = new Mat(), stats = new Mat(), centroids = new Mat();
        int numLabels = Imgproc.connectedComponentsWithStats(mascara, labels, stats, centroids, connectivity, CvType.CV_32S);

        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoCsv))) {
            writer.println("label,area,x,y,width,height,cx,cy,kept");

            for (int i = 1; i < numLabels; i++) { // Pula o 0 (fundo)
                int area = (int) stats.get(i, Imgproc.CC_STAT_AREA)[0];
                int x = (int) stats.get(i, Imgproc.CC_STAT_LEFT)[0];
                int y = (int) stats.get(i, Imgproc.CC_STAT_TOP)[0];
                int w = (int) stats.get(i, Imgproc.CC_STAT_WIDTH)[0];
                int h = (int) stats.get(i, Imgproc.CC_STAT_HEIGHT)[0];
                double cx = centroids.get(i, 0)[0];
                double cy = centroids.get(i, 1)[0];
                boolean kept = area >= minArea;

                writer.printf("%d,%d,%d,%d,%d,%d,%.2f,%.2f,%b\n", i, area, x, y, w, h, cx, cy, kept);
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar CSV: " + e.getMessage());
        }
        return numLabels - 1;
    }

    private static int[] extrairEstatísticasEColorir(Mat mascara, String caminhoCsv, String caminhoImg, int connectivity, int minArea) {
        Mat labels = new Mat(), stats = new Mat(), centroids = new Mat();
        int numLabels = Imgproc.connectedComponentsWithStats(mascara, labels, stats, centroids, connectivity, CvType.CV_32S);

        int contagemBruta = numLabels - 1;
        int contagemFiltrada = 0;

        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoCsv))) {
            writer.println("label,area,x,y,width,height,cx,cy,kept");

            for (int i = 1; i < numLabels; i++) {
                int area = (int) stats.get(i, Imgproc.CC_STAT_AREA)[0];
                boolean kept = area >= minArea;
                if (kept) contagemFiltrada++;

                writer.printf("%d,%d,%d,%d,%d,%d,%.2f,%.2f,%b\n",
                        i, area,
                        (int) stats.get(i, Imgproc.CC_STAT_LEFT)[0], (int) stats.get(i, Imgproc.CC_STAT_TOP)[0],
                        (int) stats.get(i, Imgproc.CC_STAT_WIDTH)[0], (int) stats.get(i, Imgproc.CC_STAT_HEIGHT)[0],
                        centroids.get(i, 0)[0], centroids.get(i, 1)[0], kept);
            }
        } catch (Exception e) {}

        Mat labels8u = new Mat();
        Core.normalize(labels, labels8u, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
        Mat colorida = new Mat();
        Imgproc.applyColorMap(labels8u, colorida, Imgproc.COLORMAP_JET);


        Mat mascaraBinaria = new Mat();
        Core.compare(labels, new Scalar(0), mascaraBinaria, Core.CMP_EQ);
        colorida.setTo(new Scalar(0, 0, 0), mascaraBinaria);

        Imgcodecs.imwrite(caminhoImg, colorida);

        return new int[]{contagemBruta, contagemFiltrada};
    }

    private static void gerarResumoJSON(String caminho, int conn, int minArea, int c4, int c8, int bruta, int filtrada, long areaTotal) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            writer.println("{");
            writer.println("  \"parametros\": {");
            writer.println("    \"connectivity_utilizada\": " + conn + ",");
            writer.println("    \"min_area_filtro\": " + minArea + ",");
            writer.println("    \"morfologia\": \"Abertura (5x5) -> Fechamento (5x5)\"");
            writer.println("  },");
            writer.println("  \"mascara_inicial\": {");
            writer.println("    \"componentes_conectividade_4\": " + c4 + ",");
            writer.println("    \"componentes_conectividade_8\": " + c8);
            writer.println("  },");
            writer.println("  \"mascara_final\": {");
            writer.println("    \"area_total_pixels\": " + areaTotal + ",");
            writer.println("    \"contagem_bruta\": " + bruta + ",");
            writer.println("    \"contagem_apos_filtro\": " + filtrada);
            writer.println("  }");
            writer.println("}");
        } catch (Exception e) {
            System.err.println("Erro ao gerar JSON: " + e.getMessage());
        }
    }
}