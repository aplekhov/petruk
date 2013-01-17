package ui.editor;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.text.*;
import javax.swing.*;
import ui.ChangeListener;
import ui.editor.EditorPanel.StyleAdapter.MyStyleContext;

public class EditorPanel extends JPanel implements PetrukDisplay {

  private JTextPane editor;
  private JLabel statusBar;
  private DefaultStyledDocument document;
  private DefaultStyledDocument pinnedTextDocument;
  private JTextPane pinnedTextPanel;
  private JScrollPane pinnedTextScroller;
  private HashMap<model.Style, StyleAdapter> styleAdapters;
  private Keymap defaultKeyActions;
  private Keymap customKeyActions;
  private int lastBreakPosition;
  // TODO deprecate
  private AttributeSet lastTextStyle;
  private static Logger logger = Logger.getLogger(EditorPanel.class.getPackage().getName());

  public EditorPanel() {
    super(true);
    styleAdapters = new HashMap<model.Style, StyleAdapter>();
    _setupComponents();
  }

  public void setEditorBackground(Color backgroundColor) {
    editor.setBackground(backgroundColor);
    pinnedTextPanel.setBackground(backgroundColor);
    //pinnedTextPanel.setBackground(Color.RED);
  }

  public void setKeyBindings(Map<KeyStroke, Action> keyBindings) {
    customKeyActions.removeBindings();
    for (KeyStroke key : keyBindings.keySet()) {
      customKeyActions.addActionForKeyStroke(key, keyBindings.get(key));
      logger.info("Setting key binding " + key);
    }
  }

  public void setStatusBarText(String text) {
    statusBar.setText(text);
  }

  public boolean isPinnedTextPanelVisible() {
    return pinnedTextPanel.isVisible();
  }

  public void setPinnedTextPanelVisible(boolean isVisible) {
    Rectangle visibleRect = null;
    try {
      visibleRect = editor.modelToView(editor.getCaretPosition());
    } catch (BadLocationException e) {
      e.printStackTrace();
      visibleRect = editor.getVisibleRect();
    }

    pinnedTextPanel.setVisible(isVisible);
    visibleRect.width = editor.getWidth();
    visibleRect.height = editor.getHeight();
    editor.scrollRectToVisible(visibleRect);
    //pinnedTextPanel.setEnabled(isVisible);
    //editor.setPreferredSize(editor.getMaximumSize());
    //editor.invalidate();
    //this.invalidate();
    editor.grabFocus();
  }

  public void setPinnedText(String text) {
    pinnedTextPanel.setText(text);
    Dimension d = editor.getMaximumSize();
    logger.info("editor height:" + editor.getHeight());
    d.height = editor.getHeight();

    pinnedTextPanel.setPreferredSize(d);
    pinnedTextPanel.setMaximumSize(d);

    int delay = 400; //milliseconds
    ActionListener taskPerformer = new ActionListener() {

      public void actionPerformed(ActionEvent evt) {
        try {
          Dimension d = editor.getMaximumSize();
          d.height = editor.getHeight();

          Rectangle r = pinnedTextPanel.modelToView(pinnedTextDocument.getLength());
          d.height = r.height + r.y + 20;
          if (r != null) {
            logger.info("Height: " + d.height);
          } else {
            logger.info("rectangle null!");
          }

          // complete hack
          pinnedTextPanel.setVisible(false);
          pinnedTextPanel.setPreferredSize(d);
          pinnedTextPanel.setMaximumSize(d);
          pinnedTextPanel.setVisible(true);
        } catch (BadLocationException ble) {
          ble.printStackTrace();
          //d.height = 200;
        }
      }
    };
    Timer t = new Timer(delay, taskPerformer);
    t.setRepeats(false);
    t.start();

  }

  public void setPinnedTextStyle(model.Style attributes) {
    StyleAdapter adapter = _getAdapterForStyle(attributes);
    pinnedTextDocument.setLogicalStyle(0, adapter.swingStyle);
  }

  public boolean isInDialogue() {
    logger.info("Editor position: " + Integer.toString(editor.getCaretPosition()) +
            " last break position: " + Integer.toString(lastBreakPosition));
    if (lastBreakPosition != 0) {
      return (editor.getCaretPosition() != lastBreakPosition);
    } else {
      return (editor.getCaretPosition() > 0);
    }
  }

  public void insertText(String text) {
    try {
      document.insertString(editor.getCaretPosition(), text, null);
    } catch (Exception e) {
      logger.warning(e.getMessage());
    }
  }

  public void moveCaretBack(int length) {
    editor.setCaretPosition(editor.getCaretPosition() - length);
  }

  public void setCaretColor(Color color){
    editor.setCaretColor(color);
  }

  public void setCurrentTextStyle(model.Style attributes) {
    StyleAdapter adapter = _getAdapterForStyle(attributes);
    //lastTextStyle = attributes;
    document.setLogicalStyle(editor.getCaretPosition(), adapter.swingStyle);
  }

  public void convertToFullScreen(KeyStroke toggleKey, Action revertAction) {
    statusBar.setVisible(false);
    statusBar.setEnabled(false);
    defaultKeyActions.addActionForKeyStroke(toggleKey, revertAction);
    editor.grabFocus();
  }

