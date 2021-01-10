package WasteXero_v2;

import javax.swing.*;

/**
 * a kind of structure for GUI, a special panel
 */
class BorderPanel extends JPanel {
    BorderPanel(String title) {
        setBorder(BorderFactory.createTitledBorder(title));
    }
}