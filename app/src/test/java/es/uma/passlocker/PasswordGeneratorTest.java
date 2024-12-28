package es.uma.passlocker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.uma.passlocker.utils.PasswordGenerator;

public class PasswordGeneratorTest {
    @Test
    public void testGenerate_lengthLessThanMinLength_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator passwordGenerator = new PasswordGenerator();
            passwordGenerator.generate(2, true, true, true);
        });
    }

    @Test
    public void testGenerate_length() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generate(10, true, true, true);
        assertEquals(10, password.length());
    }

    @Test
    public void testGenerate_uppercaseTrue_containsUppercase() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generate(10, true, true, true);

        boolean containsUppercase = password.matches(".*[A-Z].*");

        assertTrue(containsUppercase);
    }

    @Test
    public void testGenerate_digitsTrue_containsDigits() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generate(10, true, true, true);

        boolean containsDigits = password.matches(".*[0-9].*");

        assertTrue(containsDigits);
    }

    @Test
    public void testGenerate_specialTrue_containsSpecial() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generate(10, true, true, true);

        boolean containsSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]|,./?><].*");

        assertTrue(containsSpecial);
    }

    @Test
    public void testGenerate_justLowercase() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generate(10, false, false, false);

        boolean justLowercase = password.matches("[a-z]+")
                && !password.matches(".*[A-Z].*")
                && !password.matches(".*[0-9].*")
                && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]|,./?><].*");

        assertTrue(justLowercase);
    }
}
