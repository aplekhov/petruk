/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.Map;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import ui.dialog.SetupDialogManageable;
import util.SerializableAttributeSet;

/**
 *
 * @author anthony
 */
public class Style extends EditableModel implements SetupDialogManageable<Style>, Cloneable{
  private String name;
  private SerializableAttributeSet attributes;
  private boolean removable;
  

  public Style(){
    name = "";
    attributes = new SerializableAttributeSet();
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  public MutableAttributeSet getAttributeSet(){
    return attributes;
  }

  // for state store\restore
  public Map<String,String> getAttributes(){
    return attributes.getAttributesAsMap();
  }

  public void setAttributes(Map<String,String> attributes){
    this.attributes.setAttributesFromMap(attributes);
  }

  public String getListValue(){
    return getName();
  }

  public boolean isRemovable(){
    return removable;
  }

  public void setRemovable(boolean removable){
    this.removable = removable;
  }

  public String getPreviewText(){
    return "The quick brown fox jumped...";
  }

  public AttributeSet getPreviewStyle(){
    return getAttributeSet();
  }

  public int compareTo(Style other){
    return this.name.compareTo(other.name);
  }

  public boolean isValid(){
    return ((name != null) && (name.length() > 0) && (attributes != null));
  }

  public Object clone(){
    Style copy = new Style();
    copy.name = this.name;
    copy.removable = this.removable;
    copy.attributes = (SerializableAttributeSet) this.attributes.clone();
    return copy;
  }

  public boolean equals(Object obj){
    if(obj instanceof Style){
      Style other = (Style) obj;
      return other.getName().equals(this.getName()) &&
              other.getAttributeSet().equals(this.getAttributeSet());
    }
    else return super.equals(obj);
  }
}