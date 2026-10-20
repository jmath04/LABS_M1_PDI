public class Main {
    public static void main(String[] args) {
        String input = null;
        String output = null;
        String operation = null;
        Double value = null;
        String borda = "replicar";
        String filtro = "media3";

        // Parâmetros da M2 (Segmentação e Análise)
        String method = "fixed";
        Double threshold = 128.0;
        Integer connectivity = 8;
        Integer minArea = 0;

        // Analisador de argumentos CLI
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--input") && i + 1 < args.length) {
                input = args[i + 1];
            } else if (args[i].equals("--output") && i + 1 < args.length) {
                output = args[i + 1];
            } else if (args[i].equals("--operation") && i + 1 < args.length) {
                operation = args[i + 1];
            } else if (args[i].equals("--value") && i + 1 < args.length) {
                value = Double.parseDouble(args[i + 1]);
            } else if (args[i].equals("--borda") && i + 1 < args.length) {
                borda = args[i + 1];
            } else if (args[i].equals("--filtro") && i + 1 < args.length) {
                filtro = args[i + 1];
            } else if (args[i].equals("--method") && i + 1 < args.length) {
                method = args[i + 1];
            } else if (args[i].equals("--threshold") && i + 1 < args.length) {
                threshold = Double.parseDouble(args[i + 1]);
            } else if (args[i].equals("--connectivity") && i + 1 < args.length) {
                connectivity = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("--min-area") && i + 1 < args.length) {
                minArea = Integer.parseInt(args[i + 1]);
            }
        }

        // Validações de base
        if (operation == null) {
            System.err.println("Erro: Operacao (--operation) nao especificada.");
            System.exit(1);
        }
        if (input == null) {
            System.err.println("Erro: Imagem de entrada (--input) nao especificada.");
            System.exit(1);
        }

        // Configuração do caminho de saída padrão
        if (output == null && !operation.equals("inspect") && !operation.equals("histograma")) {
            output = "images/output/resultado_" + operation + ".jpg";

            // Regras específicas da M2 para os caminhos de saída
            if (operation.equals("segment")) {
                output = "images/output/resultado_segment.png";
            }
            if (operation.equals("analyze")) {
                output = "results/analise"; // Atua como um prefixo na M2.2
            }
        }

        // Encaminhamento M1
        if (operation.equals("inspect")) {
            LabM1_1.Inspect(input);
        } else if (operation.equals("copy")) {
            LabM1_1.copiar(input, output);
        } else if (operation.equals("brilho")) {
            if (value == null) { System.err.println("Erro: --value necessario para brilho."); System.exit(1); }
            LabM1_2.ajustarBrilho(input, output, value);
        } else if (operation.equals("negativo")) {
            LabM1_2.aplicarNegativo(input, output);
        } else if (operation.equals("contraste")) {
            if (value == null) { System.err.println("Erro: --value necessario para contraste."); System.exit(1); }
            LabM1_2.ajustarContraste(input, output, value);
        } else if (operation.equals("limiarizacao")) {
            if (value == null) { System.err.println("Erro: --value (limiar) necessario."); System.exit(1); }
            LabM1_2.aplicarLimiarizacao(input, output, value);
        } else if (operation.equals("histograma")) {
            LabM1_2.gerarHistograma(input);
        } else if (operation.equals("suavizacao")) {
            LabM1_3.aplicarSuavizacao(input, output, filtro, borda);
        } else if (operation.equals("laplaciano")) {
            LabM1_3.aplicarLaplaciano(input, output, borda);
        } else if (operation.equals("sobel")) {
            LabM1_3.aplicarSobel(input, output, borda);

            // Encaminhamento M2
        } else if (operation.equals("segment")) {
            LabM2_1.segmentar(input, output, method, threshold);
        } else if (operation.equals("analyze")) {
            LabM2_2.analisar(input, output, connectivity, minArea);

            // Operação desconhecida
        } else {
            System.err.println("Erro: Operacao '" + operation + "' nao suportada.");
            System.exit(1);
        }
    }
}