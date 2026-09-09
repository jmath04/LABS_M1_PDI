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

    // Método auxiliar para criar uma imagem sintética pequena (10x10 pixels) para os testes
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

    // --- TESTES DO M1.1 (Inspeção e Cópia) ---

    @Test
    public void testarInspect() throws Exception {
        File input = criarImagemSintetica();

        // Garante que a inspeção roda sem lançar exceções e lê a imagem sintética
        assertDoesNotThrow(() -> {
            LabM1.Inspect(input.getAbsolutePath());
        });
    }

    @Test
    public void testarCopiar() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_copia.jpg").toFile();

        LabM1.copiar(input.getAbsolutePath(), output.getAbsolutePath());

        assertTrue(output.exists(), "A imagem copiada deve ser gerada.");
        assertTrue(output.length() > 0, "O arquivo copiado não pode estar vazio.");
    }

    // --- TESTES DO M1.2 (Transformações Pontuais) ---

    @Test
    public void testarAjusteBrilho() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_brilho.jpg").toFile();

        LabM2.ajustarBrilho(input.getAbsolutePath(), output.getAbsolutePath(), 30.0);

        assertTrue(output.exists(), "A imagem com brilho ajustado deve ser gerada.");
    }

    @Test
    public void testarNegativo() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_negativo.jpg").toFile();

        LabM2.aplicarNegativo(input.getAbsolutePath(), output.getAbsolutePath());

        assertTrue(output.exists(), "A imagem com filtro negativo deve ser gerada.");
    }

    @Test
    public void testarAjusteContraste() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_contraste.jpg").toFile();

        LabM2.ajustarContraste(input.getAbsolutePath(), output.getAbsolutePath(), 1.5);

        assertTrue(output.exists(), "A imagem com contraste ajustado deve ser gerada.");
    }

    @Test
    public void testarLimiarizacao() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_limiar.jpg").toFile();

        LabM2.aplicarLimiarizacao(input.getAbsolutePath(), output.getAbsolutePath(), 128.0);

        assertTrue(output.exists(), "A imagem limiarizada deve ser gerada.");
    }

    @Test
    public void testarGerarHistograma() throws Exception {
        File input = criarImagemSintetica();

        assertDoesNotThrow(() -> {
            LabM2.gerarHistograma(input.getAbsolutePath());
        });
    }

    // --- TESTES DO M1.3 (Convolução e Filtros Espaciais) ---

    @Test
    public void testarSuavizacao() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_suave.jpg").toFile();

        LabM3.aplicarSuavizacao(input.getAbsolutePath(), output.getAbsolutePath(), "ponderada3", "replicar");

        assertTrue(output.exists(), "A imagem suavizada deve ser gerada.");
    }

    @Test
    public void testarLaplaciano() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_laplaciano.jpg").toFile();

        LabM3.aplicarLaplaciano(input.getAbsolutePath(), output.getAbsolutePath(), "replicar");

        File outBruto = tempDir.resolve("saida_laplaciano_bruto.jpg").toFile();
        File outRealce = tempDir.resolve("saida_laplaciano_realce.jpg").toFile();

        assertTrue(outBruto.exists(), "A versão bruta do Laplaciano deve ser gerada.");
        assertTrue(outRealce.exists(), "A versão realçada do Laplaciano deve ser gerada.");
    }

    @Test
    public void testarSobel() throws Exception {
        File input = criarImagemSintetica();
        File output = tempDir.resolve("saida_sobel.jpg").toFile();

        LabM3.aplicarSobel(input.getAbsolutePath(), output.getAbsolutePath(), "replicar");

        File outGx = tempDir.resolve("saida_sobel_Gx.jpg").toFile();
        File outGy = tempDir.resolve("saida_sobel_Gy.jpg").toFile();
        File outAprox = tempDir.resolve("saida_sobel_Aprox.jpg").toFile();
        File outEuclid = tempDir.resolve("saida_sobel_Euclidiana.jpg").toFile();

        assertTrue(outGx.exists(), "Gradiente Gx do Sobel gerado.");
        assertTrue(outGy.exists(), "Gradiente Gy do Sobel gerado.");
        assertTrue(outAprox.exists(), "Magnitude aproximada gerada.");
        assertTrue(outEuclid.exists(), "Magnitude euclidiana gerada.");
    }
}