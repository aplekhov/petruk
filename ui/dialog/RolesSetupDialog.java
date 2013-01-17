package ui.dialog;

import java.awt.Frame;

import model.Role;
import dao.SetupDialogDAO;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import model.Style;

public class RolesSetupDialog extends AbstractSetupDialog<Role> {
  private List<Style> styles;

  public static boolean showRolesSetupDialog(Frame frame, LinkedList<Role> roles, SetupDialogDAO dao,
          Color previewBackground, List<Style> styles){
    RolesSetupDialog dialog = new RolesSetupDialog(frame, roles, dao, previewBackground, styles);
    dialog.setVisible(true);
    return dialog.committed;
  }

  private RolesSetupDialog(Frame frame, LinkedList<Role> roles, SetupDialogDAO dao,
          Color previewBackground, List<Style> styles) {
    super(roles, dao, frame, "Setup Roles", previewBackground);
    this.styles = styles;
  }

  protected Role addAction() {
    return RoleDialog.showAddRoleDialog(this, previewBackground, new Vector<Style>(styles));
  }

  protected void editAction(Role object) {
    RoleDialog.showEditRoleDialog(this, (Role) object, previewBackground, new Vector<Style>(styles));
  }


 protected List<SetupDialogManageable> _selectObjectsToExport(Map<SetupDialogManageable, Boolean> objects){
    return ObjectSelectionDialog.showSelectionDialog(this, "Export Roles",
            "Select Roles to Export:", objects);
  }

  protected void _importObjects(List<SetupDialogManageable> objects){
    for(SetupDialogManageable object: objects){
      Role importedRole = (Role) object;
      if(!styles.contains(importedRole.getStyle())){
        styles.add(importedRole.getStyle());
      }
      listModel.insert(object);
    }
  }
}
