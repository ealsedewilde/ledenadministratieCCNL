package nl.ealse.ccnl.ledenadministratie.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CLUB")
public class ExternalRelationClub extends ExternalRelation {

}
