package nl.ealse.ccnl.ledenadministratie.dao;

import java.util.List;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;

public class PaymentFileRepository extends BaseRepository<PaymentFile> {
  @Getter
  private static PaymentFileRepository instance = new PaymentFileRepository();
  
  private PaymentFileRepository() {
    super(PaymentFile.class);
  }

  @Override
  protected Object getPrimaryKey(PaymentFile entity) {
     return entity.getFileName();
  }
  
  public List<PaymentFile> findAllByOrderByFileName() {
    return executeQuery("SELECT F FROM PaymentFile F ORDER BY F.fileName ASC");
  }

  public void deleteAll() {
    deleteAll(findAll());
  }

}
