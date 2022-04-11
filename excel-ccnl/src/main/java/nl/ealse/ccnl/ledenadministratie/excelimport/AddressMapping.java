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

    address.setStreet(helper.street);
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
      String straat = adres.getStraat().trim();
      String[] parts = straat.split("[\\s.]+");
      int px = 0;
      while (number.length() == 0 && px < parts.length) {
        extractNumber(parts[px++]);
      }
      int ix = straat.indexOf(number.toString());
      if (ix > 0) {
        street = straat.substring(0, ix);
        ix = ix + number.length();
        int l = straat.length();
        if (l > ix) {
          appendix = straat.substring(ix);
        }
      } else {
        street = straat.substring(number.length()).trim();
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
