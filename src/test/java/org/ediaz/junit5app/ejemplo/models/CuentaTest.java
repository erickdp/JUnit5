package org.ediaz.junit5app.ejemplo.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        var cuenta = new Cuenta("Erick", new BigDecimal(1000.12345));
//        cuenta.setPersona("Erick");
        var esperado = "Erick";
        var real = cuenta.getPersona();
        assertEquals(esperado, real); // se espera un valor y se lo compara con el que nos devuelva
        assertTrue(real.equals("Erick"));
    }
}