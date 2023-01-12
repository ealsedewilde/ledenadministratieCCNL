package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OTHER")
public class ExternalRelationOther extends ExternalRelation {

}
