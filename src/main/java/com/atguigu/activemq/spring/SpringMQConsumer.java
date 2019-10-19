package com.atguigu.activemq.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Spring和MQ 整合之 消费者
 * Program Name: activemq_demo
 * Created by yanlp on 2019-10-19
 *
 * @author yanlp
 * @version 1.0
 */
@Service
public class SpringMQConsumer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringMQConsumer springMQConsumer = (SpringMQConsumer) context.getBean("springMQConsumer");
        String retValue = (String) springMQConsumer.jmsTemplate.receiveAndConvert();
        System.out.println("Spring和MQ整合 消费者收到的消息为: " + retValue);
    }
}