  public void revertFromFullScreen(KeyStroke toggleKey) {
    statusBar.setEnabled(true);
    statusBar.setVisible(true);
    defaultKeyActions.removeKeyStrokeBinding(toggleKey);
    editor.grabFocus();
  }

  public String getFullText() {
    String fullText = "";
    try {
      fullText = document.getText(0, document.getLength());
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
    return fullText;
  }

  public void clearTextAndStyles() {
    logger.info("Clearing all text and styles");
    try {
      this.document.replace(0, document.getLength(), null, null);
      this.pinnedTextDocument.replace(0, pinnedTextDocument.getLength(), null, null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (StyleAdapter adapter : styleAdapters.values()) {
      document.removeStyle(adapter.swingStyle.getName());
      pinnedTextDocument.removeStyle(adapter.swingStyle.getName());
    }
  }

  public boolean hasData() {
    return (document.getLength() > 0 || pinnedTextDocument.getLength() > 0);
  }

  // TODO deprecate
  public void refreshTextSyle() {
    if (lastTextStyle != null) {
      //this.setCurrentTextStyle(lastTextStyle);
    }
  }

  private StyleAdapter _getAdapterForStyle(model.Style petrukStyle) {
    StyleAdapter adapter = styleAdapters.get(petrukStyle);
    if (adapter == null) {
      adapter = new StyleAdapter(petrukStyle);
    }
    return adapter;
  }

  private void _setupComponents() {
    // create the editor
    editor = new JTextPane();
    editor.setDragEnabled(true);
    editor.setBorder(null);
    defaultKeyActions = JTextPane.addKeymap("editor", editor.getKeymap());
    customKeyActions = JTextPane.addKeymap("custom", defaultKeyActions);
    defaultKeyActions.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            new DialogueBreakAction());
    editor.setKeymap(customKeyActions);


    // create the document
    document = new DefaultStyledDocument();
    editor.setStyledDocument(document);

    // create the status bar
    statusBar = new JLabel();

    // create the pinned text panel
    //pinnedTextPanel = new JTextArea(8, 100);
    pinnedTextPanel = new JTextPane();
    pinnedTextPanel.setEditable(false);
    //pinnedTextPanel.setBackground(Color.BLACK);
    //pinnedTextPanel.setForeground(Color.RED);
    //pinnedTextPanel.setFont(new Font("monospaced", Font.PLAIN, 14));
    pinnedTextPanel.setBorder(null);
    //System.out.println(pinnedTextPanel.getPreferredSize());

    // create the pinned text document model
    pinnedTextDocument = new DefaultStyledDocument();
    pinnedTextPanel.setDocument(pinnedTextDocument);

    // compose components
    setLayout(new BorderLayout());


    //pinnedTextScroller = new JScrollPane();
    //pinnedTextScroller.setBorder(null);
    //pinnedTextScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    //JViewport pinnedTextPort = pinnedTextScroller.getViewport();
    //pinnedTextPort.add(pinnedTextPanel);
    //pinnedTextPort.setBorder(null);
    //pinnedTextScroller.setViewportBorder(null);


    JScrollPane scroller = new JScrollPane();
    scroller.setBorder(null);
    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    JViewport port = scroller.getViewport();
    port.add(editor);
    port.setBorder(null);
    scroller.setViewportBorder(null);


    JPanel panel = new JPanel();
    panel.setBorder(null);
    panel.setLayout(new BorderLayout());
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
    editPanel.setBorder(null);


    editPanel.add(pinnedTextPanel);

    //pinnedTextScroller.setPreferredSize(new Dimension(scroller.getWidth(), 800));
    //pinnedTextScroller.setVisible(false);
    pinnedTextPanel.setVisible(false);
    //pinnedTextPanel.setMaximumSize(pinnedTextPanel.getMinimumSize());
    //editor.setPreferredSize(editor.getMaximumSize());
    //scroller.setPreferredSize(scroller.getMaximumSize());
    //pinnedTextScroller.setPreferredSize(new Dimension(60, 0));

    editPanel.add(scroller);
    panel.add("Center", editPanel);
    add("Center", panel);

    add("North", statusBar);
  }

  class StyleAdapter implements ChangeListener {

    public model.Style petrukStyle;
    public Style swingStyle;

    StyleAdapter(model.Style petrukStyle) {
      this.petrukStyle = petrukStyle;
      MyStyleContext myStyleContext = new MyStyleContext();
      this.swingStyle = myStyleContext.createStyle(petrukStyle.getName(), petrukStyle.getAttributeSet());
      petrukStyle.addChangeListener(this);
    }

    public void notifyOfChange() {
      this.swingStyle.addAttributes(petrukStyle.getAttributeSet());
    }

    class MyStyleContext extends StyleContext {

      public Style createStyle(String name, AttributeSet attributes) {
        StyleContext.NamedStyle style = new StyleContext.NamedStyle();
        style.addAttributes(attributes);
        style.setName(name);
        return style;
      }
    }
  }

  class DialogueBreakAction extends DefaultEditorKit.InsertBreakAction {

    public void actionPerformed(ActionEvent e) {
      super.actionPerformed(e);
      lastBreakPosition = EditorPanel.this.editor.getCaretPosition();
      logger.info("last break position: " + Integer.toString(lastBreakPosition));
    }
  }
}
