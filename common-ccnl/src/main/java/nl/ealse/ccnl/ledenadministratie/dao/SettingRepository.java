package nl.ealse.ccnl.ledenadministratie.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

public class SettingRepository extends BaseRepository<Setting> {
  
  public SettingRepository() {
    super(Setting.class);
  }

  @Override
  protected Object getPrimaryKey(Setting entity) {
    return entity.getId();
  }
  
  public  List<Setting> findByOrderBySettingsGroupAscKeyAsc() {
    return executeQuery("SELECT S FROM Setting S ORDER BY S.settingsGroup, S.key ASC");
  }

}
