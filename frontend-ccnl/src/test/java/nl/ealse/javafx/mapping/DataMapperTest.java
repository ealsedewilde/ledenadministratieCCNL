package nl.ealse.javafx.mapping;

import java.time.LocalDate;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nl.ealse.ccnl.control.address.AddressController;
import nl.ealse.ccnl.control.member.MemberController;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.PaymentMethod;
import nl.ealse.ccnl.mappers.MembershipStatusMapper;
import nl.ealse.ccnl.mappers.PaymentMethodMapper;
import nl.ealse.ccnl.test.FXBase;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DataMapperTest  extends FXBase {

  private AddressController addressController = new AddressController();

  @Test
  void testFormToModel() throws Exception {
    MemberController form = initializedForm();
    Member model = new Member();
    model.setAddress(null);
    ViewModel.viewToModel(form, model);
    Assertions.assertEquals("Helena Hoeve", model.getAddress().getStreet());
    Assertions.assertEquals(PaymentMethod.BANK_TRANSFER, model.getPaymentMethod());
  }

  @Test
  void testModelToForm() throws Exception {
    MemberController form = initializedForm();
    Member model = initializedModel();
    ViewModel.modelToView(form, model);
    Assertions.assertEquals("Ida Hoeve", addressController.getStreet().getText());
    Assertions.assertEquals(PaymentMethodMapper.DIRECT_DEBIT, form.getPaymentMethod().getValue());
  }

  private MemberController initializedForm() {
    MemberController form = new MemberController(null, null, null);
    injectAddressController(form);
    addressController.setStreet(new TextField("Helena Hoeve"));
    addressController.setAddressNumber(new TextField("26"));
    addressController.setAddressNumberAppendix(new TextField());
    addressController.setCity(new TextField("Gouda"));
    addressController.setCountry(new TextField());
    form.setEmail(new TextField("ledenadministratie@ccnl.nl"));
    form.setIbanNumber(new TextField("NL54ASNB0709093276"));
    form.setInitials(new TextField("ERW"));
    form.setLastName(new TextField("Wilde"));
    form.setLastNamePrefix(new TextField("de"));
    form.setMemberInfo(new TextArea());
    form.setMemberNumber(new Label("1473"));
    form.setMemberSince(new DatePicker(LocalDate.of(2000, 6, 10)));
    addressController.setPostalCode(new TextField("2804 HX"));
    form.setTelephoneNumber(new TextField("0123456789"));
    form.setNoMagazine(new CheckBox("geen blad"));

    ChoiceBox<String> memberStatus = new ChoiceBox<>(MembershipStatusMapper.getStatuses());
    memberStatus.setValue(MembershipStatusMapper.INACTIVE);
    form.setMemberStatus(memberStatus);;

    ChoiceBox<String> paymentMethod = new ChoiceBox<>(PaymentMethodMapper.getValues());
    paymentMethod.setValue(PaymentMethodMapper.BANK_TRANSFER);
    form.setPaymentMethod(paymentMethod);
    return form;
  }

  private Member initializedModel() {
    Member member = new Member();
    Address address = member.getAddress();
    address.setStreet("Ida Hoeve");
    address.setAddressNumber("16");
    address.setPostalCode("2804 TV");
    address.setCity("Gouda");
    member.setAddress(address);
    member.setEmail("santaclaus@gmail.com");
    member.setIbanNumber("NL54ASNB0709093276");
    member.setInitials("I.M.");
    member.setLastName("Wolf");
    member.setLastNamePrefix("van der");
    member.setMemberInfo("Some additional text");
    member.setMemberNumber(1473);
    member.setMemberSince(LocalDate.of(2000, 6, 1));
    member.setMemberStatus(MembershipStatus.ACTIVE);
    member.setPaymentMethod(PaymentMethod.DIRECT_DEBIT);
    member.setTelephoneNumber("06-123456789");

    return member;
  }

  private void injectAddressController(MemberController controller) {
    try {
      FieldUtils.writeField(controller, "addressController", addressController, true);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
