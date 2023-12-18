package nl.ealse.ccnl.ledenadministratie.dao;

import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.model.Setting;

@Slf4j
public class SettingRepository extends BaseRepository<Setting> {
  @Getter
  private static SettingRepository instance = new SettingRepository();
  
  private SettingRepository() {
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
