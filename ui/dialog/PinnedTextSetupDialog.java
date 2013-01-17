package ui.dialog;

import dao.SetupDialogDAO;
import java.awt.Frame;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import model.PinnedText;
import model.Style;

public class PinnedTextSetupDialog extends AbstractSetupDialog<PinnedText> {
  private List<Style> styles;

  public static boolean showPinnedTextSetupDialog(Frame frame, LinkedList<PinnedText> pinnedTexts,
          SetupDialogDAO dao, Color previewBackground, List<Style> styles){
    PinnedTextSetupDialog dialog = new PinnedTextSetupDialog(frame, pinnedTexts, dao, previewBackground, styles);
    dialog.setVisible(true);
    return dialog.committed;
  }

  private PinnedTextSetupDialog(Frame frame, LinkedList<PinnedText> pinnedTexts,
          SetupDialogDAO dao, Color previewBackground, List<Style> styles) {
    super(pinnedTexts,dao,frame, "Setup Pinned Text", previewBackground);
    this.styles = styles;
  }

  protected PinnedText addAction() {
    return PinnedTextDialog.showAddPinnedTextDialog(this, previewBackground, new Vector<Style>(styles));
  }

  protected void editAction(PinnedText object) {
    PinnedTextDialog.showEditPinnedTextDialog(this, object, previewBackground, new Vector<Style>(styles));
  }

 protected List<SetupDialogManageable> _selectObjectsToExport(Map<SetupDialogManageable, Boolean> objects){
    return ObjectSelectionDialog.showSelectionDialog(this, "Export Pinned Text",
            "Select Pinned Text to Export:", objects);
  }

 protected void _importObjects(List<SetupDialogManageable> objects){
    for(SetupDialogManageable object: objects){
      PinnedText importedPinnedText = (PinnedText) object;
      if(!styles.contains(importedPinnedText.getStyle())){
        styles.add(importedPinnedText.getStyle());
      }
      listModel.insert(object);
    }
  }
}
