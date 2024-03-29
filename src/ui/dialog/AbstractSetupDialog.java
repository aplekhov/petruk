/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AbstractSetupDialog.java
 *
 * Created on Aug 11, 2009, 12:46:18 AM
 */
package ui.dialog;

import java.util.LinkedList;
import java.util.Collections;
import java.awt.Frame;
import javax.swing.*;

import dao.SetupDialogDAO;
import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.undo.StateEdit;
import model.EditableModel.NotifiableStateEdit;

/**
 *
 * @author anthony
 */
public abstract class AbstractSetupDialog<T extends SetupDialogManageable> extends javax.swing.JDialog {

  AbstractSetupDialog(LinkedList<T> objectsToSetup, SetupDialogDAO dao, Frame frame, String title, Color previewBackground) {
    super(frame, title, true);
    this.setResizable(false);
    this.dao = dao;
    this.previewBackground = previewBackground;
    this.listModel = new CustomListModel(objectsToSetup);
    this.committed = false;

    initComponents();
    _setupComponents();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    objectList = new javax.swing.JList();
    addButton = new javax.swing.JButton();
    editButton = new javax.swing.JButton();
    deleteButton = new javax.swing.JButton();
    importButton = new javax.swing.JButton();
    exportButton = new javax.swing.JButton();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    jScrollPane2 = new javax.swing.JScrollPane();
    previewPane = new javax.swing.JTextPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    objectList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jScrollPane1.setViewportView(objectList);

    addButton.setText("Add");

    editButton.setText("Edit");

    deleteButton.setText("Delete");

    importButton.setText("Import from File");

    exportButton.setText("Export to File");

    okButton.setText("OK");

    cancelButton.setText("Cancel");

    jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));

    previewPane.setBackground(new java.awt.Color(1, 1, 1));
    previewPane.setBorder(null);
    previewPane.setForeground(new java.awt.Color(253, 251, 251));
    jScrollPane2.setViewportView(previewPane);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
          .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(editButton)
          .add(layout.createSequentialGroup()
            .add(31, 31, 31)
            .add(okButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(cancelButton))
          .add(exportButton)
          .add(importButton)
          .add(deleteButton)
          .add(addButton))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(layout.createSequentialGroup()
            .add(addButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(editButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(deleteButton))
          .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
          .add(layout.createSequentialGroup()
            .add(7, 7, 7)
            .add(importButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(exportButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 46, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
              .add(cancelButton)
              .add(okButton)))
          .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void _setupComponents() {
    // setup list
    objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    objectList.setCellRenderer(new ListRenderer());
    objectList.setModel(listModel);
    ListChangeListener listChangeListener = new ListChangeListener();
    objectList.addListSelectionListener(listChangeListener);
    listModel.addListDataListener(listChangeListener);
    listModel.addListDataListener(null);
    if (listModel.getSize() > 0) {
      objectList.setSelectedIndex(0);
    }

    // setup background
    previewPane.setBackground(previewBackground);

    // setup buttons
    SetupDialogManageable selectedObject = (SetupDialogManageable) objectList.getSelectedValue();
    _updateEditDeleteButtonStates(selectedObject);
    _updateExportButtonState();

    // setup action listeners
    addButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _addButtonActionPerformed();
      }
    });

    deleteButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _deleteButtonActionPerformed();
      }
    });
    editButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _editButtonActionPerformed();
      }
    });

    importButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _importButtonActionPerformed();
      }
    });

    exportButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _exportButtonActionPerformed();
      }
    });

    okButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _okButtonActionPerformed();
      }
    });

    cancelButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _cancelButtonActionPerformed();
      }
    });
  }

  protected abstract T addAction();

  protected abstract void editAction(T object);

  private void _addButtonActionPerformed() {
    T newObject = addAction();
    if (newObject != null) {
      listModel.insert(newObject);
      objectList.setSelectedValue(newObject, true);
      _refreshPreview(newObject);
    }
  }

  private void _editButtonActionPerformed() {
    T objectToEdit = (T) objectList.getSelectedValue();
    if (objectToEdit != null) {
      editAction(objectToEdit);
      listModel.sort();
      _refreshPreview(objectToEdit);
    }
  }

  private void _deleteButtonActionPerformed() {
    T objectToDelete = (T) objectList.getSelectedValue();
    if (objectToDelete != null) {
      listModel.remove(objectToDelete);
    }
  }

  private void _importButtonActionPerformed() {
    File importFile =
            FileDialogs.showOpenDialog(this,
            "Import", new File(System.getProperty("user.dir"), dao.getSuggestedFileName()));
    if (importFile != null) {
      List<SetupDialogManageable> importedObjects = null;
      try {
        importedObjects = dao.importFromFile(importFile);
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }

      if ((importedObjects != null) && (!importedObjects.isEmpty())) {
        _importObjects(_selectObjectsToImport(importedObjects));
      }
    }
  }

  protected Map<SetupDialogManageable, Boolean> _prepareObjectsToExport() {
    HashMap<SetupDialogManageable, Boolean> objectsToExport =
            new HashMap<SetupDialogManageable, Boolean>(this.listModel.getSize());
    for (SetupDialogManageable object : (List<SetupDialogManageable>) this.listModel.getCurrentObjects()) {
      objectsToExport.put(object, Boolean.valueOf(object.isRemovable()));
    }
    return objectsToExport;
  }

  protected void _importObjects(List<SetupDialogManageable> objects){
    // NADA, subclasses implement
  }

  protected List<SetupDialogManageable> _selectObjectsToExport(Map<SetupDialogManageable, Boolean> objects) {
    return ObjectSelectionDialog.showSelectionDialog(this, "Select Objects to Export",
            "Select Objects to Export:", objects);
  }

  protected List<SetupDialogManageable> _selectObjectsToImport(List<SetupDialogManageable> objects) {
    HashMap<SetupDialogManageable, Boolean> objectMap = new HashMap<SetupDialogManageable, Boolean>();
    for (SetupDialogManageable object : objects) {
      objectMap.put(object, Boolean.FALSE);
    }
    return ObjectSelectionDialog.showSelectionDialog(this, "Select Objects to Import",
            "Select Objects to Import:", objectMap);
  }

  private void _exportButtonActionPerformed() {
    File newFile =
            FileDialogs.showSaveDialog(this,
            "Export", new File(System.getProperty("user.dir"), dao.getSuggestedFileName()));
    if (newFile != null) {
      List<SetupDialogManageable> objectsToExport = _selectObjectsToExport(_prepareObjectsToExport());
      if (objectsToExport != null && !objectsToExport.isEmpty()) {
        try {
          dao.exportToFile(objectsToExport, newFile);
        } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }

  private void _okButtonActionPerformed() {
    committed = true;
    listModel.commit();
    _closeDialog();
  }

  private void _cancelButtonActionPerformed() {
    // TODO clear out list model?
    committed = false;
    listModel.rollback();
    _closeDialog();
  }

  private void _closeDialog() {
    dispose();
  }

  protected void _refreshPreview(SetupDialogManageable selectedObject) {
    if (selectedObject != null) {
      previewPane.setCharacterAttributes(selectedObject.getPreviewStyle(), true);
      previewPane.setText(selectedObject.getPreviewText());
    } else {
      previewPane.setText(null);
    }
  }

  private void _updateEditDeleteButtonStates(SetupDialogManageable selectedObject) {
    if (selectedObject != null) {
      editButton.setEnabled(true);
      deleteButton.setEnabled(selectedObject.isRemovable());
    } else {
      editButton.setEnabled(false);
      deleteButton.setEnabled(false);
    }
  }

  private void _updateExportButtonState() {
    if (listModel.getSize() > 0) {
      exportButton.setEnabled(true);
    } else {
      exportButton.setEnabled(false);
    }
  }

  public boolean wasCommitted() {
    return committed;
  }

  class CustomListModel<T extends SetupDialogManageable> extends AbstractListModel {

    private LinkedList<T> originalList;
    private LinkedList<T> managedObjects;
    private LinkedList<T> objectsToAdd;
    private LinkedList<T> objectsToDelete;
    private LinkedList<NotifiableStateEdit> originalStates;

    CustomListModel(LinkedList<T> objectsToSetup) {
      this.originalList = objectsToSetup;
      // clone original list
      this.managedObjects = (LinkedList<T>) this.originalList.clone();
      this.objectsToAdd = new LinkedList<T>();
      this.objectsToDelete = new LinkedList<T>();
      this.originalStates = new LinkedList<NotifiableStateEdit>();

      for (T obj : originalList) {
        this.originalStates.add(obj.getStateEdit());
      }
      sort();
    }

    public void commit() {
      for (T obj : objectsToAdd) {
        originalList.add(obj);
      }
      for (T obj : objectsToDelete) {
        originalList.remove(obj);
      }

      for (NotifiableStateEdit originalState : originalStates) {
        originalState.endWithNotify();
      }
    }

    public void rollback() {
      for (StateEdit originalState : originalStates) {
        originalState.end();
        originalState.undo();
      }
    }

    public void sort() {
      Collections.sort(managedObjects);
      fireContentsChanged(this, 0, managedObjects.size() - 1);
    }

    public Object getElementAt(int index) {
      return managedObjects.get(index);
    }

    public int getSize() {
      return managedObjects.size();
    }

    public void insert(T newObject) {
      managedObjects.add(newObject);
      objectsToAdd.add(newObject);
      int newIndex = managedObjects.size() - 1;
      fireIntervalAdded(this, newIndex, newIndex);
      sort();
    }

    public void replace(T oldObject, T newObject) {
      int oldIndex = managedObjects.indexOf(oldObject);
      managedObjects.set(oldIndex, newObject);
      fireContentsChanged(this, oldIndex, oldIndex);
      sort();
    }

    public void remove(T object) {
      int oldIndex = managedObjects.indexOf(object);
      managedObjects.remove(oldIndex);
      objectsToDelete.add(object);
      fireIntervalRemoved(this, oldIndex, oldIndex);
    }

    public List<T> getCurrentObjects() {
      return managedObjects;
    }
  }

  class ListChangeListener implements ListSelectionListener, ListDataListener {

    public void contentsChanged(ListDataEvent e) {
      _updateExportButtonState();
    }

    public void intervalAdded(ListDataEvent e) {
      _updateExportButtonState();
    }

    public void intervalRemoved(ListDataEvent e) {
      _updateExportButtonState();
    }

    public void valueChanged(ListSelectionEvent e) {
      SetupDialogManageable selectedObject =
              (SetupDialogManageable) objectList.getSelectedValue();
      _updateEditDeleteButtonStates(selectedObject);
      _refreshPreview(selectedObject);
    }
  }
  protected CustomListModel listModel;
  protected SetupDialogDAO dao;
  protected String title;
  protected Color previewBackground;
  protected boolean committed;
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton addButton;
  private javax.swing.JButton cancelButton;
  private javax.swing.JButton deleteButton;
  private javax.swing.JButton editButton;
  private javax.swing.JButton exportButton;
  private javax.swing.JButton importButton;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JList objectList;
  private javax.swing.JButton okButton;
  private javax.swing.JTextPane previewPane;
  // End of variables declaration//GEN-END:variables
}
