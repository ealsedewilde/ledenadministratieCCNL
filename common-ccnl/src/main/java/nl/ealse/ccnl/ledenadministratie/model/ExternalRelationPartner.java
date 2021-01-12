package nl.ealse.ccnl.ledenadministratie.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PARTNER")
public class ExternalRelationPartner extends ExternalRelation {

}
