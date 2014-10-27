/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Logger;
import model.PetrukState;

/**
 *
 * @author anthony
 */
public class PetrukStateXMLDao implements ExceptionListener {

  protected static Logger logger = Logger.getLogger(PetrukStateXMLDao.class.getPackage().getName());

  public static PetrukState loadState(File file) throws Exception {
    Object loadedState = null;
    XMLDecoder d = null;
    try {
      d = new XMLDecoder(
              new BufferedInputStream(
              new FileInputStream(file)));
      try {
        PetrukState.loadState(d);
        //loadedState = d.readObject();
        //if (!_isValid(loadedState)) {
        //  logger.warning("Invalid read object!");
        //  throw new Exception("There was an error reading the file.");
        //}
      } catch (ArrayIndexOutOfBoundsException eofException) {
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
    return PetrukState.GetInstance();
  }

  public static void saveState(PetrukState state, File file) throws Exception {
    try {
      XMLEncoder e = new XMLEncoder(
              new BufferedOutputStream(
              new FileOutputStream(file)));
      e.setExceptionListener(new PetrukStateXMLDao());
      e.setPersistenceDelegate(PetrukState.class, new PetrukStatePersistenceDelegate());
      state.writeState(e);
      e.flush();
      e.close();
    } catch (FileNotFoundException fe) {
      fe.printStackTrace();
      throw new Exception("There was an error creating the new file.");
    }
  }

  private static boolean _isValid(Object loadedState) {
    return (loadedState != null) && (loadedState.getClass() == PetrukState.class);
  }

  public void exceptionThrown(Exception e) {
    logger.warning("Exception thrown!");
    e.printStackTrace();
  }

  static class PetrukStatePersistenceDelegate extends DefaultPersistenceDelegate {

    protected Expression instantiate(Object oldInstance, Encoder out) {
      //return super.instantiate(oldInstance, out);
      return new Expression(oldInstance, oldInstance.getClass(), "getInstance", null);
    }
  }
}
