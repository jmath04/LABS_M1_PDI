public class Main {
    public static void main(String[] args) {
        String input = null;
        String output = null;
        String operation = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--input") && i + 1 < args.length) {
                input = args[i + 1];
            } else if (args[i].equals("--output") && i + 1 < args.length) {
                output = args[i + 1];
            } else if (args[i].equals("--operation") && i + 1 < args.length) {
                operation = args[i + 1];
            }
        }

        if (operation == null) {
            System.err.println("Error: No operation specified");
            System.exit(1);
        }

        if (operation.equals("inspect")) {
            LabM1.Inspect(input);
        } else {
            System.err.println("Error: Operation not supported");
            System.exit(1);
        }
    }
}