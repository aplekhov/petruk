package ui.dialog;

import dao.SetupDialogDAO;
import java.awt.Frame;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import model.Style;

public class StylesSetupDialog extends AbstractSetupDialog<Style> {

  public static boolean showStylesSetupDialog(Frame frame, LinkedList<Style> styles, SetupDialogDAO dao, Color previewBackground) {
    StylesSetupDialog dialog = new StylesSetupDialog(frame, styles, dao, previewBackground);
    dialog.setVisible(true);
    return dialog.committed;
  }

  private StylesSetupDialog(Frame frame, LinkedList<Style> styles, SetupDialogDAO dao, Color previewBackground) {
    super(styles, dao, frame, "Setup Styles", previewBackground);
  }

  protected Style addAction() {
    return StyleDialog.showAddStyleDialog(this, previewBackground);
  }

  protected void editAction(Style object) {
    StyleDialog.showEditStyleDialog(this, object, previewBackground);
  }


  protected List<SetupDialogManageable> _selectObjectsToExport(Map<SetupDialogManageable, Boolean> objects){
    return ObjectSelectionDialog.showSelectionDialog(this, "Export Styles",
            "Select Styles to Export:", objects);
  }

  protected void _importObjects(List<SetupDialogManageable> objects){
    List<Style> currentStyles = listModel.getCurrentObjects();
    for(SetupDialogManageable object: objects){
      Style importedStyle = (Style) object;
      if(!currentStyles.contains(importedStyle)){
        listModel.insert(object);
      }
    }
  }

}
