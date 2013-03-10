/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.action;

import java.util.logging.Logger;
import javax.swing.AbstractAction;
import model.Role;
import ui.editor.PetrukDisplay;

/**
 *
 * @author anthony
 */
public class RoleTriggerAction extends AbstractAction{
  private Role role;
  private PetrukDisplay display;

  protected static Logger logger = Logger.getLogger(RoleTriggerAction.class.getPackage().getName());

  public RoleTriggerAction(Role role, PetrukDisplay display){
    this.role = role;
    this.display = display;
  }
  
  public void actionPerformed(java.awt.event.ActionEvent event){
    logger.info("action triggered!");
    // if we're in the middle of dialogue or it's a new line but we don't display the prompt, then just
    // trigger the displaying of the name, without switching styles
    if(display.isInDialogue() || !role.isPromptDisplayed()){
      if(role.isNameDisplayed()){
        display.insertText(role.getNameForDisplay());
      }
    }
    else{
      display.setStatusBarText("current role: " + role.getName());
      if (role.isPromptDisplayed()){
        display.insertText("\n"+ role.getNameForDisplay() + ": ");
      }else{
        display.insertText("\n");
      }
      if(role.isParenthesized()){
        display.insertText("()");
        display.moveCaretBack(1);
      }
      display.setCurrentTextStyle(role.getStyle());
    }
  }
}
