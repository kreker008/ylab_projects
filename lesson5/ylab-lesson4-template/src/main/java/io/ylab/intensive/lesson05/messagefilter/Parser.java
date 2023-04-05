package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson05.messagefilter.util.DBUtil;
import io.ylab.intensive.lesson05.messagefilter.util.MQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Component
public class Parser {
    private final Connection connectionDB;
    private final com.rabbitmq.client.Connection connectionMQ;
    private final String exchangeName;
    private final String queueInputName;
    private final String queueOutputName;
    private final String routingKey;
    @Value("${Parser.selectSQL}")
    private String selectSQL;

    @Autowired
    Parser(DBUtil dbUtil, MQUtil mqUtil) {
        this.connectionDB = dbUtil.getConnection();
        this.connectionMQ = mqUtil.getConnection();
        this.exchangeName = mqUtil.getExchangeName();
        this.queueInputName = mqUtil.getQueueInputName();
        this.queueOutputName = mqUtil.getQueueOutputName();
        this.routingKey = mqUtil.getRoutingKey();
    }

    private String fixWord(StringBuilder word) throws SQLException {
        try (PreparedStatement preparedStatement = connectionDB.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, word.toString().toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return word.toString();
            }
        }
        for (int i = 1; i < word.length() - 1; ++i) {
            word.setCharAt(i, '*');
        }
        return word.toString();
    }

    private String changeMessage(String rqMessage) throws SQLException {
        StringBuilder changedMessage = new StringBuilder();
        String correctWord;
        String breakSymbols = " .,;?!\n\r";

        int i = 0;
        while (i < rqMessage.length()) {
            StringBuilder word = new StringBuilder();
            while (i < rqMessage.length() &&
                    breakSymbols.indexOf(rqMessage.charAt(i)) == -1) {
                word.append(rqMessage.charAt(i));
                ++i;
            }
            correctWord = fixWord(word);
            changedMessage.append(correctWord);
            while (i < rqMessage.length() &&
                    breakSymbols.indexOf(rqMessage.charAt(i)) != -1) {
                changedMessage.append(rqMessage.charAt(i));
                ++i;
            }
        }

        return changedMessage.toString();
    }

    private void sendMessage(String message) throws IOException, TimeoutException {
        try (Channel channel = this.connectionMQ.createChannel()) {
            channel.basicPublish("", this.queueOutputName, null, message.getBytes());
        }
    }

    public void listen() throws IOException, TimeoutException, SQLException {
        try (Channel channel = connectionMQ.createChannel()) {
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet(queueInputName, true);
                if (message != null) {
                    String rqMessage = new String(message.getBody(), StandardCharsets.UTF_8);
                    String changedMessage = changeMessage(rqMessage);
                    sendMessage(changedMessage);
                }
            }
        }
    }
}
