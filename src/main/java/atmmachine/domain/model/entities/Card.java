package atmmachine.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity(name = "card")
public class Card {

  @Id
  private String cardNumber;
  @Basic
  @JsonIgnore
  private Timestamp createTime;
  @Basic
  @JsonIgnore
  private Timestamp updateTime;
  @Basic
  @JsonIgnore
  private String pin;

  public String getCardNumber() {
    return cardNumber;
  }

  public Card() {
  }

  public Card(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  @PrePersist
  private void setCreateTime() {
    createTime = new Timestamp(System.currentTimeMillis());
  }

  @PreUpdate
  private void setUpdateTime() {
    updateTime = new Timestamp(System.currentTimeMillis());
  }

  public String getPin() {
    return pin;
  }
}
