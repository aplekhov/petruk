package dao;

import java.beans.DefaultPersistenceDelegate;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.text.StyleConstants;
import ui.dialog.SetupDialogManageable;

public class SetupDialogXMLDAOFactory {

  protected static SetupDialogXMLDAOFactory instance;
  protected static Logger logger = Logger.getLogger(SetupDialogXMLDAOFactory.class.getPackage().getName());


  public static SetupDialogXMLDAOFactory Instance() {
    if (instance == null) {
      instance = new SetupDialogXMLDAOFactory();
    }
    return instance;
  }

  public SetupDialogDAO getDAO(Class type, String suggestedFilename) {
    return new GenericXMLDAO(type, suggestedFilename);
  }

  class GenericXMLDAO implements SetupDialogDAO, ExceptionListener {

    private String suggestedFilename;
    private Class clazz;

    GenericXMLDAO(Class clazz, String suggestedFilename){
      this.suggestedFilename = suggestedFilename;
      this.clazz = clazz;
    }

    public List<SetupDialogManageable> importFromFile(File file) throws Exception {
      Vector<SetupDialogManageable> readObjects = new Vector<SetupDialogManageable>();

      XMLDecoder d = null;
      try {
        d = new XMLDecoder(
                new BufferedInputStream(
                new FileInputStream(file)));
        while (true) {
          try {
            Object object = d.readObject();
            if (_isValid(object)) {
              readObjects.add((SetupDialogManageable) object);
            } else {
              logger.warning("Invalid read object!");
              throw new Exception("There was an error reading the file.");
            }
          } catch (ArrayIndexOutOfBoundsException eofException) {
            break;
          }
        }
        d.close();
      } catch (FileNotFoundException fnfe) {
        fnfe.printStackTrace();
        throw new Exception("There was an error reading or accessing the file.");
      } finally {
        if (d != null) {
          d.close();
        }
      }
      return new LinkedList<SetupDialogManageable>(readObjects);
    }

    public void exportToFile(List<SetupDialogManageable> objects, File file) throws Exception {
      try {
        XMLEncoder e = new XMLEncoder(
                new BufferedOutputStream(
                new FileOutputStream(file)));
        e.setExceptionListener(this);
        for (SetupDialogManageable object : objects) {
          e.writeObject(object);
        }

        //StyleConstants.isBold(null);
        //SimpleAttributeSet testSet = new SimpleAttributeSet();
        //testSet.addAttributes(PetrukState.GetInstance().getStyles().getFirst().getAttributeSet());
        //System.out.println(testSet.toString());
        //e.writeObject(testSet);
        e.flush();
        e.close();
      } catch (FileNotFoundException fe) {
        fe.printStackTrace();
        throw new Exception("There was an error creating the new file.");
      }
    }

    public String getSuggestedFileName() {
      return suggestedFilename;
    }

    public void exceptionThrown(Exception e){
      logger.warning("Exception thrown!");
      e.printStackTrace();
    }

    protected boolean _isValid(Object object) {
      return (object != null) && (object.getClass() == clazz);
    }
  }
}
