package nl.ealse.ccnl.ledenadministratie.model.dao;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.PaymentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentFileRepository extends JpaRepository<PaymentFile, String> {

  List<PaymentFile> findAllByOrderByFileName();

}
