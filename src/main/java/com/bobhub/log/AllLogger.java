package com.bobhub.log;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AllLogger {
  private static final Logger logger = LoggerFactory.getLogger(AllLogger.class);
  private final DataSource dataSource;

  public AllLogger(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // 프로젝트 재실행시 자동 호출
  @PostConstruct
  public void logDatabaseConnection() {
    try (Connection connection = dataSource.getConnection()) {
      logger.info("✅ Oracle DB Connection SUCCESS ✅");
    } catch (Exception e) {
      logger.info("❌ Oracle DB Connection FAILED ❌");
    }
  }

  public void InfoLogger(String message) {
    /**
     * info 레벨 로깅을 생성하는 메서드
     *
     * @param String message: 로깅 메시지 내용
     */
    logger.info(message);
  }
}
