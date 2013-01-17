/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.dialog;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author anthony
 */
public class FileDialogs {
  public static File showSaveDialog(Component parent, String title, File suggestedFile){
    File newFile = null;
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle(title);
    chooser.setMultiSelectionEnabled(false);
    chooser.setSelectedFile(suggestedFile);
    chooser.setApproveButtonText("Save");
    int returnVal = chooser.showOpenDialog(parent);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      newFile = chooser.getSelectedFile();
      if (newFile.exists()) {
        Object[] options = {"OK", "CANCEL"};
        int result = JOptionPane.showOptionDialog(parent, "File already exists!  Replace?", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (result != JOptionPane.OK_OPTION) {
          newFile = null;
        }
      }
    }
    return newFile;
  }

  public static File showOpenDialog(Component parent, String title, File suggestedFile){
    File newFile = null;
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle(title);
    chooser.setMultiSelectionEnabled(false);
    chooser.setSelectedFile(suggestedFile);
    int returnVal = chooser.showOpenDialog(parent);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      newFile = chooser.getSelectedFile();
      if (!newFile.exists()) {
        JOptionPane.showMessageDialog(parent, "Cannot open this file!");
        newFile = null;
      }
    }
    return newFile;
  }
}
