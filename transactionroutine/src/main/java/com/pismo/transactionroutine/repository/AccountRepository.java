package com.pismo.transactionroutine.repository;

import com.pismo.transactionroutine.models.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByDocumentId(String documentId);

   // @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select * from account where account_id = :accountId for update" , nativeQuery = true)
    Account getAccountWithLock(@Param("accountId") long accountId);

//    @Query("select credit_limit from account where accountId = :accountId")
//    BigDecimal getAccountCreditLimit(Long accountId);

    @Modifying
    @Query(value = "UPDATE ACCOUNT SET CREDIT_LIMIT = :balance where account_id=:accountId and credit_limit >= 0",nativeQuery = true)
    int updateAccontCreditLimit(@Param("balance") BigDecimal balance ,@Param("accountId") Long accountId);
}
