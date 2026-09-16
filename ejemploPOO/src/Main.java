public class Main {
    public static void main(String[] args) {

        System.out.println("============================================");
        System.out.println(" SOBRECARGA vs SOBRESCRITURA DE METODOS");
        System.out.println("============================================");

        System.out.println("\n--- SECCION 1: SOBRECARGA ---");

        Animal animal = new Animal("Animal generico");

        animal.hacerSonido();
        animal.hacerSonido("fuerte");
        animal.hacerSonido("suave", 3);

        System.out.println("\n--- SECCION 2: SOBRESCRITURA ---");

        Perro firulais = new Perro("Firulais");
        Gato michi = new Gato("Michi");

        Animal[] animales = {firulais, michi};

        for (Animal a : animales) {
            System.out.println("\n" + a.getClass().getSimpleName() + ":");
            a.hacerSonido();
            a.comer("croquetas");
        }

        System.out.println("\n--- SECCION 3: AMBOS EN ACCION ---");

        Perro toby = new Perro("Toby");

        toby.hacerSonido();
        toby.hacerSonido("muy fuerte");
        toby.hacerSonido("agresivo", 2);

        toby.comer("un hueso");

        toby.buscar();
        michi.ronronear();

        System.out.println("\n============================================");
        System.out.println(" SOBRECARGA: mismo nombre, diferentes parametros");
        System.out.println(" SOBRESCRITURA: clase hija redefine al padre");
        System.out.println("============================================");
    }
}
