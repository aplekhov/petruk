/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import ui.Triggerable;

/**
 *
 * @author anthony
 */
public class PetrukState {
  private static Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
  private static Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;

  private static Style DEFAULT_STYLE = new Style();
  private static Role DEFAULT_ROLE = new Role();

  protected static Logger logger = Logger.getLogger(PetrukState.class.getPackage().getName());



  static{
    DEFAULT_STYLE.setName("_default style");
    DEFAULT_STYLE.setRemovable(false);
    MutableAttributeSet attributes = DEFAULT_STYLE.getAttributeSet();
    StyleConstants.setForeground(attributes, DEFAULT_FOREGROUND_COLOR);
    StyleConstants.setFontSize(attributes, 14);
    StyleConstants.setFontFamily(attributes,
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0]);

    DEFAULT_ROLE.setName("_default role");
    DEFAULT_ROLE.setStyle(DEFAULT_STYLE);
    DEFAULT_ROLE.setRemovable(false);
    DEFAULT_ROLE.setNameDisplayed(false);
    DEFAULT_ROLE.setPromptDisplayed(false);
  }

  private LinkedList<Style> styles;
  private LinkedList<Role> roles;
  private LinkedList<PinnedText> pinnedText;
  private Color backgroundColor;
  private Color caretColor;
  private File saveFile;
  private boolean modifiedSinceLastSave;
  private boolean saveFileChosen;
  private static transient PetrukState instance;

  public static PetrukState GetInstance(){
    if(instance == null){
      instance = new PetrukState();
    }
    return instance;
  }

  public static PetrukState getInstance(){
    return GetInstance();
  }

  private PetrukState(){
    setToDefaults();
  }

  // Fucking bullshit
  public static void writeState(XMLEncoder e){
    // super ugly hack, this whole method needs to be externalized
    PetrukState savedInstance = PetrukState.instance;
    PetrukState.instance = null;
    try{
    e.writeObject(savedInstance);
    }catch(Exception ex){
      ex.printStackTrace();
      e.flush();
    }
    PetrukState.instance = savedInstance;
  }

  // more Fucking bullshit
  public static void loadState(XMLDecoder d) throws Exception{
    // super ugly hack, this whole method needs to be externalized
    PetrukState savedInstance = PetrukState.instance;
    PetrukState.instance = null;
    try {
        Object loadedState = d.readObject();
        if ((loadedState == null) || (loadedState.getClass() != PetrukState.class)){
          logger.warning("Invalid read object!");
          PetrukState.instance = savedInstance;
          throw new Exception("There was an error reading the file.");
        }
      } catch (ArrayIndexOutOfBoundsException eofException) {
      }
  }


  public static void setState(PetrukState state){
    // super ugly hack, this whole method needs to be externalized
    PetrukState.instance = state;
  }

  public void setToDefaults(){
    styles = new LinkedList<Style>();
    roles = new LinkedList<Role>();
    pinnedText = new LinkedList<PinnedText>();
    backgroundColor = DEFAULT_BACKGROUND_COLOR;
    caretColor = DEFAULT_FOREGROUND_COLOR;
    styles.add((Style) DEFAULT_STYLE.clone());
    roles.add((Role) DEFAULT_ROLE.clone());

    saveFile = new File(System.getProperty("user.dir"), "untitled.ptk");
    modifiedSinceLastSave = true;
    saveFileChosen = false;
  }

  public String getBackgroundColorString(){
    return Integer.toString(backgroundColor.getRGB());
  }

  public void setBackgroundColorString(String backgroundColorString){
    backgroundColor = new Color(Integer.parseInt(backgroundColorString));
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color backgroundColor){
    this.backgroundColor = backgroundColor;
  }

  public String getCaretColorString(){
    return Integer.toString(caretColor.getRGB());
  }

  public void setCaretColorString(String caretColorString){
    caretColor = new Color(Integer.parseInt(caretColorString));
  }

  public Color getCaretColor() {
    return caretColor;
  }

  public void setCaretColor(Color caretColor){
    this.caretColor = caretColor;
  }

  public LinkedList<PinnedText> getPinnedText() {
    return pinnedText;
  }

  public void setPinnedText(LinkedList<PinnedText> pinnedText){
    this.pinnedText = pinnedText;
  }

  public LinkedList<Role> getRoles() {
    return roles;
  }

  public void setRoles(LinkedList<Role> roles){
    this.roles = roles;
  }

  public LinkedList<Style> getStyles() {
    return styles;
  }

  public void setStyles(LinkedList<Style> styles){
    this.styles = styles;
  }

  public Vector<Style> getStylesVector(){
    return new Vector(getStyles());
  }

  public LinkedList<Triggerable> getTriggerables(){
    LinkedList<Triggerable> triggerables = new LinkedList<Triggerable>();
    triggerables.addAll(roles);
    triggerables.addAll(pinnedText);
    return triggerables;
  }

  public File getSaveFile(){
    return saveFile;
  }

  public void setSaveFile(File saveFile){
    this.saveFile = saveFile;
  }

  public boolean isModifiedSinceLastSave(){
    return modifiedSinceLastSave;
  }

  public void setModifiedSinceLastSave(boolean modifiedSinceLastSave){
    this.modifiedSinceLastSave = modifiedSinceLastSave;
  }

  public boolean isSaveFileChosen(){
    return saveFileChosen;
  }

  public void setSaveFileChosen(boolean saveFileChosen){
    this.saveFileChosen = saveFileChosen;
  }
}
