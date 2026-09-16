public class Animal {
    protected String nombre;

    public Animal(String nombre) {
        this.nombre = nombre;
    }

    public void hacerSonido() {
        System.out.println(nombre + " hace un sonido generico.");
    }

    public void hacerSonido(String volumen) {
        System.out.println(nombre + " hace un sonido " + volumen + ".");
    }

    public void hacerSonido(String volumen, int veces) {
        for (int i = 0; i < veces; i++) {
            System.out.println(nombre + " hace un sonido " + volumen + "! (" + (i + 1) + "/" + veces + ")");
        }
    }

    public void comer(String alimento) {
        System.out.println(nombre + " come " + alimento + " de forma generica.");
    }
}
