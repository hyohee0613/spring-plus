package org.example.expert.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    /*
    다음 코드들은 Spring @Bean으로 관리하므로 바로 주입해주고있음
    private final EntityManager entityManager;

	public QueryDslConfig(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	*/

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);

    }

}

