/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StyleDialog.java
 *
 * Created on Aug 27, 2009, 2:28:42 PM
 */
package ui.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import model.Style;

/**
 *
 * @author anthony
 */
class StyleDialog extends AbstractDetailDialog {
  private static final Color DEFAULT_FONT_COLOR = Color.WHITE;
  protected Style style;

  public static Style showAddStyleDialog(Dialog parent, Color backgroundColor){
    StyleDialog dialog  = new StyleDialog(parent, "Add New Style", null, backgroundColor);
    dialog.setVisible(true);
    return (Style) dialog._getFinalObject();
  }

  public static void showEditStyleDialog(Dialog parent, Style style, Color backgroundColor){
    StyleDialog dialog  = new StyleDialog(parent, "Edit Style", style, backgroundColor);
    dialog.setVisible(true);
  }

  /** Creates new form StyleDialog */
  private StyleDialog(Dialog parent, String title, Style style, Color backgroundColor) {
    super(parent, title, backgroundColor);

    // TODO this is ugly, figure out a  better way
    if(style == null){
      this.style = new Style();
      this.style.setRemovable(true);
      StyleConstants.setForeground(this.style.getAttributeSet(), DEFAULT_FONT_COLOR);
    }
    else{
      this.style = style;
    }

    _setObject(this.style);

    _setupComponents();
  }

  
  private void _setupComponents() {
    // setup lists
    fontNameList.setListData(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
    Vector<String> fontSizes = new Vector<String>();
    for (int i = 6; i <= 72; i++) {
      fontSizes.add(Integer.toString(i));
    }
    fontSizeList.setListData(fontSizes);


    // setup buttons
    setColorButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // TODO initialize dialog with current color or default
        Color chosen_color = JColorChooser.showDialog(StyleDialog.this, "Change Font Color", 
                StyleConstants.getForeground(style.getAttributeSet()));
        if (chosen_color != null) {
          previewPane.setForeground(chosen_color);
          _processUIChange();
        }
      }
    });
    _registerCommitButton(okButton);
    _registerCancelButton(cancelButton);

    // initialize with data
    // TODO: Check threading here!  UI might get set which triggers events
    // that might not get dispatched until after listeners get added!
    _updateUIFromModel();
    _processUIChange();

    // setup change listener for UI components
    nameField.addCaretListener(this);
    fontNameList.addListSelectionListener(this);
    fontSizeList.addListSelectionListener(this);
    boldField.addActionListener(this);
    italicField.addActionListener(this);
    underlineField.addActionListener(this);
  }
  
  // Methods that implement AbstractDetailDialog

  protected void _createComponents(){
    initComponents();
  }

  protected JTextPane _getPreviewPane() {
    return previewPane;
  }



  protected void _onValid() {
    okButton.setEnabled(true);
  }

  protected void _onInvalid() {
    okButton.setEnabled(false);
  }

  protected void _updateModelFromUI(){
    style.setName(nameField.getText());

    // get style from ui
    String fontName = (String) fontNameList.getSelectedValue();
    int fontSize = Integer.parseInt((String) fontSizeList.getSelectedValue());

    MutableAttributeSet attributes = style.getAttributeSet();

    StyleConstants.setFontFamily(attributes, fontName);
    StyleConstants.setFontSize(attributes, fontSize);
    StyleConstants.setBold(attributes, boldField.isSelected());
    StyleConstants.setItalic(attributes, italicField.isSelected());
    StyleConstants.setUnderline(attributes, underlineField.isSelected());
    StyleConstants.setForeground(attributes, previewPane.getForeground());
  }

  protected void _updateUIFromModel(){
    // TODO be careful with null values!
    nameField.setText(style.getName());
    MutableAttributeSet attributes = style.getAttributeSet();
    String fontName = StyleConstants.getFontFamily(attributes);
    int fontSize = StyleConstants.getFontSize(attributes);
    fontNameList.setSelectedValue(fontName, true);
    fontSizeList.setSelectedValue(Integer.toString(fontSize), true);
    boldField.setSelected(StyleConstants.isBold(attributes));
    italicField.setSelected(StyleConstants.isItalic(attributes));
    underlineField.setSelected(StyleConstants.isUnderline(attributes));
    previewPane.setForeground(StyleConstants.getForeground(attributes));

    // set lists to defaults if necessary
    for(JList list:new JList[]{fontNameList, fontSizeList}){
      if(list.getSelectedIndex() == -1){
        list.setSelectedIndex(0);
      }
    }
  }

  //

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jScrollPane4 = new javax.swing.JScrollPane();
    fontSizeList = new javax.swing.JList();
    jScrollPane3 = new javax.swing.JScrollPane();
    fontNameList = new javax.swing.JList();
    setColorButton = new javax.swing.JButton();
    underlineField = new javax.swing.JCheckBox();
    italicField = new javax.swing.JCheckBox();
    boldField = new javax.swing.JCheckBox();
    jScrollPane5 = new javax.swing.JScrollPane();
    previewPane = new javax.swing.JTextPane();
    cancelButton = new javax.swing.JButton();
    okButton = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    nameField = new javax.swing.JTextField();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    jLabel5.setText("Font:");

    jLabel6.setText("Size:");

    fontSizeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane4.setViewportView(fontSizeList);

    fontNameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane3.setViewportView(fontNameList);

    setColorButton.setText("Set Color");

    underlineField.setText("underline");

    italicField.setText("italic");

    boldField.setText("bold");

    previewPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));
    jScrollPane5.setViewportView(previewPane);

    cancelButton.setText("Cancel");

    okButton.setText("OK");

    jLabel1.setText("Name:");

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(layout.createSequentialGroup()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(jLabel5)
              .add(layout.createSequentialGroup()
                .add(boldField)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(italicField))
              .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(underlineField)
              .add(layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                  .add(jLabel6)
                  .add(setColorButton)
                  .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(okButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(cancelButton))
          .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 232, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(layout.createSequentialGroup()
            .add(jLabel1)
            .add(33, 33, 33)
            .add(nameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel1)
          .add(nameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(23, 23, 23)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel5)
          .add(jLabel6))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(setColorButton))
          .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 144, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(boldField)
          .add(italicField)
          .add(underlineField))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .add(12, 12, 12)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(cancelButton)
          .add(okButton))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox boldField;
  private javax.swing.JButton cancelButton;
  private javax.swing.JList fontNameList;
  private javax.swing.JList fontSizeList;
  private javax.swing.JCheckBox italicField;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JTextField nameField;
  private javax.swing.JButton okButton;
  private javax.swing.JTextPane previewPane;
  private javax.swing.JButton setColorButton;
  private javax.swing.JCheckBox underlineField;
  // End of variables declaration//GEN-END:variables
}
