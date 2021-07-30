package nl.ealse.ccnl.ledenadministratie.excelimport;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
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

    address.setAddress(helper.street.toString().trim());
    address.setAddressNumber(helper.number);
    if (helper.appendix.length() > 0) {
      address.setAddressNumberAppendix(helper.appendix.toString());
    }

    return address;
  }

  private static class SplitHelper {
    StringBuilder street = new StringBuilder();
    String number = null;
    StringBuilder appendix = new StringBuilder();

    private AddressPart currentPart;

    private final List<AddressPart> parts = new ArrayList<>();


    void splitStreet(CCNLAdres adres) {
      for (char c : adres.getStraat().trim().toCharArray()) {
        if (Character.isDigit(c)) {
          handleCaracter(c, PartType.NUMBER);
        } else if (c == ' ') {
          handleCaracter(c, PartType.SPACE);
        } else {
          handleCaracter(c, PartType.OTHER);
        }
      }
      parts.add(currentPart);
      
      int mainPartIx = findMainPart();
      int ix = 0;
      for (AddressPart part : parts) {
        if (ix <= mainPartIx) {
          street.append(part.getContent().toString());
        } else if (number == null) {
          if (part.getType() == PartType.NUMBER) {
            number = part.getContent().toString();
          } else {
            street.append(part.getContent().toString());
          }
        } else {
          appendix.append(part.getContent().toString());
        }
        ix++;
      }
    }
    
    private void handleCaracter(char c, PartType type) {
      if (currentPart == null) {
        currentPart = new AddressPart(type);
      } else if (currentPart.getType() != type) {
        parts.add(currentPart);
        currentPart = new AddressPart(type);
      }
      currentPart.getContent().append(c);
    }

    private int findMainPart() {
      int maxLength = 0;
      int maxLengthIx = 0;
      int ix = 0;
      for (AddressPart part : parts) {
        if (part.getType() == PartType.OTHER && part.getContent().length() > maxLength) {
          maxLength = part.getContent().length();
          maxLengthIx = ix;
        }
        ix++;
      }
      return maxLengthIx;
    }

    @Getter
    private static class AddressPart {

      private final PartType type;

      private final StringBuilder content = new StringBuilder();

      public AddressPart(PartType type) {
        this.type = type;
      }
    }

    private enum PartType {
      NUMBER, SPACE, OTHER
    }

 

  }


}
