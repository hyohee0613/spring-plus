package org.example.expert.domain.manager.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.log.ManagerLog;
import org.example.expert.domain.manager.repository.ManagerLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class ManagerLogService {

    private final ManagerLogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void writeLog(String action, String status, String message) {
        ManagerLog log = new ManagerLog(action, status, message);  //로그 작성
        logRepository.save(log);  //DB 저장
    }
}
