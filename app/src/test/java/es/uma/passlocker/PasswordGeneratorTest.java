package es.uma.passlocker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.uma.passlocker.utils.PasswordGenerator;

public class PasswordGeneratorTest {

    @Test
    public void generate_lengthNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generate(null, true, true, true);
        });
    }

    @Test
    public void generate_lengthZero_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generate(0, true, true, true);
        });
    }

    @Test
    public void generate_lengthNegative_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generate(-10, true, true, true);
        });
    }

    @Test
    public void generate_lengthLessThanMinLength_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generate(2, true, true, true);
        });
    }

    @Test
    public void generate_length10_returns10() {
        String password = PasswordGenerator.generate(10, true, true, true);
        assertEquals(10, password.length());
    }

    @Test
    public void generate_uppercaseTrue_containsUppercase() {
        String password = PasswordGenerator.generate(10, true, true, true);

        boolean containsUppercase = password.matches(".*[A-Z].*");

        assertTrue(containsUppercase);
    }

    @Test
    public void generate_digitsTrue_containsDigits() {
        String password = PasswordGenerator.generate(10, true, true, true);

        boolean containsDigits = password.matches(".*[0-9].*");

        assertTrue(containsDigits);
    }

    @Test
    public void generate_specialTrue_containsSpecial() {
        String password = PasswordGenerator.generate(10, true, true, true);

        boolean containsSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]|,./?><].*");

        assertTrue(containsSpecial);
    }

    @Test
    public void generate_justLowercase() {
        String password = PasswordGenerator.generate(10, false, false, false);

        boolean justLowercase = password.matches("[a-z]+")
                && !password.matches(".*[A-Z].*")
                && !password.matches(".*[0-9].*")
                && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]|,./?><].*");

        assertTrue(justLowercase);
    }
}
