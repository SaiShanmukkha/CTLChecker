
import javax.swing.SwingUtilities;

import view.CTLCheckerUI;

public class Main {
    public static void main(String [] args) {
        SwingUtilities.invokeLater(CTLCheckerUI::new);
    }
}
