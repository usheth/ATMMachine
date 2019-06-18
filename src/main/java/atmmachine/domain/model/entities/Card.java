package atmmachine.domain.model.entities;

import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "card")
public class Card {

  @Id
  private String cardNumber;
  @Basic
  private Timestamp createTime;
  @Basic
  private Timestamp updateTime;

  public Card(String cardNumber) {
    this.cardNumber = cardNumber;
  }
}
