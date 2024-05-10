package com.todolist.todolist.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Builder  // Builder & AllArgs -> Mapper 에서 id 를 찾아야 함..
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    // 배포시 @Builder 표현식 무시- > 초기값을 기본값으로 설정
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    // 1:N ] N쪽이 FK가진 연관관계의 주인
    private List<Todo> todoList = new ArrayList<>();

    public void updateLoginId(String loginId){
        this.loginId = loginId;
    }

    public void updatePassword(String Password){
        this.password = password;
    }

    public void updateName(String name){
        this.name = name;
    }
}
