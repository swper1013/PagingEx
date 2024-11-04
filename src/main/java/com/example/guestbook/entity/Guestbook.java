package com.example.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Guestbook extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length = 100,nullable = false)
    private String title;
    @Column(length = 1500,nullable = false)
    private String content;
    @Column(length = 50,nullable = false)
    private String writer;

    public  void changeTitle(String title){
        this.title = title;
    }
    public void changeContent(String content){
        this.content = content;
    }

    public void changeWriter(String writer){this.writer=writer;}


}
