package es.uma.passlocker.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

public class PasswordUtilsTest {

    @Nested
    class Generate{
        @Test
        public void generate_lengthNull_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                PasswordUtils.generate(null, true, true, true);
            });
        }

        @Test
        public void generate_lengthZero_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                PasswordUtils.generate(0, true, true, true);
            });
        }

        @Test
        public void generate_lengthNegative_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                PasswordUtils.generate(-10, true, true, true);
            });
        }

        @Test
        public void generate_lengthLessThanMinLength_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                PasswordUtils.generate(2, true, true, true);
            });
        }

        @Test
        public void generate_length10_returns10() {
            String password = PasswordUtils.generate(10, true, true, true);
            assertEquals(10, password.length());
        }

        @Test
        public void generate_uppercaseTrue_containsUppercase() {
            String password = PasswordUtils.generate(10, true, true, true);

            boolean containsUppercase = password.matches(".*[A-Z].*");

            assertTrue(containsUppercase);
        }

        @Test
        public void generate_digitsTrue_containsDigits() {
            String password = PasswordUtils.generate(10, true, true, true);

            boolean containsDigits = password.matches(".*[0-9].*");

            assertTrue(containsDigits);
        }

        @Test
        public void generate_specialTrue_containsSpecial() {
            String password = PasswordUtils.generate(10, true, true, true);

            boolean containsSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]|,./?><].*");

            assertTrue(containsSpecial);
        }

        @Test
        public void generate_justLowercase() {
            String password = PasswordUtils.generate(10, false, false, false);

            boolean justLowercase = password.matches("[a-z]+")
                    && !password.matches(".*[A-Z].*")
                    && !password.matches(".*[0-9].*")
                    && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]|,./?><].*");

            assertTrue(justLowercase);
        }
    }

    @Nested
    class CheckStrength {

        @Test
        public void checkStrength_lengthLessThan7_returnsVeryWeak() {
            String password = "1bA4.6";

            int strength = PasswordUtils.checkStrength(password);

            assertEquals(25, strength);
        }

        @Test
        public void checkStrength_lengthLessThan9LowercaseOnly_returnsVeryWeak() {
            String password1 = "abcddfdh";
            String password2 = "atcdafe";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);

            assertEquals(25, strength1);
            assertEquals(25, strength2);
        }

        @Test
        public void checkStrength_lengthLessThan9NumbersOnly_returnsVeryWeak() {
            String password1 = "12345678";
            String password2 = "1234567";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);

            assertEquals(25, strength1);
            assertEquals(25, strength2);
        }

        @Test
        public void checkStrength_lengthLessThan9_returnsWeak() {
            String password1 = "ABCDdFaH";
            String password2 = "Aa2D4FaH";
            String password3 = "Aa2(4Fa.";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);

            assertEquals(50, strength1);
            assertEquals(50, strength2);
            assertEquals(50, strength3);
        }

        @Test
        public void checkStrength_lengthLessThan12NumbersOnly_returnsVeryWeak() {
            String password1 = "123456789";
            String password2 = "1234567890";
            String password3 = "12345678901";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);

            assertEquals(25, strength1);
            assertEquals(25, strength2);
            assertEquals(25, strength3);
        }

        @Test
        public void checkStrength_lengthLessThan12_returnsWeak() {
            String password1 = "ABCDdFaH12";
            String password2 = "Aa2D4FaH12";
            String password3 = "Aa2(4Fa.12";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);

            assertEquals(50, strength1);
            assertEquals(50, strength2);
            assertEquals(50, strength3);
        }

        @Test
        public void checkStrength_length11AllSimbols_returnsMedium(){
            String password = "Aa2(4Fa.12b";

            int strength = PasswordUtils.checkStrength(password);

            assertEquals(75, strength);
        }

        @Test
        public void checkStrength_lengthLessThan18NumbersOnly_returnsWeak(){
            String password1 = "12345678901234567";
            String password2 = "123456789012345";
            String password3 = "123456789012";


            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);

            assertEquals(50, strength1);
            assertEquals(50, strength2);
            assertEquals(50, strength3);
        }

        @Test
        public void checkStrength_lengthLessThan17_returnsMedium(){
            String password1 = "ABCDdFaHaabABXac";
            String password2 = "Aa2D4FaH12b12x6";
            String password3 = "Aa2(4Fa.12b1$a,2";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);

            assertEquals(75, strength1);
            assertEquals(75, strength2);
            assertEquals(75, strength3);
        }

        @Test
        public void checkStrength_lengthLessThan25AllNumbersOrAllLowerCase_returnsMedium() {
            String password = "123456789012345678901234";
            String password2 = "123456789012345678";
            String password3 = "abcdefghijabcdefghijaaa";
            String password4 = "abcdefghijabcdefgh";

            int strength = PasswordUtils.checkStrength(password);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);
            int strength4 = PasswordUtils.checkStrength(password4);

            assertEquals(75, strength);
            assertEquals(75, strength2);
            assertEquals(75, strength3);
            assertEquals(75, strength4);
        }

        @Test
        public void checkStrength_lengthGreaterThan16WithCombinations_returnsStrong() {
            String password1 = "Aa2(4Fa.12b1$a,2x";
            String password2 = "AaBbCcDdEeFfGgHhI";
            String password3 = "AaCC4Faa12b1Bab2x";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);

            assertEquals(100, strength1);
            assertEquals(100, strength2);
            assertEquals(100, strength3);
        }

        @Test
        public void checkStrength_lengthGreaterThan25_returnsStrong() {
            String password1 = "1234567890123456789012345";
            String password2 = "abcdefghijabcdefghijabcdefghi";
            String password3 = "Aa2(4Fa.12b1$a,2x3c4V5b6N7m8";
            String password4 = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPp";

            int strength1 = PasswordUtils.checkStrength(password1);
            int strength2 = PasswordUtils.checkStrength(password2);
            int strength3 = PasswordUtils.checkStrength(password3);
            int strength4 = PasswordUtils.checkStrength(password4);

            assertEquals(100, strength1);
            assertEquals(100, strength2);
            assertEquals(100, strength3);
            assertEquals(100, strength4);
        }
    }
}
