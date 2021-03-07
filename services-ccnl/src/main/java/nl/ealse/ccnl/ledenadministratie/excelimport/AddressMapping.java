package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.util.StringJoiner;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLAdres;
import nl.ealse.ccnl.ledenadministratie.model.Address;

/**
 * Fills the Address from a CCNLLid.
 * 
 * @author ealse
 *
 */
public final class AddressMapping {

  private AddressMapping() {}

  public static final Address mapAddress(CCNLAdres adres) {
    Address address = new Address();
    address.setCity(adres.getPlaats());
    address.setPostalCode(adres.getPostcode());
    address.setCountry(adres.getLand());

    SplitHelper helper = new SplitHelper();
    helper.splitStreet(adres);

    address.setAddress(helper.street.toString());
    address.setAddressNumber(helper.number.toString());
    if (helper.appendix.length() > 0) {
      address.setAddressNumberAppendix(helper.appendix.toString());
    }
    return address;
  }

  private static class SplitHelper {
    StringJoiner street = new StringJoiner(" ");
    StringBuilder number = new StringBuilder();
    StringBuilder appendix = new StringBuilder();

    void splitStreet(CCNLAdres adres) {
      boolean bp = false;
      String[] parts = adres.getStraat().split("\\s+");
      for (int ix = 0; ix < parts.length; ix++) {
        bp = extracted(bp, parts, ix);
      }
    }

    private boolean extracted(boolean bp, String[] parts, int ix) {
      if (Character.isDigit(parts[ix].charAt(0))) {
        for (char ch : parts[ix].toCharArray()) {
          if (!bp && Character.isDigit(ch)) {
            number.append(ch);
          } else {
            bp = true;
            appendix.append(ch);
          }
        }
        for (int y = 0; y < ix; y++) {
          street.add(parts[y]);
        }
        for (int z = ix + 1; z < parts.length; z++) {
          appendix.append(" ").append(parts[z]);
        }
      }
      return bp;
    }


  }


}
