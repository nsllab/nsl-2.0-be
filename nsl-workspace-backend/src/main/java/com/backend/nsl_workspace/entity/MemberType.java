package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "member_typ_tbl")
public class MemberType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_type_id")
    private Integer id;

    @Column(name = "member_type_name")
    private String memberTypeName;

    // Getters
    public Integer getId() {
        return id;
    }

    public String getMemberTypeName() {
        return memberTypeName;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setMemberTypeName(String memberTypeName) {
        this.memberTypeName = memberTypeName;
    }
}