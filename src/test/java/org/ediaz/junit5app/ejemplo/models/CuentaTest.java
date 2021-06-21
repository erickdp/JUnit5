package org.ediaz.junit5app.ejemplo.models;

import org.ediaz.junit5app.ejemplo.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.BigDecimalConversion;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

//    El ciclo de vida es que se crea una insstancia de la clase para cada test

    @Test
    void testNombreCuenta() {
        var cuenta = new Cuenta("Erick", new BigDecimal(1000.12345));
//        cuenta.setPersona("Erick");
        var esperado = "Erick";
        var real = cuenta.getPersona();
        assertNotNull(real);
        assertEquals(esperado, real); // se espera un valor y se lo compara con el que nos devuelva
        assertTrue(real.equals("Erick"));
    }

    @Test
    void testSaldoCuenta() {
        var cuenta = new Cuenta("Erick", new BigDecimal("1000.12345")); // Se puede usar solo new Cuenta y luego CTRL + ALT + V para generar codigo
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); // Si saldo es menor a 0 devuelve -1 si es mayor 1 y si son iguales 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        var cuenta = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));
        var cuenta2 = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta); // No son iguales por que tiene otro puntero a la memoria, pero si se agrega el metodo equals ya son iguales
    }

    @Test
    void testDebitoCuenta() {
        var cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100)); // De igual manera si se lanza una excepcion las pruebas de abajo no se ejecutan
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        var cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {
        var cuenta = new Cuenta("Erick", new BigDecimal("1000.12345"));
        var excepcion = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        }); // Si se especifica aqui otra excepcion el test va a fallat porque la que se lanza es DineroInsuficienteException
        var mensaje = excepcion.getMessage();
        var esperado = "Dinero insuficiente";
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
    void testRelacioneBancoCuentas() {
        var cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        var cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        var banco = new Banco();
        banco.setCuentas(new ArrayList<>());
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco de Quito");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());

        assertEquals(2, banco.getCuentas().size());
        assertEquals("Banco de Quito", cuenta1.getBanco().getNombre());
//        Primer forma
        assertEquals("Andres", banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Andres")) // filtro la lista hasta encontrar Andres
                .findFirst() // Escogo el primero que tenga Andres
                .get().getPersona()); // Y devuelvo a la persona
//        Segunda fomra
        assertTrue(banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Andres")) // filtro la lista hasta encontrar Andres
                .findFirst()
                .isPresent());
//        Tercera forma
        assertTrue(banco.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("Andres"))); // Busca algun match igual a Andres
    }


}