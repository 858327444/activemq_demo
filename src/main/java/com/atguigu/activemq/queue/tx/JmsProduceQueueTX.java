package com.atguigu.activemq.queue.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * QUEUE 消息生产者   事务
 * Program Name: activemq_demo
 * Created by yanlp on 2019-10-15
 *
 * @author yanlp
 * @version 1.0
 */
public class JmsProduceQueueTX {
    /**
     * mq地址,写的是自己本地ip   端口号默认是61616
     */
    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    /**
     * 名称
     */
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        // 1.创建连接工厂,按照给定的url地址,采用默认用户名和密码  admin和admin
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂,获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 3.创建会话session  两个参数,第一个叫事务,第二个叫签收
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        // 4.创建目的地(具体是队列还是主题topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        // 5.创建消息的生成者
        MessageProducer messageProducer = session.createProducer(queue);
        // 6.通过使用messageProducer生产3条消息发送到MQ的队列里面去
        for (int i = 1; i <= 3; i++) {
            // 7.创建消息
            TextMessage textMessage = session.createTextMessage("msg----queue" + i);
            // 8.通过messageProducer发送给mq
            messageProducer.send(textMessage);
        }
        // 9.关闭资源  倒着关闭
        messageProducer.close();
        // 如果事务设置为true,则必须要在sessioni在lose前,commit
        session.commit();
        session.close();
        connection.close();

        System.out.println("******queue 事务消息发布到MQ完成");
    }

}
