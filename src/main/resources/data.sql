-- ============================================
-- tbl_transaction 테스트 데이터 30건
-- 전날 날짜 기준 (배치 실행일 하루 전)
-- 금액 0원 2건 포함 → Processor에서 필터
-- 결과: writeCount=28
-- ============================================
-- [ batch meta table초기화 ]
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE batch_step_execution_context;
TRUNCATE TABLE batch_step_execution;
TRUNCATE TABLE batch_job_execution_context;
TRUNCATE TABLE batch_job_execution_params;
TRUNCATE TABLE batch_job_execution;
TRUNCATE TABLE batch_job_instance;

-- sequence초기화
ALTER SEQUENCE batch_job_seq             RESTART WITH 1;
ALTER SEQUENCE batch_job_execution_seq   RESTART WITH 1;
ALTER SEQUENCE batch_step_execution_seq  RESTART WITH 1;

SET FOREIGN_KEY_CHECKS = 1;


SET @yesterday = DATE_SUB(NOW(), INTERVAL 1 DAY);
DELETE FROM tbl_settlement;
DELETE FROM tbl_transaction;

INSERT INTO tbl_transaction
(account_no, tx_type, amount, tx_date, settled)
VALUES
-- DEPOSIT 15건
('110-001', 'DEPOSIT', 1000000, @yesterday, false),
('110-001', 'DEPOSIT', 2000000, @yesterday, false),
('110-002', 'DEPOSIT', 500000,  @yesterday, false),
('110-002', 'DEPOSIT', 3000000, @yesterday, false),
('110-003', 'DEPOSIT', 750000,  @yesterday, false),
('110-003', 'DEPOSIT', 1500000, @yesterday, false),
('110-004', 'DEPOSIT', 4000000, @yesterday, false),
('110-004', 'DEPOSIT', 800000,  @yesterday, false),
('110-005', 'DEPOSIT', 250000,  @yesterday, false),
('110-005', 'DEPOSIT', 600000,  @yesterday, false),
('110-006', 'DEPOSIT', 1200000, @yesterday, false),
('110-006', 'DEPOSIT', 900000,  @yesterday, false),
('110-007', 'DEPOSIT', 350000,  @yesterday, false),
('110-007', 'DEPOSIT', 2500000, @yesterday, false),
('110-008', 'DEPOSIT', 0,       @yesterday, false),  -- 금액 0 → 필터

-- WITHDRAW 15건
('110-001', 'WITHDRAW', 500000,  @yesterday, false),
('110-001', 'WITHDRAW', 1000000, @yesterday, false),
('110-002', 'WITHDRAW', 300000,  @yesterday, false),
('110-002', 'WITHDRAW', 200000,  @yesterday, false),
('110-003', 'WITHDRAW', 750000,  @yesterday, false),
('110-003', 'WITHDRAW', 1500000, @yesterday, false),
('110-004', 'WITHDRAW', 2000000, @yesterday, false),
('110-004', 'WITHDRAW', 400000,  @yesterday, false),
('110-005', 'WITHDRAW', 150000,  @yesterday, false),
('110-005', 'WITHDRAW', 800000,  @yesterday, false),
('110-006', 'WITHDRAW', 600000,  @yesterday, false),
('110-006', 'WITHDRAW', 1100000, @yesterday, false),
('110-007', 'WITHDRAW', 250000,  @yesterday, false),
('110-007', 'WITHDRAW', 900000,  @yesterday, false),
('110-008', 'WITHDRAW', 0,       @yesterday, false); -- 금액 0 → 필터

DELETE FROM tbl_savings;

INSERT INTO tbl_savings
(savings_no, owner_name, linked_account,
 principal, annual_rate, monthly_amount, months,
 start_date, maturity_date, status)
VALUES
    ('SV-001','홍길동','110-001',12000000,3.50,1000000,12,
     DATE_SUB(CURDATE(),INTERVAL 12 MONTH),CURDATE(),'ACTIVE'),
    ('SV-002','김철수','110-002', 6000000,3.00, 500000,12,
     DATE_SUB(CURDATE(),INTERVAL 12 MONTH),CURDATE(),'ACTIVE'),
    ('SV-003','이영희','110-003',24000000,4.00,2000000,12,
     DATE_SUB(CURDATE(),INTERVAL 12 MONTH),CURDATE(),'ACTIVE'),
    -- 만기 아님 (내일 만기)
    ('SV-004','박민준','110-004',12000000,3.50,1000000,12,
     DATE_SUB(CURDATE(),INTERVAL 11 MONTH),
     DATE_ADD(CURDATE(),INTERVAL 1 MONTH),'ACTIVE');
