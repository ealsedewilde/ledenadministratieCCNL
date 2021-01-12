package nl.ealse.ccnl.ledenadministratie.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("OTHER")
public class ExternalRelationOther extends ExternalRelation {

}
