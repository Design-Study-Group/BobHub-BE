package com.bobhub.party;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 간단한 테스트로 환경 설정 확인
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SimpleTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseConnection() {
        // 데이터베이스 연결 테스트
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result);
        System.out.println("✅ 데이터베이스 연결 성공!");
    }

    @Test
    void testTableExists() {
        // 테이블 존재 확인
        try {
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM party", Integer.class);
            System.out.println("✅ party 테이블이 존재합니다!");
        } catch (Exception e) {
            System.out.println("❌ party 테이블이 없습니다: " + e.getMessage());
            fail("테이블이 생성되지 않았습니다");
        }
    }

    @Test
    void testDataExists() {
        // 테스트 데이터 확인
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM party", Integer.class);
            System.out.println("✅ party 테이블에 " + count + "개의 데이터가 있습니다!");
            assertTrue(count > 0, "테스트 데이터가 있어야 합니다");
        } catch (Exception e) {
            System.out.println("❌ 데이터 조회 실패: " + e.getMessage());
            fail("테스트 데이터가 없습니다");
        }
    }
} 