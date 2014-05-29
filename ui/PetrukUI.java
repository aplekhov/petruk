package ui;

import dao.PetrukStateXMLDao;
import dao.SetupDialogXMLDAOFactory;
import ui.editor.EditorPanel;
import ui.dialog.StylesSetupDialog;
import ui.dialog.RolesSetupDialog;
import ui.dialog.PinnedTextSetupDialog;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.logging.Logger;
import javax.swing.event.*;
import javax.swing.*;
import model.PetrukState;
import model.PinnedText;
import model.Role;
import model.Style;
import ui.action.PinnedTextTriggerAction;
import ui.action.RoleTriggerAction;
import ui.dialog.FileDialogs;
import ui.dialog.TriggerablesDialog;

public class PetrukUI extends JFrame {

  private EditorPanel editorPanel;
  private JMenuBar menuBar;
  private JMenuItem save_menu_item;
  private static AppExitAction exitAction = new AppExitAction();
  private static String DEFAULT_TITLE = "Petruk";
  private PetrukState petrukState;
  private static Logger logger = Logger.getLogger(PetrukUI.class.getPackage().getName());

  public PetrukUI(PetrukState petrukState) {
    // Force SwingSet to come up in the Cross Platform L&F
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exc) {
      System.err.println("Error loading L&F: " + exc);
    }
    this.petrukState = petrukState;

