import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LabM1 {
    public static void Inspect() {
        try {
            File arquivo = new File("images/input/Kurama.jpg");
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

            System.out.println(largura);
            System.out.println(altura);
            System.out.println(numCanais);
            System.out.println(RtypeImg);
            System.out.println(largura * altura);
            System.out.println(imagem.getRGB(0, 0));

        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo");
        }
    }
}

    /*
    public int retornaValMin(BufferedImage imagem){
        int menor_pixel = 256;
        for (int i = 0; i < imagem.getHeight(); i++) {
            for (int j = 0; j < imagem.getWidth(); j++) {
                if (imagem.getRGB(j, i) > menor_pixel) {
                    menor_pixel = imagem.getRGB(j, i);
                }
            }
        }


    }
}
     */

