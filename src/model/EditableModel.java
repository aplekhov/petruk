/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.LinkedList;
import java.util.List;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import ui.ChangeListener;
import util.StateCareTaker;

/**
 *
 * @author anthony
 */
public class EditableModel {
  private transient StateCareTaker stateCareTaker;
  private transient LinkedList<ChangeListener> changeListeners;

  public EditableModel(){
    this.stateCareTaker = new StateCareTaker(this);
    this.changeListeners = new LinkedList<ChangeListener>();
  }

  public void addChangeListener(ChangeListener c){
    changeListeners.add(c);
  }

  public NotifiableStateEdit getStateEdit(){
    return new NotifiableStateEdit(this.stateCareTaker, changeListeners);
  }

  public class NotifiableStateEdit extends StateEdit{
    private List<ChangeListener> listeners;
    public NotifiableStateEdit(StateEditable object, List<ChangeListener> listeners){
      super(object);
      this.listeners = listeners;
    }

    public void endWithNotify(){
      super.end();
      if(!this.postState.isEmpty()){
        for(ChangeListener c:listeners){
          c.notifyOfChange();
        }
      }
    }


  }
}
