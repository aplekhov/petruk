/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import ui.Triggerable;
import ui.dialog.SetupDialogManageable;

/**
 *
 * @author anthony
 */
public class PinnedText extends EditableModel implements SetupDialogManageable<PinnedText>, Triggerable {

  private String name;
  private String text;
  private Style style;
  private KeyStroke hotKey;
  private boolean removable;

  public PinnedText() {
  }

  public KeyStroke getHotKey() {
    return hotKey;
  }

  public void setHotKey(KeyStroke hotKey) {
    this.hotKey = hotKey;
  }

  public String getHotKeyString() {
    String hotKeyString = null;
    if (hotKey != null) {
      hotKeyString = hotKey.toString();
    }
    return hotKeyString;
  }

  public void setHotKeyString(String hotKeyString) {
    this.hotKey = KeyStroke.getKeyStroke(hotKeyString);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Style getStyle() {
    return style;
  }

  public void setStyle(Style style) {
    this.style = style;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int compareTo(PinnedText other) {
    return this.name.compareTo(other.name);
  }

  // SetupDialogManageable Interface
  public String getListValue() {
    String listValue = name;
    if (hotKey != null) {
      listValue = listValue + "\t (Hot Key: " + hotKey.toString() + ")";
    }
    return listValue;
  }

  public boolean isRemovable() {
    return removable;
  }

  public void setRemovable(boolean removable) {
    this.removable = removable;
  }

  public boolean isValid() {
    return ((name != null) && (name.length() > 0) &&
            (text != null) && (text.length() > 0) && (style != null));
  }

  public String getPreviewText() {
    return text;
  }

  public AttributeSet getPreviewStyle() {
    return style.getPreviewStyle();
  }

  // Triggerable Interface
  public String getTriggerableListValue(){
    String hotKeyDescription = null;
    if(hotKey == null){
      hotKeyDescription = "None";
    }else{
      hotKeyDescription = hotKey.toString();
    }
    return "(PinnedText) " + getName() + ": " + hotKeyDescription;
  }

  public KeyStroke getTrigger(){
    return getHotKey();
  }

  public void setTrigger(KeyStroke keyStroke){
    setHotKey(keyStroke);
  }
  //
}
