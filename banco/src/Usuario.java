import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombre;
    private String apellido;
    private String telefono;
    private String pais;
    private String contrasena;
    private String numeroCuenta;
    private double saldo;
    private List<String> historial;

    public Usuario(String nombre, String apellido, String telefono, String pais, String contrasena, String numeroCuenta) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.pais = pais;
        this.contrasena = contrasena;
        this.numeroCuenta = numeroCuenta;
        this.saldo = 0.00;
        this.historial = new ArrayList<>();
    }

    public Usuario(String nombre, String apellido, String telefono, String pais, String contrasena,
                   String numeroCuenta, double saldo, List<String> historial) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.pais = pais;
        this.contrasena = contrasena;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.historial = historial != null ? historial : new ArrayList<>();
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getPais() { return pais; }
    public String getContrasena() { return contrasena; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public double getSaldo() { return saldo; }
    public List<String> getHistorial() { return historial; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setPais(String pais) { this.pais = pais; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public void agregarMovimiento(String movimiento) {
        this.historial.add(movimiento);
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(escape(nombre)).append(";")
          .append(escape(apellido)).append(";")
          .append(escape(telefono)).append(";")
          .append(escape(pais)).append(";")
          .append(escape(contrasena)).append(";")
          .append(escape(numeroCuenta)).append(";")
          .append(saldo).append(";");

        for (int i = 0; i < historial.size(); i++) {
            sb.append(escape(historial.get(i)));
            if (i < historial.size() - 1) {
                sb.append("#");
            }
        }

        return sb.toString();
    }

    public static Usuario fromFileString(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;

        String[] partes = linea.split(";", -1);
        if (partes.length < 8) return null;

        String nombre = unescape(partes[0]);
        String apellido = unescape(partes[1]);
        String telefono = unescape(partes[2]);
        String pais = unescape(partes[3]);
        String contrasena = unescape(partes[4]);
        String numeroCuenta = unescape(partes[5]);
        double saldo = 0;
        try {
            saldo = Double.parseDouble(partes[6]);
        } catch (NumberFormatException e) {
            saldo = 0.00;
        }

        List<String> historial = new ArrayList<>();
        if (partes.length > 7 && !partes[7].trim().isEmpty()) {
            String[] movimientos = partes[7].split("#", -1);
            for (String mov : movimientos) {
                if (!mov.trim().isEmpty()) {
                    historial.add(unescape(mov));
                }
            }
        }

        return new Usuario(nombre, apellido, telefono, pais, contrasena, numeroCuenta, saldo, historial);
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("#", "\\#")
                .replace("\n", "\\n");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n")
                .replace("\\#", "#")
                .replace("\\;", ";")
                .replace("\\\\", "\\");
    }

    @Override
    public String toString() {
        return String.format("Titular: %s %s | Cuenta: %s | Saldo: $%.2f", nombre, apellido, numeroCuenta, saldo);
    }
}
