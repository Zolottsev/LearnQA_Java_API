package tests.old;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PhraseLengthTest {
    @Test
    public void phraseLengthTest() {
       int length = string().length();
       boolean a = true;
       if (length<=15)  a = false;
       assertEquals(true, a, "Phrase length less than 15 characters");
    }

    public String string() {
        return "aaaaaaaaaaбббббk";
    }
}
