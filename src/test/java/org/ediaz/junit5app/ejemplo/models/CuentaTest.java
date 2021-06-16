package org.ediaz.junit5app.ejemplo.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

//    El ciclo de vida es que se crea una insstancia de la clase para cada test

    @Test
    void testNombreCuenta() {
        var cuenta = new Cuenta("Erick", new BigDecimal(1000.12345));
//        cuenta.setPersona("Erick");
        var esperado = "Erick";
        var real = cuenta.getPersona();
        assertEquals(esperado, real); // se espera un valor y se lo compara con el que nos devuelva
        assertTrue(real.equals("Erick"));
    }

    @Test
    void testSaldoCuenta() {
        var cuenta = new Cuenta("Erick", new BigDecimal("1000.12345")); // Se puede usar solo new Cuenta y luego CTRL + ALT + V para generar codigo
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); // Si saldo es menor a 0 devuelve -1 si es mayor 1 y si son iguales 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }


}