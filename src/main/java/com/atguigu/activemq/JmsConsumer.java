package com.atguigu.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

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

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号消费者");
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
       /*
        第一种方式消费: 同步阻塞方式(receive())
        订阅者或接收者调动MessageConsumer的reveive()方法来接收消息,receive方法将在接收到消息前(或超时前)将一直阻塞
        while (true) {
            // 消费,有无参数和有参数receive, 过4秒后消费停止
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
            if (null != textMessage) {
                System.out.println("******通过receive方式消费成功 消息为: " + textMessage.getText());
            } else {
                break;
            }
        }
        // 6.关闭资源 倒着关闭
        messageConsumer.close();
        session.close();
        connection.close();
        */

        // 第二种消费方式:  通过监听器
        // 异步非阻塞方式 (监听器 onMessage())
        // 订阅者或接收者,通过MessageConsumer的setMessageListener(MessageListener messageListener)注册一个消息监听器,
        // 当消息到达后,系统会自动调用MessageListener的onMessage(Message message)方法进行消费
        messageConsumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("******通过MessageListener 消费成功" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 保证可以消费到,如果没有下面一行代码,极大可能会消费不到数据,就关闭资源了
        System.in.read();
        // 6.关闭资源 倒着关闭
        messageConsumer.close();
        session.close();
        connection.close();

        /*
            消费者三大消费情况

            1.先生产,   只启动1号消费者,问题:  1号可以消费吗
                 可以消费
            2.先生产,先启动1号消费者,再启动2号消费者,问题:2号还可以消费吗?
                1号可以消费,2号不可以消费
            3.先启动两个消费者,再启动生产者,生产者生产出6条消息,请问消费情况如何?
                两个消费者一人消费一半

         */


    }
}
