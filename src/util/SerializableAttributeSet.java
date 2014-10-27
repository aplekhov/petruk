/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author anthony
 */
public class SerializableAttributeSet extends SimpleAttributeSet{
  private static HashMap<String, AttributeSerializer> attributeSerializers = _initializeSerializers();

  public Map<String,String> getAttributesAsMap(){
    HashMap<String, String> map = new HashMap<String,String>();
    for(String key: attributeSerializers.keySet()){
      String value = attributeSerializers.get(key).encode(this);
      if(value != null && value.length() > 0){
        map.put(key, value);
      }
    }
    return map;
  }

  public void setAttributesFromMap(Map<String,String> attributes){
    for(String key: attributes.keySet()){
      if(attributeSerializers.containsKey(key)){
        attributeSerializers.get(key).decode(this, attributes.get(key));
      }
    }
  }

  private static HashMap<String, AttributeSerializer> _initializeSerializers(){
    HashMap<String, AttributeSerializer> serializers = new HashMap<String, AttributeSerializer>();

    serializers.put("FontName", new AttributeSerializer(){
      public String encode(AttributeSet attributes){
        return StyleConstants.getFontFamily(attributes);
      }
      public void decode(MutableAttributeSet attributeSet, String attribute){
        StyleConstants.setFontFamily(attributeSet, attribute);
      }
    });

    serializers.put("FontSize", new AttributeSerializer(){
      public String encode(AttributeSet attributes){
        return Integer.toString(StyleConstants.getFontSize(attributes));
      }
      public void decode(MutableAttributeSet attributeSet, String attribute){
        StyleConstants.setFontSize(attributeSet, Integer.parseInt(attribute));
      }
    });

    serializers.put("FontColor", new AttributeSerializer(){
      public String encode(AttributeSet attributes){
        return Integer.toString(StyleConstants.getForeground(attributes).getRGB());
      }
      public void decode(MutableAttributeSet attributeSet, String attribute){
        StyleConstants.setForeground(attributeSet, new Color(Integer.parseInt(attribute)));
      }
    });

    serializers.put("isBold", new AttributeSerializer(){
      public String encode(AttributeSet attributes){
        return Boolean.toString(StyleConstants.isBold(attributes));
      }
      public void decode(MutableAttributeSet attributeSet, String attribute){
        StyleConstants.setBold(attributeSet, Boolean.valueOf(attribute));
      }
    });

    serializers.put("isItalic", new AttributeSerializer(){
      public String encode(AttributeSet attributes){
        return Boolean.toString(StyleConstants.isItalic(attributes));
      }
      public void decode(MutableAttributeSet attributeSet, String attribute){
        StyleConstants.setItalic(attributeSet, Boolean.valueOf(attribute));
      }
    });

    serializers.put("isUnderline", new AttributeSerializer(){
      public String encode(AttributeSet attributes){
        return Boolean.toString(StyleConstants.isUnderline(attributes));
      }
      public void decode(MutableAttributeSet attributeSet, String attribute){
        StyleConstants.setUnderline(attributeSet, Boolean.valueOf(attribute));
      }
    });

    return serializers;
  }


  interface AttributeSerializer{
    String encode(AttributeSet attributes);
    void decode(MutableAttributeSet attributeSet, String attribute);
  }
}
