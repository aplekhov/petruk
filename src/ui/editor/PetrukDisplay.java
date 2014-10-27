/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.editor;

import model.Style;

/**
 *
 * @author anthony
 */
public interface PetrukDisplay {
  public void setStatusBarText(String text);

  public boolean isPinnedTextPanelVisible();
  public void setPinnedTextPanelVisible(boolean isVisible);
  public void setPinnedText(String text);
  public void setPinnedTextStyle(Style attributes);

  public boolean isInDialogue();

  public void insertText(String text);
  public void setCurrentTextStyle(Style attributes);
  public void moveCaretBack(int length);
}
