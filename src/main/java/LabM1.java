import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class LabM1 {
    public static void Inspect(String img) {
        try {
            File arquivo = new File(img);
            BufferedImage imagem = ImageIO.read(arquivo);
            int largura = imagem.getWidth();
            int altura = imagem.getHeight();
            int numCanais = imagem.getSampleModel().getNumBands();
            int typeImg = imagem.getType();
            String RtypeImg = "Tipo desconhecido";

            if (typeImg == BufferedImage.TYPE_INT_RGB || typeImg == BufferedImage.TYPE_3BYTE_BGR) {
                RtypeImg = "RGB";
            } else if (typeImg == BufferedImage.TYPE_INT_ARGB || typeImg == BufferedImage.TYPE_4BYTE_ABGR) {
                RtypeImg = "ARGB";
            } else if (typeImg == BufferedImage.TYPE_BYTE_GRAY || typeImg == BufferedImage.TYPE_USHORT_GRAY) {
                RtypeImg = "GRAYSCALE";
            }

            int min = 255;
            int max = 0;
            long soma = 0;

            for (int i = 0; i < altura; i++) {
                for (int j = 0; j < largura; j++) {
                    int pixelBruto = imagem.getRGB(j, i);

                    int vermelho = (pixelBruto >> 16) & 0xff;
                    int verde    = (pixelBruto >> 8) & 0xff;
                    int azul     = pixelBruto & 0xff;

                    int intensidade = (vermelho + verde + azul) / 3;

                    if (intensidade < min) min = intensidade;
                    if (intensidade > max) max = intensidade;
                    soma += intensidade;
                }
            }

            long media = soma / (largura * altura);

            System.out.println("width=" + largura);
            System.out.println("height=" + altura);
            System.out.println("channels=" + numCanais);
            System.out.println("type=" + RtypeImg);
            System.out.println("pixels=" + (largura * altura));
            System.out.println("min=" + min);
            System.out.println("max=" + max);
            System.out.println("avg=" + media);

        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo");
        }
    }

    public static void copiar(String input, String output) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            int largura = imgOrig.getWidth();
            int altura = imgOrig.getHeight();

            BufferedImage imgSaida = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < altura; i++) {
                for (int j = 0; j < largura; j++) {
                    int pixel = imgOrig.getRGB(j, i);
                    imgSaida.setRGB(j, i, pixel);
                }
            }
            ImageIO.write(imgSaida, "jpg", new File(output));
            System.out.println("Imagem copiada com sucesso para: " + output);

        } catch (Exception e) {
            System.out.println("Erro ao processar a imagem: " + e.getMessage());
        }
    }
}