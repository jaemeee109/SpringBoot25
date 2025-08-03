package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mbc.board.domain.BaseEntity;
import org.mbc.board.domain.Member;

@Entity
@Table(name="cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_mid")
    private Member member;


} // Cart 종료
