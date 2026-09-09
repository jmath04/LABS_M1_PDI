public class Main {
    public static void main(String[] args) {
        String input = null;
        String output = null;
        String operation = null;
        Double value = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--input") && i + 1 < args.length) {
                input = args[i + 1];
            } else if (args[i].equals("--output") && i + 1 < args.length) {
                output = args[i + 1];
            } else if (args[i].equals("--operation") && i + 1 < args.length) {
                operation = args[i + 1];
            } else if (args[i].equals("--value") && i + 1 < args.length) {
                value = Double.parseDouble(args[i + 1]);
            }
        }

        if (operation == null) {
            System.err.println("Error: No operation specified");
            System.exit(1);
        }

        // Correção do output padrão para um nome de arquivo válido no diretório do projeto
        if (output == null && !operation.equals("inspect") && !operation.equals("histograma")) {
            output = "images/output/resultado_padrao.jpg";
        }

        // Roteamento completo M1.1 e M1.2
        if (operation.equals("inspect")) {
            LabM1.Inspect(input);
        } else if (operation.equals("copy")) {
            LabM1.copiar(input, output);
        } else if (operation.equals("brilho")) {
            LabM2.ajustarBrilho(input, output, value);
        } else if (operation.equals("negativo")) {
            LabM2.aplicarNegativo(input, output);
        } else if (operation.equals("contraste")) {
            // Nova rota: Atividade 2 da M1.2
            LabM2.ajustarContraste(input, output, value);
        } else if (operation.equals("limiarizacao")) {
            // Nova rota: Atividade 4 da M1.2
            LabM2.aplicarLimiarizacao(input, output, value);
        } else if (operation.equals("histograma")) {
            // Nova rota: Atividade 5 da M1.2 (Pode gerar o CSV com base no input)
            LabM2.gerarHistograma(input);
        } else {
            System.err.println("Error: Operation not supported");
            System.exit(1);
        }
    }
}