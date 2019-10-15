package com.atguigu.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息消费者
 * Program Name: activemq_demo
 * Created by yanlp on 2019-10-15
 *
 * @author yanlp
 * @version 1.0
 */
public class JmsConsumer {
    /**
     * mq地址,写的是自己本地ip   端口号默认是61616
     */
    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        // 1.创建连接工厂,按照给定的url地址,采用默认用户名和密码  admin和admin
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂,获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 3.创建会话session  两个参数,第一个叫事务,第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4.创建目的地(具体是队列还是主题topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        // 5.创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        while (true) {
            TextMessage textMessage = (TextMessage) messageConsumer.receive();
            if (null != textMessage) {
                System.out.println("******消费成功 消息为: " + textMessage.getText());
            } else {
                break;
            }
        }
        // 6.关闭资源 倒着关闭
        messageConsumer.close();
        session.close();
        connection.close();


    }
}
