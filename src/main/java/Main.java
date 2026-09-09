public class Main {
    public static void main(String[] args) {
        String input = null;
        String output = null;
        String operation = null;
        Double value = null;
        String borda = "replicar";
        String filtro = "media3";

        // Parseador de argumentos CLI
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
            }
        }

        // Validações Base
        if (operation == null) {
            System.err.println("Erro: Operacao (--operation) nao especificada.");
            System.exit(1);
        }
        if (input == null) {
            System.err.println("Erro: Imagem de entrada (--input) nao especificada.");
            System.exit(1);
        }

        // Output padrão (se não for passado, cria um na pasta de output)
        if (output == null && !operation.equals("inspect") && !operation.equals("histograma")) {
            output = "images/output/resultado_" + operation + ".jpg";
        }

        // Roteamento
        if (operation.equals("inspect")) {
            LabM1.Inspect(input);
        } else if (operation.equals("copy")) {
            LabM1.copiar(input, output);
        } else if (operation.equals("brilho")) {
            if (value == null) { System.err.println("Erro: --value necessario para brilho."); System.exit(1); }
            LabM2.ajustarBrilho(input, output, value);
        } else if (operation.equals("negativo")) {
            LabM2.aplicarNegativo(input, output);
        } else if (operation.equals("contraste")) {
            if (value == null) { System.err.println("Erro: --value necessario para contraste."); System.exit(1); }
            LabM2.ajustarContraste(input, output, value);
        } else if (operation.equals("limiarizacao")) {
            if (value == null) { System.err.println("Erro: --value (limiar) necessario."); System.exit(1); }
            LabM2.aplicarLimiarizacao(input, output, value);
        } else if (operation.equals("histograma")) {
            LabM2.gerarHistograma(input);
        } else if (operation.equals("suavizacao")) {
            LabM3.aplicarSuavizacao(input, output, filtro, borda);
        } else if (operation.equals("laplaciano")) {
            LabM3.aplicarLaplaciano(input, output, borda);
        } else if (operation.equals("sobel")) {
            LabM3.aplicarSobel(input, output, borda);
        } else {
            System.err.println("Erro: Operacao '" + operation + "' nao suportada.");
            System.exit(1);
        }
    }
}