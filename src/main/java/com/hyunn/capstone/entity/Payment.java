package com.hyunn.capstone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "payment")
@Getter
@ToString(exclude = "paymentId")
@NoArgsConstructor
public class Payment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long paymentId;

  // 가격
  @Column(name = "price")
  private Integer price;

  // 주소
  @Column(name = "address")
  private String address;

  // 배송 회사 링크
  @Column(name = "link")
  private String link;

  // 조인
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private Payment(Integer price, String address, User user) {
    this.price = price;
    this.address = address;
    this.user = user;
  }

  public static Payment createPayment(Integer price, String address, User user) {
    return new Payment(price, address, user);
  }

}
