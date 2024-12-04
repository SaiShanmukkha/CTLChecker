import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import controller.CTLFormula;
import model.KripkeStructure;
import model.State;

public class TestMain {

    private KripkeStructure kripke;

    private void loadKripkeModel(String modelPath) throws Exception {
        System.out.println("Loading Kripke model from: " + modelPath);
        String rawText = new String(Files.readAllBytes(Paths.get(modelPath)));
        String kripkeDefinition = utils.Util.cleanText(rawText);
        kripke = new KripkeStructure(kripkeDefinition);

        
        assertNotNull(kripke, "The Kripke model should be loaded.");
        assertFalse(kripke.states.isEmpty(), "The Kripke model should contain states.");
        assertFalse(kripke.transitions.isEmpty(), "The Kripke model should contain transitions.");
        System.out.println("Kripke model loaded and validated successfully.");
    }

    private void TestModelConditions(String modelPath, String formulasPath) throws Exception {
        
        loadKripkeModel(modelPath);

        System.out.println("Testing conditions from: " + formulasPath);

        
        List<String> testLines = Files.readAllLines(Paths.get(formulasPath))
                .stream()
                .map(line -> line.startsWith("\uFEFF") ? line.substring(1) : line) 
                .toList();

        
        for (String line : testLines) {
            System.out.println("\nProcessing line: " + line); 

            String[] parts = line.split(";");
            if (parts.length != 3) {
                System.out.println("Malformed line: " + line); 
                continue;
            }

            String stateName = parts[0].trim();
            String condition = parts[1].trim();
            boolean expected = Boolean.parseBoolean(parts[2].trim());

            System.out.println("Testing state: " + stateName + ", Condition: " + condition);

            State testState = new State(stateName, kripke);
            CTLFormula ctlFormula = new CTLFormula(condition, testState, kripke);

            try {
                boolean result = ctlFormula.IsSatisfy();
                System.out.println("Result: " + result + ", Expected: " + expected);
                assertEquals(expected, result, "The formula '" + condition + "' should " +
                        (expected ? "" : "not ") + "hold in state " + stateName + ".");

                System.out.println("\nCompleted Successfully\n");
            } catch (Exception e) {
                System.out.println("Error processing condition: " + condition + " for state: " + stateName);
                e.printStackTrace();
                throw e; 
            }
        }
    }

    @Test
    void TestModel1() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 1.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 1 - Test Formulas.txt"
        );
    }

    @Test
    void TestModel2() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 2.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 2 - Test Formulas.txt"
        );
    }

    @Test
    void TestModel3() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 3.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 3 - Test Formulas.txt"
        );
    }

    @Test
    void TestModel4() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 4.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 4 - Test Formulas.txt"
        );
    }

    @Test
    void TestModel5() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 5.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 5 - Test Formulas.txt"
        );
    }

    @Test
    void TestModel6() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 6.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 6 - Test Formulas.txt"
        );
    }

    @Test
    void TestModel7() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 7.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 7 - Test Formulas.txt"
        );
    }
    
    @Test
    void microwaveExample() throws Exception {
        TestModelConditions(
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 8.txt",
                "S:\\Java\\modelCTLChecker\\app\\src\\test\\resources\\Test Files\\Model 8 - Test Formulas.txt"
        );
    }
}
