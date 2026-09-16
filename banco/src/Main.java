import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Banco banco;

    public static void main(String[] args) {
        String rutaDatos = System.getProperty("user.dir") + "/datos/usuarios.txt";
        banco = new Banco(rutaDatos);

        System.out.println("========================================");
        System.out.println("   SISTEMA BANCARIO INTERACTIVO");
        System.out.println("========================================");

        boolean ejecutando = true;
        while (ejecutando) {
            ejecutando = menuPrincipal();
        }

        System.out.println("\nGracias por usar el Sistema Bancario. ¡Hasta luego!");
        scanner.close();
    }

    private static boolean menuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Registrar");
        System.out.println("2. Ingresar");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opcion: ");

        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                menuRegistro();
                return true;
            case 2:
                Usuario usuario = menuLogin();
                if (usuario != null) {
                    menuUsuario(usuario);
                }
                return true;
            case 3:
                return false;
            default:
                System.out.println("Opcion no valida.");
                return true;
        }
    }

    private static void menuRegistro() {
        System.out.println("\n--- REGISTRO DE USUARIO ---");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();

        System.out.print("Numero de telefono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Pais: ");
        String pais = scanner.nextLine().trim();

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || pais.isEmpty() || contrasena.isEmpty()) {
            System.out.println("\nError: Todos los campos son obligatorios.");
            return;
        }

        Usuario nuevo = banco.registrar(nombre, apellido, telefono, pais, contrasena);
        if (nuevo != null) {
            System.out.println("\n¡Registro exitoso!");
            System.out.println("Su numero de cuenta es: " + nuevo.getNumeroCuenta());
            System.out.println("Guarde este numero para iniciar sesion.");
        } else {
            System.out.println("\nError: Ya existe un usuario con ese nombre y apellido.");
        }
    }

    private static Usuario menuLogin() {
        System.out.println("\n--- INICIAR SESION ---");

        System.out.print("Nombre o Numero de cuenta: ");
        String entrada = scanner.nextLine().trim();

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine().trim();

        if (entrada.isEmpty() || contrasena.isEmpty()) {
            System.out.println("\nError: Complete todos los campos.");
            return null;
        }

        Usuario usuario = banco.login(entrada, contrasena);
        if (usuario != null) {
            System.out.println("\n¡Bienvenido/a, " + usuario.getNombre() + "!");
            return usuario;
        } else {
            System.out.println("\nError: Credenciales incorrectas.");
            return null;
        }
    }

    private static void menuUsuario(Usuario usuario) {
        boolean sesionActiva = true;
        while (sesionActiva) {
            System.out.println("\n--- MENU DE USUARIO ---");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Ingresar saldo (Deposito)");
            System.out.println("3. Transferencia");
            System.out.println("4. Historial de movimientos");
            System.out.println("5. Cerrar sesion");
            System.out.print("Seleccione una opcion: ");

            int opcion = leerEntero();
            switch (opcion) {
                case 1:
                    consultarSaldo(usuario);
                    break;
                case 2:
                    depositarSaldo(usuario);
                    break;
                case 3:
                    transferir(usuario);
                    break;
                case 4:
                    verHistorial(usuario);
                    break;
                case 5:
                    sesionActiva = false;
                    System.out.println("Sesion cerrada.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private static void consultarSaldo(Usuario usuario) {
        System.out.println("\n--- CONSULTA DE SALDO ---");
        System.out.println(usuario);
        System.out.println("Telefono: " + usuario.getTelefono());
        System.out.println("Pais: " + usuario.getPais());
    }

    private static void depositarSaldo(Usuario usuario) {
        System.out.println("\n--- DEPOSITO ---");
        System.out.print("Ingrese la cantidad a depositar: $");

        double cantidad = leerDouble();
        if (cantidad <= 0) {
            System.out.println("Error: La cantidad debe ser mayor a 0.");
            return;
        }

        banco.depositar(usuario, cantidad);
        System.out.printf("Deposito exitoso. Nuevo saldo: $%.2f%n", usuario.getSaldo());
    }

    private static void transferir(Usuario usuario) {
        System.out.println("\n--- TRANSFERENCIA ---");
        System.out.println("Saldo actual: " + usuario.getSaldo());
        System.out.print("Numero de cuenta destino: ");
        String cuentaDestino = scanner.nextLine().trim();

        if (cuentaDestino.isEmpty()) {
            System.out.println("Error: Ingrese un numero de cuenta.");
            return;
        }

        System.out.print("Cantidad a transferir: $");
        double cantidad = leerDouble();
        if (cantidad <= 0) {
            System.out.println("Error: La cantidad debe ser mayor a 0.");
            return;
        }

        boolean exito = banco.transferir(usuario, cuentaDestino, cantidad);
        if (exito) {
            System.out.println("Transferencia exitosa.");
            System.out.printf("Nuevo saldo: $%.2f%n", usuario.getSaldo());
        } else {
            System.out.println("Error: No se pudo realizar la transferencia.");
            System.out.println("Verifique el numero de cuenta y que tenga saldo suficiente.");
        }
    }

    private static void verHistorial(Usuario usuario) {
        System.out.println("\n--- HISTORIAL DE MOVIMIENTOS ---");
        if (usuario.getHistorial().isEmpty()) {
            System.out.println("No hay movimientos registrados.");
            return;
        }
        for (int i = usuario.getHistorial().size() - 1; i >= 0; i--) {
            String[] partes = usuario.getHistorial().get(i).split("\\|", -1);
            if (partes.length >= 3) {
                String tipo = partes[0];
                String monto = partes[1];
                String fecha = partes[2];
                String extra = partes.length > 3 ? " -> Cuenta: " + partes[3] : "";

                String tipoLegible;
                switch (tipo) {
                    case "DEPOSITO":
                        tipoLegible = "[DEPOSITO]   ";
                        break;
                    case "TRANSFERENCIA_SALIDA":
                        tipoLegible = "[ENVIO]      ";
                        break;
                    case "TRANSFERENCIA_ENTRADA":
                        tipoLegible = "[RECIBIDO]   ";
                        break;
                    default:
                        tipoLegible = "[" + tipo + "]";
                }
                System.out.printf("%s %s  %s%s%n", tipoLegible, monto, fecha, extra);
            }
        }
    }

    private static int leerEntero() {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private static double leerDouble() {
        try {
            double valor = scanner.nextDouble();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}
