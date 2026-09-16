public class Gato extends Animal {

    public Gato(String nombre) {
        super(nombre);
    }

    @Override
    public void hacerSonido() {
        System.out.println(nombre + " dice: Miau~");
    }

    @Override
    public void comer(String alimento) {
        if (alimento.toLowerCase().contains("pescado")) {
            System.out.println(nombre + " come el pescado delicadamente. Glotoneria!");
        } else {
            System.out.println(nombre + " olfatea " + alimento + "... no le interesa.");
        }
    }

    public void ronronear() {
        System.out.println(nombre + " ronronea... purrrrrr...");
    }
}
