package com.gb.sapp.domain.post.post.entity;

import com.gb.sapp.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Setter(value = AccessLevel.PRIVATE)

    private LocalDateTime modifyDate;

    private String title;

    private String body;

    @ManyToOne(fetch = LAZY)
    private Member author;

    @Column(columnDefinition = "BOOLEAN default false")
    private boolean published;

    @Column(columnDefinition = "BOOLEAN default false")
    private boolean listed;

    public void setModified() {
        setModifyDate(LocalDateTime.now());
    }
}
