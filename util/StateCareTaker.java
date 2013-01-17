/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.beans.BeanInfo;
import java.beans.Expression;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.Statement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.undo.StateEditable;

/**
 *
 * @author anthony
 */
public class StateCareTaker implements StateEditable {

  private Object target;
  private HashSet<PropertyDescriptor> properties;
  private static HashMap<Class<?>, HashSet<PropertyDescriptor>> classPropertiesCache =
          new HashMap<Class<?>, HashSet<PropertyDescriptor>>();
  private static Logger logger = Logger.getLogger(StateCareTaker.class.getPackage().getName());

  public StateCareTaker(Object target) {
    this.target = target;
    _initialize(target);
  }

  private void _initialize(Object target) {
    logger.info("Initializing StateCareTaker for " + target);
    if (classPropertiesCache.containsKey(target.getClass())) {
      this.properties = classPropertiesCache.get(target.getClass());
      logger.info("No need to introspect, class properties already cached");
    } else {
      logger.info("Introspecting class " + target.getClass().getName() + " for properties");
      this.properties = new HashSet<PropertyDescriptor>();
      try {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
          if (propertyDescriptor.getValue("transient") == null) {
            properties.add(propertyDescriptor);
            logger.info("Discovered property: " + propertyDescriptor.getName());
          }
        }
      } catch (IntrospectionException ie) {
        if (logger.isLoggable(Level.SEVERE)) {
          ie.printStackTrace();
        }
      }
      logger.info("caching properties for class " + target.getClass().getName());
      classPropertiesCache.put(target.getClass(), this.properties);
    }
  }

  public Object _getPropertyValue(PropertyDescriptor property) {
    Object value = null;
    Method readMethod = property.getReadMethod();
    Expression expression = new Expression(target, readMethod.getName(), null);
    try {
      value = expression.getValue();
    } catch (Exception e) {
      logger.severe(e.getMessage());
    }
    return value;
  }

  public void _setPropertyValue(PropertyDescriptor property, Object value) {
    Method writeMethod = property.getWriteMethod();
    Statement statement = new Statement(target, writeMethod.getName(), new Object[]{value});
    try {
      statement.execute();
    } catch (Exception e) {
      logger.severe(e.getMessage());
    }
  }

  public void restoreState(Hashtable<?, ?> state) {
    logger.info("In restoreState for object " + target.toString());
    _printDebug(state);

    for (Object key : state.keySet()) {
      _setPropertyValue((PropertyDescriptor) key, state.get(key));
    }
  }

  public void storeState(Hashtable<Object, Object> state) {
    logger.info("In storeState for object " + target.toString());
    for (PropertyDescriptor property : properties) {
      Object value = _getPropertyValue(property);
      if (value != null) {
        state.put(property, value);
      }
    }
  }

  private void _printDebug(Hashtable<?, ?> state) {
    logger.info("State of " + target.toString() + ": " + state.toString());
  }
}
