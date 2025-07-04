package org.example.expert.domain.manager.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class ManagerLog extends Timestamped {  //언제 기록되었는지(createdAt)가 중요하므로 timestamped 필요

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;         // 어떤 action인지
    private String status;         // 성공 or 실패
    private String detailMessage;  // 오류 내용, 유저 ID 등

    public ManagerLog(String action, String status, String detailMessage) {
        this.action = action;
        this.status = status;
        this.detailMessage = detailMessage;
    }
}
