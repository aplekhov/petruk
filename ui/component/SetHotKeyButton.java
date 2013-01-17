/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author anthony
 */
public class SetHotKeyButton extends JButton implements ActionListener, KeyListener, FocusListener {

  public SetHotKeyButton(Container container, JTextComponent display) {
    super();
    this.container = container;
    this.display = display;
    this.setText("Set Key");
    this.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {
    _setToListenMode();
  }

  public void keyPressed(KeyEvent e) {
    //System.out.println(KeyStroke.getKeyStrokeForEvent(e));
    //System.out.println(e.getKeyCode());
    KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
    if (keyStroke.getKeyCode() == KeyEvent.VK_ESCAPE) {
      // TODO replace old value?
      setCapturedKeyStroke(null);
    } else {
      //display.setText(keyStroke.toString());
      setCapturedKeyStroke(keyStroke);
      //if(keyStroke.getKeyCode())
    }
    //_notifyOfChange();
    
  }

  public void keyReleased(KeyEvent e) {
    _setToDisplayMode();
  }

  public void keyTyped(KeyEvent e) {
  }

  public void focusGained(FocusEvent e) {
  }

  public void focusLost(FocusEvent e) {
    container.requestFocus();
  }

  public void setCapturedKeyStroke(KeyStroke keyStroke) {
    synchronized (this) {
      capturedKeyStroke = keyStroke;
    }
    _updateDisplay();
  }

  public synchronized KeyStroke getCapturedKeyStroke() {
    return capturedKeyStroke;
  }

  //public void setChangeListener(ChangeListener c){
    //this.keyChangeListener = c;
  //}

  protected void _updateDisplay() {
    if (capturedKeyStroke != null) {
      String modifiers = KeyEvent.getKeyModifiersText(capturedKeyStroke.getModifiers());
      String keyText = KeyEvent.getKeyText(capturedKeyStroke.getKeyCode());
      if (modifiers.length() > 0) {
        keyText = modifiers + "+" + keyText;
      }
      display.setText(keyText);
    } else {
      display.setText("None");
    }
  }

  protected void _setToListenMode(){
    this.setEnabled(false);
    this.setText("Press Key");
    // TODO save old value?
    display.setText("");
    container.addFocusListener(this);
    container.addKeyListener(this);
    container.requestFocus();
  }

  protected void _setToDisplayMode(){
    container.removeFocusListener(this);
    container.removeKeyListener(this);
    // TODO replace old value?
    this.setText("Set Key");
    this.setEnabled(true);
    this.fireStateChanged();
  }
  /*
  protected void _notifyOfChange(){
    if(keyChangeListener != null){
      keyChangeListener.stateChanged(new ChangeEvent(this));
    }
  }

 */
  private Container container;
  private JTextComponent display;
  private KeyStroke capturedKeyStroke;
  //private ChangeListener keyChangeListener;
}