    _setInitialState();
  }

  protected void _setInitialState() {
    // TODO set title to include performance file name
    // SET INITIAL BACKGROUND COLOR
    this.setBackground(Color.lightGray);
    this.getContentPane().setLayout(new BorderLayout());
    editorPanel = new EditorPanel();
    editorPanel.setCurrentTextStyle(petrukState.getStyles().getFirst());
    editorPanel.setStatusBarText("current role: default");


    this.getContentPane().add("Center", editorPanel);
    menuBar = _createMenuBar();
    this.setJMenuBar(menuBar);
    this.addWindowListener(exitAction);
    this.pack();
    // TODO set to meaningful size
    this.setSize(500, 600);

    _updateUIFromState();
  }

  protected JMenuBar _createMenuBar() {
    JMenuBar menubar = new JMenuBar();

    JMenu file_menu = new JMenu("File");
    JMenu setup_menu = new JMenu("Setup");
    JMenu text_menu = new JMenu("Text");
    JMenu display_menu = new JMenu("Display");

    // file menu
    JMenuItem new_menu_item = new JMenuItem("New Performance Setup");
    JMenuItem open_menu_item = new JMenuItem("Open Performance Setup");
    save_menu_item = new JMenuItem("Save Performance Setup");
    JMenuItem save_as_menu_item = new JMenuItem("Save Performance Setup As");


    new_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _newAction();
      }
    });

    open_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _openAction();
      }
    });

    save_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _saveAction();
      }
    });

    save_as_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _saveAsAction();
      }
    });


    JMenuItem quit_menu_item = new JMenuItem("Quit");
    quit_menu_item.addActionListener(exitAction);

    // setup menu
    JMenuItem setup_styles_menu_item = new JMenuItem("Setup Styles");
    JMenuItem setup_roles_menu_item = new JMenuItem("Setup Roles");
    JMenuItem setup_pinned_text_menu_item = new JMenuItem("Setup Pinned Text");
    JMenuItem edit_hotkeys_menu_item = new JMenuItem("Edit Hotkeys");

    JMenuItem change_background_item = new JMenuItem("Change Background Color");
    JMenuItem change_caret_item = new JMenuItem("Change Caret Color");

    setup_styles_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        boolean result = StylesSetupDialog.showStylesSetupDialog(PetrukUI.this, petrukState.getStyles(),
                SetupDialogXMLDAOFactory.Instance().getDAO(Style.class, "styles.xml"),
                petrukState.getBackgroundColor());
        if (result == true) {
          petrukState.setModifiedSinceLastSave(true);
          _updateUIFromState();
        }
      }
    });

    setup_roles_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        boolean result = RolesSetupDialog.showRolesSetupDialog(PetrukUI.this, petrukState.getRoles(),
                SetupDialogXMLDAOFactory.Instance().getDAO(Role.class, "roles.xml"),
                petrukState.getBackgroundColor(), petrukState.getStyles());
        if (result == true) {
          petrukState.setModifiedSinceLastSave(true);
          _updateUIFromState();
        }
      }
    });

    setup_pinned_text_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        boolean result = PinnedTextSetupDialog.showPinnedTextSetupDialog(PetrukUI.this, petrukState.getPinnedText(),
                SetupDialogXMLDAOFactory.Instance().getDAO(PinnedText.class, "pinned_text.xml"),
                petrukState.getBackgroundColor(), petrukState.getStyles());
        if (result == true) {
          petrukState.setModifiedSinceLastSave(true);
          _updateUIFromState();
        }
      }
    });
    

    edit_hotkeys_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        boolean result = TriggerablesDialog.showDialog(PetrukUI.this, petrukState.getTriggerables());
        if (result == true) {
          petrukState.setModifiedSinceLastSave(true);
          _updateUIFromState();
        }
      }
    });

    change_background_item.addActionListener(new ChangeBackgroundColorAction());
    change_caret_item.addActionListener(new ChangeCaretColorAction());


    // text menu
    JMenuItem clear_text_menu_item = new JMenuItem("Clear Text");
    JMenuItem export_menu_item = new JMenuItem("Export As Script");
    clear_text_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _clearTextAction();
      }
    });

    export_menu_item.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        _exportAction();
      }
    });

    // Display menu
    JMenuItem go_fullscreen_menu_item = new JMenuItem("Go Fullscreen");
    go_fullscreen_menu_item.addActionListener(new ChangeFullScreenModeAction());

    file_menu.add(new_menu_item);
    file_menu.add(open_menu_item);
    file_menu.add(save_menu_item);
    file_menu.add(save_as_menu_item);
    file_menu.add(quit_menu_item);

    setup_menu.add(setup_styles_menu_item);
    setup_menu.add(setup_roles_menu_item);
    setup_menu.add(setup_pinned_text_menu_item);
    setup_menu.add(edit_hotkeys_menu_item);
    setup_menu.add(change_background_item);
    setup_menu.add(change_caret_item);

    text_menu.add(clear_text_menu_item);
    text_menu.add(export_menu_item);

    display_menu.add(go_fullscreen_menu_item);

    menubar.add(file_menu);
    menubar.add(setup_menu);
    menubar.add(text_menu);
    menubar.add(display_menu);

    return menubar;
  }

  protected void _updateUIFromState() {
    this.setTitle(_getTitle());
    this.save_menu_item.setEnabled(petrukState.isModifiedSinceLastSave());
    editorPanel.setEditorBackground(this.petrukState.getBackgroundColor());
    editorPanel.setCaretColor(petrukState.getCaretColor());
    Hashtable<KeyStroke, Action> keyBindings = new Hashtable<KeyStroke, Action>();
    for (Role role : petrukState.getRoles()) {
      if (role.getHotKey() != null) {
        keyBindings.put(role.getHotKey(), new RoleTriggerAction(role, editorPanel));
        logger.info("Adding trigger for " + role.getHotKey().toString());
      }
    }
    HashSet<PinnedTextTriggerAction> pinnedTextTriggers = new HashSet<PinnedTextTriggerAction>();
    for (PinnedText pinnedText : petrukState.getPinnedText()) {
      if (pinnedText.getHotKey() != null) {
        PinnedTextTriggerAction action = new PinnedTextTriggerAction(pinnedText, editorPanel);
        pinnedTextTriggers.add(action);
        keyBindings.put(pinnedText.getHotKey(), action);
        logger.info("Adding trigger for " + pinnedText.getHotKey().toString());
      }
    }
    // so ugly
    PinnedTextTriggerAction.setAllActions(pinnedTextTriggers);

    editorPanel.setKeyBindings(keyBindings);
    // TODO deprecate
    editorPanel.refreshTextSyle();
  }

  protected String _getTitle() {
    String title = DEFAULT_TITLE;
    title = title + " - " + petrukState.getSaveFile().getName();
    if (petrukState.isModifiedSinceLastSave()) {
      title = title + "* (Modified)";
    }
    return title;
  }

  protected void _newAction() {
    if (editorPanel.hasData()) {

      Object[] options = {"OK", "CANCEL"};
      int result = JOptionPane.showOptionDialog(this, "This will clear all the text that you have entered. Continue?", "Warning",
              JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
              null, options, options[0]);
      if (result != JOptionPane.OK_OPTION) {
        return;
      } else {
        editorPanel.clearTextAndStyles();
      }
    }

    // TODO refactor and make more DRY
    petrukState.setToDefaults();
    editorPanel.setCurrentTextStyle(petrukState.getStyles().getFirst());
    editorPanel.setStatusBarText("current role: default");

    _updateUIFromState();
  }

  protected void _openAction() {
    File openFile =
            FileDialogs.showOpenDialog(this, "Open Performance Setup", petrukState.getSaveFile());

    if (openFile != null) {
      try {
        PetrukState state = PetrukStateXMLDao.loadState(openFile);
        PetrukState.setState(state);
        petrukState = PetrukState.GetInstance();
        petrukState.setSaveFile(openFile);
        petrukState.setSaveFileChosen(true);
        petrukState.setModifiedSinceLastSave(false);
      } catch (Exception e) {
        e.printStackTrace();
        // TODO pop up error message
      }
    }
    _updateUIFromState();
  }

  protected void _saveAction() {
    if (!petrukState.isSaveFileChosen()) {
      _saveAsAction();
    } else {
      _save(petrukState.getSaveFile());
    }
  }

  protected void _saveAsAction() {
    File newFile =
            FileDialogs.showSaveDialog(this,
            "Save Performance Setup As", petrukState.getSaveFile());
    _save(newFile);
  }

  protected void _save(File saveFile) {
    if (saveFile != null) {
      try {
        PetrukStateXMLDao.saveState(petrukState, saveFile);

        petrukState.setSaveFile(saveFile);
        petrukState.setSaveFileChosen(true);
        petrukState.setModifiedSinceLastSave(false);
      } catch (Exception e) {
        e.printStackTrace();
        // TODO pop up error mesg
      }
    }
    _updateUIFromState();
  }

  protected void _clearTextAction(){
    if (editorPanel.hasData()) {
      Object[] options = {"OK", "CANCEL"};
      int result = JOptionPane.showOptionDialog(this, "This will clear all the text that you have entered. Continue?", "Warning",
              JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
              null, options, options[0]);
      if (result != JOptionPane.OK_OPTION) {
        return;
      } else {
        editorPanel.clearTextAndStyles();
      }
    }
  }

  protected void _exportAction() {
    File newFile =
            FileDialogs.showSaveDialog(this,
            "Export Script", new File(System.getProperty("user.dir"), "performance.txt"));
    if (newFile != null) {
      FileWriter output = null;
      try {
        output = new FileWriter(newFile);
        output.write(editorPanel.getFullText());
        output.flush();
      } catch (java.io.IOException ioe) {
        ioe.printStackTrace();
        // TODO popup error mesg
      } finally {
        if (output != null) {
          try {
            output.close();
          } catch (java.io.IOException ioe2) {
            ioe2.printStackTrace();
          }
        }
      }
    }
  }

  /**
   *  TODO: Hook for checking if save needed?
   */
  protected static final class AppExitAction extends WindowAdapter implements ActionListener {

    public void windowClosing(WindowEvent e) {
      _doExit();
    }

    public void actionPerformed(ActionEvent e) {
      _doExit();
    }

    protected void _doExit() {
      System.exit(0);
    }
  }

  protected class ChangeBackgroundColorAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
      Color chosen_color = JColorChooser.showDialog(PetrukUI.this, "Change Background Color",
              petrukState.getBackgroundColor());
      if (chosen_color != null) {
        petrukState.setBackgroundColor(chosen_color);
        petrukState.setModifiedSinceLastSave(true);
        _updateUIFromState();
      }
    }
  }

  protected class ChangeCaretColorAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
      Color chosen_color = JColorChooser.showDialog(PetrukUI.this, "Change Caret Color",
              petrukState.getCaretColor());
      if (chosen_color != null) {
        petrukState.setCaretColor(chosen_color);
        petrukState.setModifiedSinceLastSave(true);
        _updateUIFromState();
      }
    }
  }

  protected class ChangeFullScreenModeAction extends AbstractAction implements MenuListener {

    protected boolean isInFullScreenMode;
    protected Frame frame;

    private int oldScreenWidth;
    private int oldScreenHeight;

    public ChangeFullScreenModeAction() {
      this.isInFullScreenMode = false;
      this.frame = PetrukUI.this;
    }

    public void actionPerformed(ActionEvent e) {
      logger.info("full screen action triggered through actionlistener interface");
      _doChange();
    }

    public void menuSelected(MenuEvent e) {
      logger.info("full screen action triggered through menulistener interface");
      _doChange();
    }

    public void menuCanceled(MenuEvent e) {
    }

    public void menuDeselected(MenuEvent e) {
    }

    protected void _doChange() {
      logger.info("performing full screen change action!");
      KeyStroke esc_key = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice gd = ge.getDefaultScreenDevice();

      if (!gd.isFullScreenSupported()){
        logger.info("full screen is not supported!");
        return;
      }

      if (!isInFullScreenMode) {
        menuBar.setVisible(false);
        menuBar.setEnabled(false);

        oldScreenWidth = frame.getWidth();
        oldScreenHeight = frame.getHeight();
        frame.setVisible(false);
        frame.dispose();
        frame.setUndecorated(true);

                
        
        
        //Window w  = gd.getFullScreenWindow();
        //gd.setFullScreenWindow(frame);
        //DisplayMode dm = gd.getDisplayMode();
        //frame.setSize(dm.getWidth(), dm.getHeight());

        /*Toolkit tk = Toolkit.getDefaultToolkit();
        frame.setLocation(0, 0);
        frame.setBounds(new Rectangle(new Point(0, 0), tk.getScreenSize()));
        frame.setSize(tk.getScreenSize());
        frame.setResizable(false);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        //frame.pack();
        */
        gd.setFullScreenWindow(frame);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setEnabled(true);
        frame.toFront();
        editorPanel.convertToFullScreen(esc_key, this);

        isInFullScreenMode = true;
      } else {
        frame.setVisible(false);
        frame.dispose();
        frame.setSize(oldScreenWidth, oldScreenHeight);
        frame.setLocation(0, 0);
        //Toolkit tk = Toolkit.getDefaultToolkit();
        //frame.setBounds(new Rectangle(new Point(0, 0), tk.getScreenSize()));
        //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice gd = ge.getDefaultScreenDevice();
        //gd.setFullScreenWindow(null);
        menuBar.setVisible(true);
        menuBar.setEnabled(true);
        frame.setUndecorated(false);
        frame.setExtendedState(Frame.NORMAL);
        //frame.pack();
        gd.setFullScreenWindow(null);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.toFront();
        frame.setEnabled(true);
        editorPanel.revertFromFullScreen(esc_key);
        isInFullScreenMode = false;
      }
    }
  }
}
