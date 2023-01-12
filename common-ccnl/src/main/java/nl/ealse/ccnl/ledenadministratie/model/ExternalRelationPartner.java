package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PARTNER")
public class ExternalRelationPartner extends ExternalRelation {

}
