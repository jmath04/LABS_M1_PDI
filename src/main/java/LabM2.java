import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class LabM2 {

    private static int clamp(int valor) {
        if (valor > 255) return 255;
        if (valor < 0) return 0;
        return valor;
    }

    public static void ajustarBrilho(String input, String output, Double b) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            int largura = imgOrig.getWidth();
            int altura = imgOrig.getHeight();
            BufferedImage imgSaida = new BufferedImage(largura, altura, BufferedImage.TYPE_BYTE_GRAY);

            for (int i = 0; i < altura; i++) {
                for (int j = 0; j < largura; j++) {
                    int pixel = imgOrig.getRGB(j, i);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b_canal = pixel & 0xff;

                    int cinza = (r + g + b_canal) / 3;

                    int novoValor = clamp((int) (cinza + b));

                    int novoPixel = (255 << 24) | (novoValor << 16) | (novoValor << 8) | novoValor;
                    imgSaida.setRGB(j, i, novoPixel);
                }
            }
            ImageIO.write(imgSaida, "jpg", new File(output));
            System.out.println("Brilho ajustado com sucesso: " + output);
        } catch (Exception e) {
            System.out.println("Erro ao processar brilho: " + e.getMessage());
        }
    }

    public static void aplicarNegativo(String input, String output) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            int largura = imgOrig.getWidth();
            int altura = imgOrig.getHeight();
            BufferedImage imgSaida = new BufferedImage(largura, altura, BufferedImage.TYPE_BYTE_GRAY);

            for (int i = 0; i < altura; i++) {
                for (int j = 0; j < largura; j++) {
                    int pixel = imgOrig.getRGB(j, i);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b_canal = pixel & 0xff;

                    int cinza = (r + g + b_canal) / 3;

                    int novoValor = clamp(255 - cinza);

                    int novoPixel = (255 << 24) | (novoValor << 16) | (novoValor << 8) | novoValor;
                    imgSaida.setRGB(j, i, novoPixel);
                }
            }
            ImageIO.write(imgSaida, "jpg", new File(output));
            System.out.println("Filtro negativo aplicado: " + output);
        } catch (Exception e) {
            System.out.println("Erro ao processar negativo: " + e.getMessage());
        }
    }

    public static void ajustarContraste(String input, String output, Double alpha) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            int largura = imgOrig.getWidth();
            int altura = imgOrig.getHeight();
            BufferedImage imgSaida = new BufferedImage(largura, altura, BufferedImage.TYPE_BYTE_GRAY);

            for (int i = 0; i < altura; i++) {
                for (int j = 0; j < largura; j++) {
                    int pixel = imgOrig.getRGB(j, i);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b_canal = pixel & 0xff;

                    int cinza = (r + g + b_canal) / 3;

                    int novoValor = clamp((int) (alpha * (cinza - 128) + 128));

                    int novoPixel = (255 << 24) | (novoValor << 16) | (novoValor << 8) | novoValor;
                    imgSaida.setRGB(j, i, novoPixel);
                }
            }
            ImageIO.write(imgSaida, "jpg", new File(output));
            System.out.println("Contraste ajustado com sucesso: " + output);
        } catch (Exception e) {
            System.out.println("Erro ao processar contraste: " + e.getMessage());
        }
    }

    public static void aplicarLimiarizacao(String input, String output, Double tDouble) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            int largura = imgOrig.getWidth();
            int altura = imgOrig.getHeight();
            BufferedImage imgSaida = new BufferedImage(largura, altura, BufferedImage.TYPE_BYTE_GRAY);

            int limiar = tDouble.intValue();

            for (int i = 0; i < altura; i++) {
                for (int j = 0; j < largura; j++) {
                    int pixel = imgOrig.getRGB(j, i);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b_canal = pixel & 0xff;

                    int cinza = (r + g + b_canal) / 3;

                    int novoValor = (cinza < limiar) ? 0 : 255;

                    int novoPixel = (255 << 24) | (novoValor << 16) | (novoValor << 8) | novoValor;
                    imgSaida.setRGB(j, i, novoPixel);
                }
            }
            ImageIO.write(imgSaida, "jpg", new File(output));
            System.out.println("Limiarização aplicada com sucesso: " + output);
        } catch (Exception e) {
            System.out.println("Erro ao aplicar limiarização: " + e.getMessage());
        }
    }

    public static void gerarHistograma(String input) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            int largura = imgOrig.getWidth();
            int altura = imgOrig.getHeight();

                int[] histograma = new int[256];

            for (int i = 0; i < altura; i++) {
                for (int j = 0; j < largura; j++) {
                    int pixel = imgOrig.getRGB(j, i);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b_canal = pixel & 0xff;

                    int cinza = (r + g + b_canal) / 3;

                    // Contabiliza essa intensidade
                    histograma[cinza]++;
                }
            }

            String csvOutput = "images/output/histograma.csv";
            PrintWriter writer = new PrintWriter(new FileWriter(csvOutput));

            writer.println("intensidade,quantidade");

            for (int i = 0; i < 256; i++) {
                writer.println(i + "," + histograma[i]);
            }

            writer.close();
            System.out.println("Histograma salvo com sucesso em: " + csvOutput);

        } catch (Exception e) {
            System.out.println("Erro ao gerar histograma: " + e.getMessage());
        }
    }
}