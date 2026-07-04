package com.microjainslee.tx;

import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Narayana-backed JTA transaction manager cho JAIN SLEE.
 *
 * Wire vào EventRouter.dispatchWithTransaction() — mỗi SBB onEvent()
 * chạy trong 1 JTA transaction để profile + timer + activity updates atomic.
 *
 * CRITICAL — VirtualThread pinning prevention:
 *   Narayana 7.0 dùng ThreadLocal nhưng không còn synchronized blocks
 *   trên hot path. Nếu vẫn thấy pinning qua -Djdk.tracePinnedThreads=full,
 *   dùng ReentrantLock wrapper bên dưới.
 *
 * Module: jainslee-tx (NEW)
 * Dependency: org.jboss.narayana.jta:narayana-jta:7.0.0.Final
 */
public class SleeTransactionManager {

    private static final Logger LOG = LogManager.getLogger(SleeTransactionManager.class);

    private final jakarta.transaction.TransactionManager tm;

    // Pinning guard — thay synchronized trong Narayana internal nếu cần
    private final ReentrantLock txLock = new ReentrantLock();

    public SleeTransactionManager() {
        // Narayana standalone — không cần JBoss AS hay WildFly
        this.tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
        LOG.info("SleeTransactionManager initialized (Narayana standalone)");
    }

    /**
     * Execute task trong JTA transaction.
     * Sequence: begin → task → commit | rollback.
     *
     * @param task SBB event handler invocation
     * @throws RuntimeException wrapping original nếu task fail
     */
    public void executeInTransaction(Runnable task) {
        try {
            tm.begin();
            try {
                task.run();
                if (tm.getStatus() == Status.STATUS_ACTIVE) {
                    tm.commit();
                    LOG.tracef("Transaction committed");
                }
            } catch (Throwable t) {
                LOG.errorf(t, "SBB transaction failed — rolling back");
                safeRollback();
                throw new RuntimeException("SBB transaction rolled back", t);
            }
        } catch (jakarta.transaction.NotSupportedException | SystemException e) {
            throw new RuntimeException("JTA transaction boundary error", e);
        }
    }

    /**
     * Execute với explicit rollback control — trả về false nếu rolled back.
     * Dùng khi SBB cần set rollback-only (ví dụ: validation fail nhưng không throw).
     */
    public boolean executeWithResult(TransactionalTask task) {
        try {
            tm.begin();
            try {
                task.execute(this);
                int status = tm.getStatus();
                if (status == Status.STATUS_MARKED_ROLLBACK) {
                    tm.rollback();
                    return false;
                }
                if (status == Status.STATUS_ACTIVE) {
                    tm.commit();
                    return true;
                }
                return false;
            } catch (Throwable t) {
                safeRollback();
                throw new RuntimeException("Transaction rolled back", t);
            }
        } catch (jakarta.transaction.NotSupportedException | SystemException e) {
            throw new RuntimeException("JTA boundary error", e);
        }
    }

    /** Đánh dấu transaction hiện tại rollback-only (SBB gọi từ context). */
    public void setRollbackOnly() {
        try {
            tm.setRollbackOnly();
            LOG.debugf("Transaction marked rollback-only");
        } catch (SystemException e) {
            LOG.errorf(e, "Failed to mark rollback-only");
        }
    }

    /** Trả về status hiện tại — dùng cho EventMDC logging. */
    public int currentStatus() {
        try {
            return tm.getStatus();
        } catch (SystemException e) {
            return Status.STATUS_UNKNOWN;
        }
    }

    public boolean isActive() {
        return currentStatus() == Status.STATUS_ACTIVE;
    }

    public String currentStatusName() {
        return switch (currentStatus()) {
            case Status.STATUS_ACTIVE          -> "ACTIVE";
            case Status.STATUS_COMMITTED       -> "COMMITTED";
            case Status.STATUS_ROLLEDBACK      -> "ROLLED_BACK";
            case Status.STATUS_ROLLING_BACK    -> "ROLLING_BACK";
            case Status.STATUS_MARKED_ROLLBACK -> "MARKED_ROLLBACK";
            case Status.STATUS_NO_TRANSACTION  -> "NO_TX";
            default                            -> "UNKNOWN";
        };
    }

    private void safeRollback() {
        try {
            int status = currentStatus();
            if (status != Status.STATUS_NO_TRANSACTION && status != Status.STATUS_ROLLEDBACK) {
                tm.rollback();
            }
        } catch (SystemException e) {
            LOG.errorf(e, "Failed to rollback — transaction may be in inconsistent state");
        }
    }

    @FunctionalInterface
    public interface TransactionalTask {
        void execute(SleeTransactionManager txMgr) throws Exception;
    }
}
