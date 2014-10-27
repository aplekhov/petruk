/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.undo.StateEdit;

/**
 *
 * @author anthony
 */
abstract class AbstractDetailDialog extends javax.swing.JDialog implements
        InputMethodListener, ListSelectionListener, ActionListener,
        CaretListener, ChangeListener {

  private SetupDialogManageable object;
  private StateEdit originalState;
  private boolean committed;

  AbstractDetailDialog(Dialog parent, String title, Color previewBackground) {
    super(parent, title, true);
    this.setResizable(false);

    this.committed = false;
    // little hack to work with IDE
    _createComponents();
    _getPreviewPane().setBackground(previewBackground);
  }

  protected SetupDialogManageable _getFinalObject() {
    if (committed) {
      return object;
    } else {
      return null;
    }
  }

  protected abstract JTextPane _getPreviewPane();

  protected abstract void _updateUIFromModel();

  protected abstract void _updateModelFromUI();

  protected abstract void _createComponents();

  // MUST BE CALLED FROM SUBCLASS BEFORE DOING ANYTHING
  protected final void _setObject(SetupDialogManageable object){
    this.object = object;
    this.originalState = object.getStateEdit();
  }

  protected void _refreshPreview() {
    if (object != null) {
      _getPreviewPane().setCharacterAttributes(object.getPreviewStyle(), true);
      _getPreviewPane().setText(object.getPreviewText());
    } else {
      _getPreviewPane().setText(null);
    }
  }

  protected void _validate() {
    if (object.isValid()) {
      _onValid();
    } else {
      _onInvalid();
    }
  }

  protected void _onInvalid() {
  }

  ;

  protected void _onValid() {
  }

  ;

  protected void _onCommit() {
    originalState.end();
    committed = true;
    dispose();
  }

  protected void _onCancel() {
    originalState.end();
    originalState.undo();
    committed = false;
    dispose();
  }

  protected void _registerCommitButton(JButton button) {
    button.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _onCommit();
      }
    });
  }

  protected void _registerCancelButton(JButton button) {
    button.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _onCancel();
      }
    });
  }

  protected void _processUIChange() {
    _updateModelFromUI();
    _validate();
    _refreshPreview();
  }

  public void caretPositionChanged(InputMethodEvent event) {
    _processUIChange();
  }

  public void inputMethodTextChanged(InputMethodEvent event) {
    _processUIChange();
  }

  public void valueChanged(ListSelectionEvent e) {
    _processUIChange();
  }

  public void actionPerformed(ActionEvent e) {
    _processUIChange();
  }

  public void caretUpdate(CaretEvent e){
    _processUIChange();
  }

  public void stateChanged(ChangeEvent e){
    _processUIChange();
  }
}
