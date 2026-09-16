public class Perro extends Animal {

    public Perro(String nombre) {
        super(nombre);
    }

    @Override
    public void hacerSonido() {
        System.out.println(nombre + " dice: Guau! Guau!");
    }

    @Override
    public void comer(String alimento) {
        if (alimento.toLowerCase().contains("hueso")) {
            System.out.println(nombre + " muerde el hueso con entusiasmo! CRACK!");
        } else {
            System.out.println(nombre + " come " + alimento + " como buen perrito.");
        }
    }

    public void buscar() {
        System.out.println(nombre + " busca la pelota con la cola moviendose.");
    }
}
