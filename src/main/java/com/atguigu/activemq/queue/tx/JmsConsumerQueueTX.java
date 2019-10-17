package com.atguigu.activemq.queue.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * QUEUE 消息消费者 事务
 * Program Name: activemq_demo
 * Created by yanlp on 2019-10-15
 *
 * @author yanlp
 * @version 1.0
 */
public class JmsConsumerQueueTX {
    /**
     * mq地址,写的是自己本地ip   端口号默认是61616
     */
    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号消费者");
        // 1.创建连接工厂,按照给定的url地址,采用默认用户名和密码  admin和admin
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂,获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 3.创建会话session  两个参数,第一个叫事务,第二个叫签收
        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        // 4.创建目的地(具体是队列还是主题topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        // 5.创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        while (true) {
            Message message = messageConsumer.receive(4000L);
            if (null != message && message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("******通过MessageListener 事务 消费成功" + textMessage.getText());
            } else {
                break;
            }
        }

//        messageConsumer.setMessageListener(message -> {
//            if (message != null && message instanceof TextMessage) {
//                TextMessage textMessage = (TextMessage) message;
//                try {
//                    System.out.println("******通过MessageListener 事务 消费成功" + textMessage.getText());
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        // 只在使用MessageLisener时使用, System.in.read();
        // 6.关闭资源 倒着关闭
        messageConsumer.close();
        session.commit();
        session.close();
        connection.close();
    }
}
