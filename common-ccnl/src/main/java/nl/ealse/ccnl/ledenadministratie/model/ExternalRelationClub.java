package nl.ealse.ccnl.ledenadministratie.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLUB")
public class ExternalRelationClub extends ExternalRelation {

}
