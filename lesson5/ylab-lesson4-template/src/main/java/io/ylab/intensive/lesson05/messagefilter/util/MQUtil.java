package io.ylab.intensive.lesson05.messagefilter.util;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
public class MQUtil {
    private final int CONNECTION_NUM;
    private final List<Connection> connectionPull;
    private int connectionGot;
    @Value("${MQUtil.exchangeName}")
    private String exchangeName;
    @Value("${MQUtil.queueInputName}")
    private String queueInputName;
    @Value("${MQUtil.queueOutputName}")
    private String queueOutputName;
    @Value("${MQUtil.routingKey}")
    private String routingKey;


    @Autowired
    public MQUtil(ConnectionFactory connectionFactory, @Value("${MQUtil.connectionsNum}") int connectionNum) throws IOException, TimeoutException {
        this.CONNECTION_NUM = connectionNum;
        this.connectionPull = new ArrayList<>();
        this.connectionGot = 0;
        for(int i = 0; i < CONNECTION_NUM; ++i){
            this.connectionPull.add(connectionFactory.newConnection());
        }
    }

    @PostConstruct
    public void setQueues() throws Exception {
        if(connectionPull.size() == 0){
            throw new  Exception("connection pull пустой");
        }
        try(Channel channel = connectionPull.get(0).createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(queueInputName, true, false, false, null);
            channel.queueDeclare(queueOutputName, true, false, false, null);
            channel.queueBind(queueInputName, exchangeName,routingKey);
            channel.queueBind(queueOutputName, exchangeName, routingKey);
        }
    }

    @PreDestroy
    private void closeConnections() throws IOException {
        for(Connection connection: connectionPull){
            if(connection.isOpen()){
                connection.close();
            }
        }
    }

    public Connection getConnection(){
        if(connectionGot >= CONNECTION_NUM){
            return null;
        }
        return connectionPull.get(connectionGot++);
    }

    public String getQueueInputName() {
        return queueInputName;
    }

    public String getQueueOutputName() {
        return queueOutputName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }
}
