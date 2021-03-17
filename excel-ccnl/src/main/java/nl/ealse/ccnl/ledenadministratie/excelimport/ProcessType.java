package nl.ealse.ccnl.ledenadministratie.excelimport;

public enum ProcessType {
  /**
   * Add in case of non existing content.
   */
  ADD, 
  
  /**
   * Add and overwrite content.
   * <ol>
   * <li>Add in case of non existing content</li>
   * <li>Overwrite existing content</li>
   * </ol>
   */
  ADD_OVERWRITE, 
  
  /**
   * Replace all content with the import-data.
   * <ol>
   * <li>Add in case of non existing content</li>
   * <li>Overwrite existing content</li>
   * <li>Remove non matching existing content</li>
   * </ol>
   */
  REPLACE;
}