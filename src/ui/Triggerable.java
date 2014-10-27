/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import javax.swing.KeyStroke;
import model.EditableModel.NotifiableStateEdit;

/**
 *
 * @author anthony
 */
public interface Triggerable {
  public String getTriggerableListValue();
  public KeyStroke getTrigger();
  public void setTrigger(KeyStroke keyStroke);
  public NotifiableStateEdit getStateEdit();
}
