/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dialog;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author anthony
 */
class ListRenderer extends DefaultListCellRenderer {

  public Component getListCellRendererComponent(JList list,
          Object value,
          int index,
          boolean isSelected,
          boolean cellHasFocus) {
    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    this.setText(((SetupDialogManageable) value).getListValue());
    return c;
  }
}
