import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Banco {
    private List<Usuario> usuarios;
    private final String ARCHIVO_DATOS;

    public Banco(String rutaArchivo) {
        this.usuarios = new ArrayList<>();
        this.ARCHIVO_DATOS = rutaArchivo;
        cargarDatos();
    }

    private String generarNumeroCuenta() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        String cuenta = sb.toString();
        for (Usuario u : usuarios) {
            if (u.getNumeroCuenta().equals(cuenta)) {
                return generarNumeroCuenta();
            }
        }
        return cuenta;
    }

    public Usuario registrar(String nombre, String apellido, String telefono, String pais, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.getNombre().equalsIgnoreCase(nombre) && u.getApellido().equalsIgnoreCase(apellido)) {
                return null;
            }
        }
        String numeroCuenta = generarNumeroCuenta();
        Usuario nuevo = new Usuario(nombre, apellido, telefono, pais, contrasena, numeroCuenta);
        usuarios.add(nuevo);
        guardarDatos();
        return nuevo;
    }

    public Usuario login(String nombreOCuenta, String contrasena) {
        for (Usuario u : usuarios) {
            boolean coincideNombre = u.getNombre().equalsIgnoreCase(nombreOCuenta);
            boolean coincideCuenta = u.getNumeroCuenta().equals(nombreOCuenta);
            if ((coincideNombre || coincideCuenta) && u.getContrasena().equals(contrasena)) {
                return u;
            }
        }
        return null;
    }

    public void depositar(Usuario usuario, double cantidad) {
        if (cantidad <= 0) return;
        usuario.setSaldo(usuario.getSaldo() + cantidad);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        usuario.agregarMovimiento("DEPOSITO|+" + String.format("%.2f", cantidad) + "|" + fecha);
        guardarDatos();
    }

    public boolean transferir(Usuario emisor, String numeroCuentaDestino, double cantidad) {
        if (cantidad <= 0 || emisor.getSaldo() < cantidad) return false;

        Usuario receptor = null;
        for (Usuario u : usuarios) {
            if (u.getNumeroCuenta().equals(numeroCuentaDestino)) {
                receptor = u;
                break;
            }
        }
        if (receptor == null || receptor.getNumeroCuenta().equals(emisor.getNumeroCuenta())) return false;

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        emisor.setSaldo(emisor.getSaldo() - cantidad);
        receptor.setSaldo(receptor.getSaldo() + cantidad);

        emisor.agregarMovimiento("TRANSFERENCIA_SALIDA|-" + String.format("%.2f", cantidad) + "|" + fecha + "|" + receptor.getNumeroCuenta());
        receptor.agregarMovimiento("TRANSFERENCIA_ENTRADA|+" + String.format("%.2f", cantidad) + "|" + fecha + "|" + emisor.getNumeroCuenta());

        guardarDatos();
        return true;
    }

    public void guardarDatos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (Usuario u : usuarios) {
                writer.write(u.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_DATOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Usuario u = Usuario.fromFileString(linea);
                if (u != null) {
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
        }
    }
}
