public class Main {
    public static void main(String[] args) {
        String input = null;
        String output = null;
        String operation = null;
        Double value = null;
        String borda = "replicar"; // Padrão
        String filtro = "media3"; // Padrão

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

        if (operation == null) {
            System.err.println("Error: No operation specified");
            System.exit(1);
        }

        if (output == null && !operation.equals("inspect") && !operation.equals("histograma")) {
            output = "images/output/resultado_padrao.jpg";
        }

        // Roteamento M1.1, M1.2 e M1.3
        if (operation.equals("inspect")) LabM1.Inspect(input);
        else if (operation.equals("copy")) LabM1.copiar(input, output);
        else if (operation.equals("brilho")) LabM2.ajustarBrilho(input, output, value);
        else if (operation.equals("negativo")) LabM2.aplicarNegativo(input, output);
        else if (operation.equals("contraste")) LabM2.ajustarContraste(input, output, value);
        else if (operation.equals("limiarizacao")) LabM2.aplicarLimiarizacao(input, output, value);
        else if (operation.equals("histograma")) LabM2.gerarHistograma(input);
        else if (operation.equals("suavizacao")) LabM3.aplicarSuavizacao(input, output, filtro, borda);
        else if (operation.equals("laplaciano")) LabM3.aplicarLaplaciano(input, output, borda);
        else if (operation.equals("sobel")) LabM3.aplicarSobel(input, output, borda);
        else {
            System.err.println("Error: Operation not supported");
            System.exit(1);
        }
    }
}