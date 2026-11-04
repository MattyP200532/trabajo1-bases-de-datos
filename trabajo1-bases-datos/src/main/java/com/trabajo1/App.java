
package com.trabajo1;

import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcion;

        do {
            System.out.println("\n==================================");
            System.out.println("       CAFETERIA UTN");
            System.out.println("==================================");
            System.out.println("1. Gestionar ingredientes");
            System.out.println("2. Gestionar platos");
            System.out.println("3. Gestionar bebidas");
            System.out.println("4. Gestionar recetas");
            System.out.println("5. Registrar abastecimientos");
            System.out.println("6. Registrar ventas de platos");
            System.out.println("7. Registrar ventas de bebidas");
            System.out.println("8. Consultar reportes");
            System.out.println("0. Salir");
            System.out.println("==================================");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        System.out.println("Gestion de ingredientes");
                        break;
                    case 2:
                        System.out.println("Gestion de platos");
                        break;
                    case 3:
                        System.out.println("Gestion de bebidas");
                        break;
                    case 4:
                        System.out.println("Gestion de recetas");
                        break;
                    case 5:
                        System.out.println("Registro de abastecimientos");
                        break;
                    case 6:
                        System.out.println("Venta de platos");
                        break;
                    case 7:
                        System.out.println("Venta de bebidas");
                        break;
                    case 8:
                        System.out.println("Consulta de reportes");
                        break;
                    case 0:
                        System.out.println("Cerrando el sistema...");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }

            } catch (NumberFormatException e) {
                opcion = -1;
                System.out.println("Debe ingresar un numero.");
            }

        } while (opcion != 0);

        scanner.close();
    }
}