import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class LabM3 {

    // Guarda de saturação final
    private static int clamp(int valor) {
        if (valor > 255) return 255;
        if (valor < 0) return 0;
        return valor;
    }

    // Extrai os tons de cinza da imagem para uma matriz double
    private static double[][] getMatrizCinza(BufferedImage img) {
        int largura = img.getWidth();
        int altura = img.getHeight();
        double[][] matriz = new double[altura][largura];

        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                int pixel = img.getRGB(j, i);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                matriz[i][j] = (r + g + b) / 3.0;
            }
        }
        return matriz;
    }

    // Converte a matriz de volta para Imagem aplicando saturação
    private static BufferedImage criarImagem(double[][] matriz) {
        int altura = matriz.length;
        int largura = matriz[0].length;
        BufferedImage imgSaida = new BufferedImage(largura, altura, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                int cinza = clamp((int) Math.round(matriz[i][j]));
                int rgb = (255 << 24) | (cinza << 16) | (cinza << 8) | cinza;
                imgSaida.setRGB(j, i, rgb);
            }
        }
        return imgSaida;
    }

    // --- Atividade 1 e 2: Convolução Genérica e Bordas ---
    public static double[][] convolucao(double[][] img, double[][] kernel, String borda) {
        int altura = img.length;
        int largura = img[0].length;
        int kSize = kernel.length;

        // Guardas
        if (kSize % 2 == 0 || kSize != kernel[0].length) {
            throw new IllegalArgumentException("O kernel deve ser quadrado e ter dimensão ímpar.");
        }

        double[][] resultado = new double[altura][largura];
        int offset = kSize / 2;

        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {

                // Estratégia: Copiar (Ignora convolução nas bordas e copia original)
                if (borda.equals("copiar") && (i < offset || i >= altura - offset || j < offset || j >= largura - offset)) {
                    resultado[i][j] = img[i][j];
                    continue;
                }

                double soma = 0.0;
                for (int ki = 0; ki < kSize; ki++) {
                    for (int kj = 0; kj < kSize; kj++) {
                        int yImg = i + ki - offset;
                        int xImg = j + kj - offset;

                        // Estratégia: Replicar
                        if (borda.equals("replicar")) {
                            yImg = Math.max(0, Math.min(yImg, altura - 1));
                            xImg = Math.max(0, Math.min(xImg, largura - 1));
                        }

                        soma += img[yImg][xImg] * kernel[ki][kj];
                    }
                }
                resultado[i][j] = soma;
            }
        }
        return resultado;
    }

    // --- Atividade 3: Suavização ---
    public static void aplicarSuavizacao(String input, String output, String tipoFiltro, String borda) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            double[][] matriz = getMatrizCinza(imgOrig);
            double[][] kernel = null;

            if (tipoFiltro.equals("media3")) {
                kernel = new double[][] {
                        {1/9.0, 1/9.0, 1/9.0},
                        {1/9.0, 1/9.0, 1/9.0},
                        {1/9.0, 1/9.0, 1/9.0}
                };
            } else if (tipoFiltro.equals("ponderada3")) {
                kernel = new double[][] {
                        {1/16.0, 2/16.0, 1/16.0},
                        {2/16.0, 4/16.0, 2/16.0},
                        {1/16.0, 2/16.0, 1/16.0}
                };
            } else if (tipoFiltro.equals("media5")) {
                kernel = new double[5][5];
                for (int i=0; i<5; i++) for (int j=0; j<5; j++) kernel[i][j] = 1/25.0;
            }

            double[][] resultado = convolucao(matriz, kernel, borda);
            ImageIO.write(criarImagem(resultado), "jpg", new File(output));
            System.out.println("Suavização aplicada: " + output);

        } catch (Exception e) {
            System.out.println("Erro na suavização: " + e.getMessage());
        }
    }

    // --- Atividade 4: Laplaciano e Realce ---
    public static void aplicarLaplaciano(String input, String output, String borda) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            double[][] matriz = getMatrizCinza(imgOrig);

            double[][] kernelLap = {
                    {0, -1, 0},
                    {-1, 4, -1},
                    {0, -1, 0}
            };

            double[][] laplaciano = convolucao(matriz, kernelLap, borda);
            double[][] realce = new double[matriz.length][matriz[0].length];

            // Resposta bruta pode ser negativa, convertemos para absoluta na visualização
            double[][] laplacianoAbs = new double[matriz.length][matriz[0].length];

            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    laplacianoAbs[i][j] = Math.abs(laplaciano[i][j]); // Visualização bruta
                    realce[i][j] = matriz[i][j] + laplaciano[i][j];   // Imagem + Bordas
                }
            }

            String outAbs = output.replace(".jpg", "_bruto.jpg");
            String outRealce = output.replace(".jpg", "_realce.jpg");

            ImageIO.write(criarImagem(laplacianoAbs), "jpg", new File(outAbs));
            ImageIO.write(criarImagem(realce), "jpg", new File(outRealce));
            System.out.println("Laplaciano gerado: " + outAbs + " e " + outRealce);

        } catch (Exception e) {
            System.out.println("Erro no Laplaciano: " + e.getMessage());
        }
    }

    // --- Atividade 5: Sobel ---
    public static void aplicarSobel(String input, String output, String borda) {
        try {
            BufferedImage imgOrig = ImageIO.read(new File(input));
            double[][] matriz = getMatrizCinza(imgOrig);

            double[][] kernelGx = {
                    {-1, 0, 1},
                    {-2, 0, 2},
                    {-1, 0, 1}
            };

            double[][] kernelGy = {
                    {-1, -2, -1},
                    {0,  0,  0},
                    {1,  2,  1}
            };

            double[][] Gx = convolucao(matriz, kernelGx, borda);
            double[][] Gy = convolucao(matriz, kernelGy, borda);

            double[][] magAprox = new double[matriz.length][matriz[0].length];
            double[][] magEuclid = new double[matriz.length][matriz[0].length];

            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    magAprox[i][j] = Math.abs(Gx[i][j]) + Math.abs(Gy[i][j]);
                    magEuclid[i][j] = Math.sqrt((Gx[i][j] * Gx[i][j]) + (Gy[i][j] * Gy[i][j]));
                }
            }

            // Para extrair imagens de Gx e Gy sozinhas, usamos valor absoluto
            for(int i = 0; i < matriz.length; i++) {
                for(int j = 0; j < matriz[0].length; j++) {
                    Gx[i][j] = Math.abs(Gx[i][j]);
                    Gy[i][j] = Math.abs(Gy[i][j]);
                }
            }

            ImageIO.write(criarImagem(Gx), "jpg", new File(output.replace(".jpg", "_Gx.jpg")));
            ImageIO.write(criarImagem(Gy), "jpg", new File(output.replace(".jpg", "_Gy.jpg")));
            ImageIO.write(criarImagem(magAprox), "jpg", new File(output.replace(".jpg", "_Aprox.jpg")));
            ImageIO.write(criarImagem(magEuclid), "jpg", new File(output.replace(".jpg", "_Euclidiana.jpg")));

            System.out.println("Filtros Sobel gerados com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro no Sobel: " + e.getMessage());
        }
    }
}