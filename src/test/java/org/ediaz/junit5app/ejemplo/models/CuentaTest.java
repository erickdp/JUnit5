package org.ediaz.junit5app.ejemplo.models;

import org.ediaz.junit5app.ejemplo.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.BigDecimalConversion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // No es recomendable usar esta anotacion pues solo se crea un instancia
class CuentaTest {

    Cuenta cuenta; // Esta variable no guarda estado, se crea una nueva para cada test

    //    Este metodo con esta anotacion se ejecuta antes de cada instancia de test
    @BeforeEach
    void initMethod() {
        this.cuenta = new Cuenta("Erick", new BigDecimal("1000.12345")); // El objetivo es tomar valores comunes y obtener codigo mas limpio
        System.out.println("Iniciado el metodo.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Terminando el metodo de prueba");
    }

    //    Se ejecuta este metodo antes de la instancia de la clase por eso es estatico
    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando la clase de prueba");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando la clase de prueba");
    }

    //    El ciclo de vida es que se crea una insstancia de la clase para cada test
    @Test
    @DisplayName("Probando nombre de la cuenta corriente") // Esta anotacion sirve para dar nombre descriptivo al test
    @Disabled
    // Esta anotacion sirve para saltar las pruebas
    void testNombreCuenta() {
//        fail(); Intenciona el fallo
//        cuenta.setPersona("Erick");
        var esperado = "Erick";
        var real = cuenta.getPersona();
        assertNotNull(real);
        assertEquals(esperado, real); // se espera un valor y se lo compara con el que nos devuelva
        assertTrue(real.equals("Erick"));
    }

    @Test
    @DisplayName("probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado")
    void testSaldoCuenta() {
//        var cuenta = new Cuenta("Erick", new BigDecimal("1000.12345")); // Se puede usar solo new Cuenta y luego CTRL + ALT + V para generar codigo
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); // Si saldo es menor a 0 devuelve -1 si es mayor 1 y si son iguales 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("testeando referencias que sean iguales con el metodo equals")
    void testReferenciaCuenta() {
        var cuenta = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));
        var cuenta2 = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta); // No son iguales por que tiene otro puntero a la memoria, pero si se agrega el metodo equals ya son iguales
    }

    @Test
    void testDebitoCuenta() {
//        var cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100)); // De igual manera si se lanza una excepcion las pruebas de abajo no se ejecutan
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
//        var cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {
//        var cuenta = new Cuenta("Erick", new BigDecimal("1000.12345"));
        var excepcion = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        }); // Si se especifica aqui otra excepcion el test va a fallat porque la que se lanza es DineroInsuficienteException
        var mensaje = excepcion.getMessage();
        var esperado = "Dinero Insuficiente";
        assertEquals(esperado, mensaje);
    }

    @Test
    void testTransferirDineroCuentas() {
        var cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        var cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        var banco = new Banco();
        banco.setNombre("Banco de Quito");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    @Disabled
    @DisplayName("probando relacion entre las cuentas y el banco con assertAll")
    void testRelacioneBancoCuentas() {
        var cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        var cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        var banco = new Banco();
        banco.setCuentas(new ArrayList<>());
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco de Quito");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

//        Si existe fallo en mas de un assert solo se mostrara el primero y los otros no. La solucion es usar assertAll
        assertAll(
                () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()), // Si la prueba es mas de una linea se usa {}
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size(),
                        () -> "El numero de cuentas en el banco no es el esperado"),
                // Se pueden pasar mensajes y seran construcciones futuras que solo se crean el String si falla el test, esto es para mejorar el rendimiento
                () -> assertEquals("Banco de Quito", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Andres", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Andres")) // filtro la lista hasta encontrar Andres
                        .findFirst() // Escogo el primero que tenga Andres
                        .get().getPersona()));
    }

//    Se utiliza anotaciones para definir si los metodos se ejecutan si cumple una propiedad como por ejemplo tipo de SO, arquitectura, etc.

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows() {
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void testSoloLinuxYMac() {
    }

//    Si tiene un jdk diferente


    @Test
    @EnabledOnJre(JRE.JAVA_11)
    void testJRE11() {
    }

    @Test
    @DisabledOnJre(JRE.JAVA_16)
    void testNOJRE15() {
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "1.8.0")
    void testJavaVersion() {
    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "peddoooo")
    void testUsuario() {
    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    void testArquitectura() {
    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testDev() {
    }

    @Test
    void imprimirSystemProperties() {
        var properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    void imprimirVariablesEntorno() {
        var getenv = System.getenv();
        getenv.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "COMPUTERNAME", matches = "ASUS")
    void testNombrePC() {
    }
}