package dao;

import java.io.File;

import java.util.List;
import ui.dialog.SetupDialogManageable;

public interface SetupDialogDAO{
  public List<SetupDialogManageable> importFromFile(File file) throws Exception;
  public void exportToFile(List<SetupDialogManageable> objects, File file) throws Exception;
  public String getSuggestedFileName();
}
