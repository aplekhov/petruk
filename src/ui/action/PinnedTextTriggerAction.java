/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.action;

import java.util.Set;
import javax.swing.AbstractAction;
import model.PinnedText;
import ui.editor.PetrukDisplay;

/**
 *
 * @author anthony
 */
public class PinnedTextTriggerAction extends AbstractAction{
  private PinnedText pinnedText;
  private PetrukDisplay display;
  private boolean isEnabled;
  private static Set<PinnedTextTriggerAction> allActions;

  public PinnedTextTriggerAction(PinnedText pinnedText, PetrukDisplay display){
    this.pinnedText = pinnedText;
    this.display = display;
    isEnabled = false;
  }

  public static void setAllActions(Set<PinnedTextTriggerAction> allActions){
    PinnedTextTriggerAction.allActions = allActions;
  }
  
  public void actionPerformed(java.awt.event.ActionEvent event){
    _checkAllActions();
    if(isEnabled == false){
      display.setPinnedTextPanelVisible(true);
      display.setPinnedTextStyle(pinnedText.getStyle());
      display.setPinnedText("\n" + pinnedText.getText());
      isEnabled = true;
    }
    else{
      display.setPinnedTextPanelVisible(false);
      isEnabled = false;
    }
  }

  // another dirty hack
  private void _checkAllActions(){
    for(PinnedTextTriggerAction action:allActions){
      if(action != this){
        action.isEnabled = false;
      }
    }
  }
}
