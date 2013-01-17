package ui.dialog;

import javax.swing.text.AttributeSet;
import model.EditableModel.NotifiableStateEdit;

public interface SetupDialogManageable<K extends Comparable<? super K>> extends Comparable<K>{

  public NotifiableStateEdit getStateEdit();

  public String getListValue();
  public boolean isRemovable();
  public boolean isValid();
  
  public String getPreviewText();
  public AttributeSet getPreviewStyle();
}
