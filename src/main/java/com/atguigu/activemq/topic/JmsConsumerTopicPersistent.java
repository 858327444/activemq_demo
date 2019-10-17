package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * TOPIC消息消费者  持久化
 * Program Name: activemq_demo
 * Created by yanlp on 2019-10-15
 *
 * @author yanlp
 * @version 1.0
 */
public class JmsConsumerTopicPersistent {
    /**
     * mq地址,写的是自己本地ip   端口号默认是61616
     */
    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    /**
     * 名称
     */
    public static final String TOPIC_NAME = "topicPersistent";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号消费者");
        // 1.创建连接工厂,按照给定的url地址,采用默认用户名和密码  admin和admin
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂,获得连接connection
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("z3");
        // 3.创建会话session  两个参数,第一个叫事务,第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4.创建目的地(具体是队列还是主题topic)
        Topic topic = session.createTopic(TOPIC_NAME);
        // 5.创建主题持久化订阅,第二个参数备注,没啥用
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark...");
        // 6.先订阅,然后在这里启动
        connection.start();
        Message message = topicSubscriber.receive();
        // 7.接收消息
        while (message != null) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("*****收到持久化Topic: " + textMessage.getText());
            message = topicSubscriber.receive();
        }
        // 8.关闭资源
        session.close();
        connection.close();
    }
}
