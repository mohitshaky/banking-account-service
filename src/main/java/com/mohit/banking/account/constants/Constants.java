package com.mohit.banking.account.constants;

/**
 * Constants - Application-wide constants for Banking Account Service.
 * @author mohit
 */
public final class Constants {

    /** Topic for account created events. */
    public static final String TOPIC_ACCOUNT_CREATED = "BA_ACCOUNT_CREATED";

    /** Topic for transaction events. */
    public static final String TOPIC_TRANSACTION_EVENT = "BA_TRANSACTION_EVENT";

    /** Topic for audit log events. */
    public static final String TOPIC_AUDIT_LOG = "BA_AUDIT_LOG";

    /** Consumer group ID for account events. */
    public static final String GROUP_ACCOUNT = "BA_ACCOUNT_GRP";

    /** Consumer group ID for transaction events. */
    public static final String GROUP_TRANSACTION = "BA_TXN_GRP";

    /** Header name for transaction ID. */
    public static final String HEADER_TRANSACTION_ID = "transactionId";

    /** Header name for correlation ID. */
    public static final String HEADER_CORRELATION_ID = "correlationId";

    /** Header name for source channel. */
    public static final String HEADER_SOURCE_CHANNEL = "sourceChannel";

    /** Header name for tenant ID. */
    public static final String HEADER_TENANT_ID = "tenantId";

    private Constants() {}
}
