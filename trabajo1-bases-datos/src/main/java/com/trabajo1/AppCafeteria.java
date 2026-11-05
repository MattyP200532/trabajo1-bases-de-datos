
package com.trabajo1;

import com.trabajo1.cafeteria.controller.ReporteController;
import com.trabajo1.cafeteria.controller.BebidaController;
import com.trabajo1.cafeteria.controller.VentaBebidasController;
import com.trabajo1.cafeteria.controller.RecetaController;
import com.trabajo1.cafeteria.controller.PlatoController;
import com.trabajo1.cafeteria.controller.VentaPlatosController;
import com.trabajo1.cafeteria.controller.MovimientoInventarioController;
import com.trabajo1.cafeteria.controller.IngredienteController;
import com.trabajo1.cafeteria.controller.AbastecimientoController;
import java.util.Scanner;

public class AppCafeteria {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        IngredienteController ingredienteController = new IngredienteController(scanner);
PlatoController platoController = new PlatoController(scanner);
AbastecimientoController abastecimientoController = new AbastecimientoController(scanner);
MovimientoInventarioController movimientoController = new MovimientoInventarioController(scanner);
VentaPlatosController ventaPlatosController = new VentaPlatosController(scanner);
         RecetaController recetaController = new RecetaController(scanner);
         VentaBebidasController ventaBebidasController =
        new VentaBebidasController(scanner);
        BebidaController bebidaController =
        new BebidaController(scanner);
        ReporteController reporteController =
        new ReporteController(scanner);

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
            System.out.println("9. Consultar movimientos de inventario");
            System.out.println("0. Salir");
            System.out.println("==================================");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {

                    case 1:
                        int opcionIngrediente;

                        do {
                            System.out.println("\n--- GESTIONAR INGREDIENTES ---");
                            System.out.println("1. Registrar ingrediente");
                            System.out.println("2. Listar ingredientes");
                            System.out.println("0. Volver al menu principal");
                            System.out.print("Seleccione una opcion: ");

                            try {
                                opcionIngrediente =
                                        Integer.parseInt(scanner.nextLine());

                                switch (opcionIngrediente) {
                                    case 1:
                                        ingredienteController.registrarIngrediente();
                                        break;
                                    case 2:
                                        ingredienteController.listarIngredientes();
                                        break;
                                    case 0:
                                        System.out.println("Volviendo al menu principal...");
                                        break;
                                    default:
                                        System.out.println("Opcion no valida.");
                                }

                            } catch (NumberFormatException e) {
                                opcionIngrediente = -1;
                                System.out.println("Debe ingresar un numero.");
                            }

                        } while (opcionIngrediente != 0);

                        break;

                    case 2:
    int opcionPlato;

    do {
        System.out.println("\n--- GESTIONAR PLATOS ---");
        System.out.println("1. Registrar plato");
        System.out.println("2. Listar platos");
        System.out.println("0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        try {
            opcionPlato = Integer.parseInt(scanner.nextLine());

            switch (opcionPlato) {
                case 1:
                    platoController.registrarPlato();
                    break;

                case 2:
                    platoController.listarPlatos();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } catch (NumberFormatException e) {
            opcionPlato = -1;
            System.out.println("Debe ingresar un numero.");
        }

    } while (opcionPlato != 0);

    break;

                    
case 3:
    int opcionBebida;

    do {
        System.out.println("\n--- GESTIONAR BEBIDAS ---");
        System.out.println("1. Registrar bebida");
        System.out.println("2. Listar bebidas");
        System.out.println("3. Desactivar bebida");
        System.out.println("0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        try {
            opcionBebida = Integer.parseInt(scanner.nextLine());

            switch (opcionBebida) {
                case 1:
                    bebidaController.registrarBebida();
                    break;

                case 2:
                    bebidaController.listarBebidas();
                    break;

                case 3:
                    bebidaController.desactivarBebida();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } catch (NumberFormatException e) {
            opcionBebida = -1;
            System.out.println("Debe ingresar un numero.");
        }

    } while (opcionBebida != 0);

    break;


                    case 4:
    int opcionReceta;

    do {
        System.out.println("\n===== GESTION DE RECETAS =====");
        System.out.println("1. Registrar receta");
        System.out.println("2. Listar recetas");
        System.out.println("0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        try {
            opcionReceta = Integer.parseInt(scanner.nextLine());

            switch (opcionReceta) {
                case 1:
                    recetaController.registrarReceta();
                    break;

                case 2:
                    recetaController.listarRecetas();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } catch (NumberFormatException e) {
            opcionReceta = -1;
            System.out.println("Ingrese un numero valido.");
        }

    } while (opcionReceta != 0);

    break;

                    case 5:
    int opcionAbastecimiento;

    do {
        System.out.println("\n--- GESTIONAR ABASTECIMIENTOS ---");
        System.out.println("1. Registrar abastecimiento");
        System.out.println("2. Listar abastecimientos");
        System.out.println("0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        try {
            opcionAbastecimiento =
                    Integer.parseInt(scanner.nextLine());

            switch (opcionAbastecimiento) {
                case 1:
                    abastecimientoController.registrarAbastecimiento();
                    break;

                case 2:
                    abastecimientoController.listarAbastecimientos();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } catch (NumberFormatException e) {
            opcionAbastecimiento = -1;
            System.out.println("Debe ingresar un numero.");
        }

    } while (opcionAbastecimiento != 0);

    break;

                    case 6:
    int opcionVentaPlatos;

    do {
        System.out.println("\n--- GESTIONAR VENTAS DE PLATOS ---");
        System.out.println("1. Registrar venta de platos");
        System.out.println("2. Listar ventas de platos");
        System.out.println("0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        try {
            opcionVentaPlatos =
                    Integer.parseInt(scanner.nextLine());

            switch (opcionVentaPlatos) {
                case 1:
                    ventaPlatosController.registrarVenta();
                    break;

                case 2:
                    ventaPlatosController.listarVentas();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } catch (NumberFormatException e) {
            opcionVentaPlatos = -1;
            System.out.println("Debe ingresar un numero.");
        }

    } while (opcionVentaPlatos != 0);

    break;

                    case 7:
    int opcionVentaBebidas;

    do {
        System.out.println("\n--- GESTIONAR VENTAS DE BEBIDAS ---");
        System.out.println("1. Registrar venta de bebidas");
        System.out.println("2. Listar ventas de bebidas");
        System.out.println("0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        try {
            opcionVentaBebidas =
                    Integer.parseInt(scanner.nextLine());

            switch (opcionVentaBebidas) {
                case 1:
                    ventaBebidasController.registrarVenta();
                    break;

                case 2:
                    ventaBebidasController.listarVentas();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } catch (NumberFormatException e) {
            opcionVentaBebidas = -1;
            System.out.println("Debe ingresar un numero.");
        }

    } while (opcionVentaBebidas != 0);

    break;

                    case 8:
    reporteController.mostrarMenu();
    break;
                    
case 9:
    int opcionMovimiento;

    do {
        System.out.println("\n--- MOVIMIENTOS DE INVENTARIO ---");
        System.out.println("1. Listar todos los movimientos");
        System.out.println("2. Buscar movimientos por ingrediente");
        System.out.println("0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        try {
            opcionMovimiento =
                    Integer.parseInt(scanner.nextLine());

            switch (opcionMovimiento) {
                case 1:
                    movimientoController.listarMovimientos();
                    break;

                case 2:
                    movimientoController.buscarMovimientosPorIngrediente();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } catch (NumberFormatException e) {
            opcionMovimiento = -1;
            System.out.println("Debe ingresar un numero.");
        }

    } while (opcionMovimiento != 0);

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

        // No cerrar System.in
    }
}