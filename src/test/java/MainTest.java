import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Path;

public class MainTest {

    @TempDir
    Path tempDir;

    private File criarImagemSintetica() throws Exception {
        File arquivoInput = tempDir.resolve("input_teste.jpg").toFile();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int val = (x + y) * 12; // Gradiente de intensidades
                int rgb = (255 << 24) | (val << 16) | (val << 8) | val;
                img.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(img, "jpg", arquivoInput);
        return arquivoInput;
    }

    private File criarMascaraSintetica() throws Exception {
        File arquivoInput = tempDir.resolve("mascara_teste.png").toFile();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                // Cria um quadrado branco no meio e fundo preto
                int val = (x >= 3 && x <= 6 && y >= 3 && y <= 6) ? 255 : 0;
                int rgb = (255 << 24) | (val << 16) | (val << 8) | val;
                img.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(img, "png", arquivoInput);
        return arquivoInput;
    }

    @Test
    public void testarInspect() throws Exception {
        File input = criarImagemSintetica();
        assertDoesNotThrow(() -> {
            LabM1_1.Inspect(input.getAbsolutePath());
        });
    }

    @Test
    public void testarCopiar() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_copia.jpg").toFile();
        LabM1_1.copiar(input.getAbsolutePath(), output.getAbsolutePath());
        assertTrue(output.exists(), "A imagem copiada deve ser gerada.");
    }



    @Test
    public void testarAjusteBrilho() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_brilho.jpg").toFile();
        LabM1_2.ajustarBrilho(input.getAbsolutePath(), output.getAbsolutePath(), 30.0);
        assertTrue(output.exists());
    }

    @Test
    public void testarNegativo() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_negativo.jpg").toFile();
        LabM1_2.aplicarNegativo(input.getAbsolutePath(), output.getAbsolutePath());
        assertTrue(output.exists());
    }

    @Test
    public void testarAjusteContraste() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_contraste.jpg").toFile();
        LabM1_2.ajustarContraste(input.getAbsolutePath(), output.getAbsolutePath(), 1.5);
        assertTrue(output.exists());
    }

    @Test
    public void testarLimiarizacao() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_limiar.jpg").toFile();
        LabM1_2.aplicarLimiarizacao(input.getAbsolutePath(), output.getAbsolutePath(), 128.0);
        assertTrue(output.exists());
    }

    @Test
    public void testarGerarHistograma() throws Exception {
        File input = criarImagemSintetica();
        assertDoesNotThrow(() -> {
            LabM1_2.gerarHistograma(input.getAbsolutePath());
        });
    }



    @Test
    public void testarSuavizacao() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_suave.jpg").toFile();
        LabM1_3.aplicarSuavizacao(input.getAbsolutePath(), output.getAbsolutePath(), "ponderada3", "replicar");
        assertTrue(output.exists());
    }

    @Test
    public void testarLaplaciano() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_laplaciano.jpg").toFile();
        LabM1_3.aplicarLaplaciano(input.getAbsolutePath(), output.getAbsolutePath(), "replicar");

        assertTrue(tempDir.resolve("saida_laplaciano_bruto.jpg").toFile().exists());
        assertTrue(tempDir.resolve("saida_laplaciano_realce.jpg").toFile().exists());
    }

    @Test
    public void testarSobel() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_sobel.jpg").toFile();
        LabM1_3.aplicarSobel(input.getAbsolutePath(), output.getAbsolutePath(), "replicar");

        assertTrue(tempDir.resolve("saida_sobel_Gx.jpg").toFile().exists());
        assertTrue(tempDir.resolve("saida_sobel_Gy.jpg").toFile().exists());
        assertTrue(tempDir.resolve("saida_sobel_Aprox.jpg").toFile().exists());
        assertTrue(tempDir.resolve("saida_sobel_Euclidiana.jpg").toFile().exists());
    }

    @Test
    public void testarSegmentacaoOpenCV() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_segmentada.png").toFile();

        assertDoesNotThrow(() -> {
            LabM2_1.segmentar(input.getAbsolutePath(), output.getAbsolutePath(), "fixed", 128.0);
        });

        assertTrue(output.exists(), "A mascara em formato PNG deve ser gerada pelo OpenCV.");
    }

    @Test
    public void testarAnaliseComponentes() throws Exception {
        File input = criarMascaraSintetica();
        String prefixo = tempDir.resolve("analise_teste").toAbsolutePath().toString();

        assertDoesNotThrow(() -> {
            LabM2_2.analisar(input.getAbsolutePath(), prefixo, 8, 0);
        });

        assertTrue(new File(prefixo + "_initial.csv").exists(), "CSV inicial deve ser gerado.");
        assertTrue(new File(prefixo + "_final.csv").exists(), "CSV final deve ser gerado.");
        assertTrue(new File(prefixo + "_summary.json").exists(), "JSON de resumo deve ser gerado.");
        assertTrue(new File(prefixo + "_labels.png").exists(), "Imagem de rotulos coloridos deve ser gerada.");
    }
}