package nats.lite.listener;

import nats.lite.util.NatsServerUtil;
import io.nats.client.*;
import io.nats.client.support.Status;
import lombok.extern.slf4j.Slf4j;

/**
 * @time: 2022/10/12
 * @author: .sun
 */

@Slf4j
public class NatsErrorListener implements io.nats.client.ErrorListener {


    public NatsErrorListener() {

    }

    /**
     * NATs related errors that occur asynchronously in the client library are sent
     * to an ErrorListener via errorOccurred. The ErrorListener can use the error text to decide what to do about the problem.
     * <p>The text for an error is described in the protocol doc at `https://nats.io/documentation/internals/nats-protocol`.
     * <p>In some cases the server will close the clients connection after sending one of these errors. In that case, the
     * connections {@link ConnectionListener ConnectionListener} will be notified.
     *
     * @param conn  The connection associated with the error
     * @param error The text of error that has occurred, directly from the server
     */
    @Override
    public void errorOccurred(Connection conn, String error) {

        log.error("service: [{}] , server : [{}]  ,  NATS connection error occurred :[{}]", conn.getOptions().getConnectionName(), NatsServerUtil.getServer(conn), error);
    }

    /**
     * Exceptions that occur in the "normal" course of operations are sent to the
     * ErrorListener using exceptionOccurred. Examples include, application exceptions
     * during Dispatcher callbacks, IOExceptions from the underlying socket, etc..
     * The library will try to handle these, via reconnect or catching them, but they are
     * forwarded here in case the application code needs them for debugging purposes.
     *
     * @param conn The connection associated with the error
     * @param exp  The exception that has occurred, and was handled by the library
     */
    @Override
    public void exceptionOccurred(Connection conn, Exception exp) {
        log.error("service: [{}] , server : [{}]  ,  NATS connection exception occurred :[{}]", conn.getOptions().getConnectionName(), NatsServerUtil.getServer(conn), exp.getMessage());
    }

    /**
     * Called by the connection when a &quot;slow&quot; consumer is detected. This call is only made once
     * until the consumer stops being slow. At which point it will be called again if the consumer starts
     * being slow again.
     *
     * <p>See {@link Consumer#setPendingLimits(long, long) Consumer.setPendingLimits}
     * for information on how to configure when this method is fired.
     *
     * <p> Slow consumers will result in dropped messages each consumer provides a method
     * for retrieving the count of dropped messages, see {@link Consumer#getDroppedCount() Consumer.getDroppedCount}.
     *
     * @param conn     The connection associated with the error
     * @param consumer The consumer that is being marked slow
     */
    @Override
    public void slowConsumerDetected(Connection conn, Consumer consumer) {
        log.warn("service: [{}] , server : [{}]  ,  NATS connection slow consumer detected ,Delivered message count:[{}] , Pending message count:[{}] , Pending message limit:[{}], Dropped message count:[{}]",
                conn.getOptions().getConnectionName(), NatsServerUtil.getServer(conn), consumer.getDeliveredCount() ,consumer.getPendingMessageCount() ,consumer.getPendingMessageLimit() ,  consumer.getDroppedCount());
    }

    /**
     * Called by the connection when a message is discarded.
     *
     * @param conn The connection that discarded the message
     * @param msg  The message that is discarded
     */
    @Override
    public void messageDiscarded(Connection conn, Message msg) {
        log.error("service: [{}] , server : [{}]  ,  NATS connection message discard :[{}]", conn.getOptions().getConnectionName(), NatsServerUtil.getServer(conn), msg.getSubject());
    }

    /**
     * Called when subscription heartbeats are missed according to the configured period and threshold.
     * The consumer must be configured with an idle heartbeat time.
     *
     * @param conn                 The connection that had the issue
     * @param sub                  the JetStreamSubscription that this occurred on
     * @param lastStreamSequence   the last received stream sequence
     * @param lastConsumerSequence the last received consumer sequence
     */
    @Override
    public void heartbeatAlarm(Connection conn, JetStreamSubscription sub, long lastStreamSequence, long lastConsumerSequence) {
        log.warn("service: [{}] , server : [{}]  ,  NATS connection heartbeat alarm :[{}]", conn.getOptions().getConnectionName(), NatsServerUtil.getServer(conn), sub);
    }

    /**
     * Called by the connection when an unhandled status is received.
     *
     * @param conn   The connection that had the issue
     * @param sub    the JetStreamSubscription that this occurred on
     * @param status the status
     */
    @Override
    public void unhandledStatus(Connection conn, JetStreamSubscription sub, Status status) {
        log.error("service: [{}] , server : [{}]  ,  NATS connection unhandled status :[{}]", conn.getOptions().getConnectionName(), NatsServerUtil.getServer(conn), status);
    }

    /**
     * Called by the connection when a flow control is processed.
     *
     * @param conn    The connection that had the issue
     * @param sub     the JetStreamSubscription that this occurred on
     * @param subject the flow control subject that was handled
     * @param source  enum indicating flow control handling in response to which type of message
     */
    @Override
    public void flowControlProcessed(Connection conn, JetStreamSubscription sub, String subject, FlowControlSource source) {
        log.warn("service: [{}] , server : [{}]  ,  NATS connection flow control processed :[{}]", conn.getOptions().getConnectionName(), NatsServerUtil.getServer(conn), sub);
    }

}
