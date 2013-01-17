package model;

import javax.swing.KeyStroke;

import javax.swing.text.AttributeSet;
import ui.Triggerable;
import ui.dialog.SetupDialogManageable;

public class Role extends EditableModel implements SetupDialogManageable<Role>, Triggerable,Cloneable {
  private String name;
  private String displayName;
  private String description;
  private Style style;
  private KeyStroke hotKey;
  private boolean nameDisplayed;
  private boolean promptDisplayed;
  private boolean removable;
  private boolean parenthesized;


  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public KeyStroke getHotKey() {
    return hotKey;
  }

  public void setHotKey(KeyStroke hotKey) {
    this.hotKey = hotKey;
  }

  public String getHotKeyString(){
    String hotKeyString = null;
    if(hotKey != null){
      hotKeyString = hotKey.toString();
    }
    return hotKeyString;
  }

  public void setHotKeyString(String hotKeyString){
    this.hotKey = KeyStroke.getKeyStroke(hotKeyString);
  }

  public boolean isRemovable() {
    return removable;
  }

  public void setRemovable(boolean removable) {
    this.removable = removable;
  }

  public boolean isNameDisplayed() {
    return nameDisplayed;
  }

  public void setNameDisplayed(boolean nameDisplayed) {
    this.nameDisplayed = nameDisplayed;
  }

  public boolean isPromptDisplayed() {
    return promptDisplayed;
  }

  public void setPromptDisplayed(boolean promptDisplayed) {
    this.promptDisplayed = promptDisplayed;
  }

  public Style getStyle() {
    return style;
  }

  public void setStyle(Style style) {
    this.style = style;
  }

  public boolean isParenthesized(){
    return parenthesized;
  }

  public void setParenthesized(boolean parenthesized){
    this.parenthesized = parenthesized;
  }

  public Role(){
  }


  // SetupDialogManageable interface
  public int compareTo(Role other){
    return this.name.compareTo(other.name);
  }

  public String getListValue(){
    String listValue = name;
    if(hotKey != null){
      listValue = listValue + "\t (Hot Key: " + hotKey.toString() + ")";
    }
    return listValue;
  }

  public boolean isValid(){
    return ((name != null) && (name.length() > 0) && (style != null));
  }

  public String getNameForDisplay(){
    if(displayName != null && displayName.length() > 0){
      return displayName;
    }
    else{
      return name;
    }
  }

  public String getPreviewText(){
    String text = "The quick brown fox...";
    if(parenthesized){
      text = "(" + text + ")";
    }
    if(promptDisplayed){
      text = getNameForDisplay() + ": " + text;
    }
    return text;
  }
  
  public AttributeSet getPreviewStyle(){
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
    return "(Role) " + getName() + ": " + hotKeyDescription;
  }

  public KeyStroke getTrigger(){
    return getHotKey();
  }

  public void setTrigger(KeyStroke keyStroke){
    setHotKey(keyStroke);
  }
  //

  public Object clone(){
    Role copy = new Role();
    copy.name = this.name;
    copy.removable = this.removable;
    copy.description = this.description;
    copy.displayName = this.displayName;
    copy.hotKey = this.hotKey;
    copy.nameDisplayed = this.nameDisplayed;
    copy.promptDisplayed = this.promptDisplayed;
    copy.style = this.style;
    return copy;
  }
}
