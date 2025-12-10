import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test; //Needed to create test methods
import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals

public class ExampleTest extends BaseTest { //needed as a base to create other tests

    @Test //marks test code and is needed before every block of test code
    public void exampleTestCode() {
        int someNum = 0;
        someNum = 10;
        assertEquals(10, someNum);
    }

    @Test //marks test code and is needed before every block of test code
    public void otherExampleTestCode() {
        int otherNum;
        otherNum = 15;
        assertEquals(15, otherNum);
    }

    @Test //marks test code and is needed before every block of test code
    public void playSoundTest() {
        int otherNum;
        otherNum = 15;
        assertEquals(15, otherNum);
    }
}

//If you take the stuff down here, it's a good template

//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test; //Needed to create test methods
//import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals
//
//public class CharacterSelectScreenTest extends BaseTest { //needed as a base to create other tests
//
//    @BeforeAll (this bit is optional)
//    public static void testSetup() {
//        //insert code here
//    }
//
//    @Test //marks test code and is needed before every block of test code
//    public void exampleTestCode() {
//        //insert code here
//    }
//}
