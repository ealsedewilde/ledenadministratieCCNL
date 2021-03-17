package nl.ealse.ccnl.ledenadministratie.excelimport;

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

    address.setAddress(helper.street);
    address.setAddressNumber(helper.number.toString());
    if (helper.appendix != null) {
      address.setAddressNumberAppendix(helper.appendix);
    }
    return address;
  }

  private static class SplitHelper {
    String street = null;
    StringBuilder number = new StringBuilder();
    String appendix = null;

    void splitStreet(CCNLAdres adres) {
      String[] parts = adres.getStraat().split("[\\s.]+");
      int px = 0;
      while (number.length() == 0 && px < parts.length) {
        extractNumber(parts[px++]);
      }
      int ix = adres.getStraat().indexOf(number.toString());
      if (ix > 0) {
        street = adres.getStraat().substring(0, ix);
        ix = ix + number.length();
        int l = adres.getStraat().length();
        if (l > ix) {
          appendix = adres.getStraat().substring(ix);
        }
      } else {
        street = adres.getStraat();
      }
    }

    private void extractNumber(String part) {
      if (Character.isDigit(part.charAt(0))) {
        for (char ch : part.toCharArray()) {
          if (Character.isDigit(ch)) {
            number.append(ch);
          }
        }
      }
     }


  }


}
