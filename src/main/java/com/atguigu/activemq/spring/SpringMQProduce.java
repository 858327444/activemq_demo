package com.atguigu.activemq.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * Spring和mq 整合之 队列生产者
 * Program Name: activemq_demo
 * Created by yanlp on 2019-10-19
 *
 * @author yanlp
 * @version 1.0
 */
@Service
public class SpringMQProduce {
    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringMQProduce springMQProduce = (SpringMQProduce) context.getBean("springMQProduce");// 相当于 SpringMQProduce springMQProduce  = new SpringMQProduce();
        springMQProduce.jmsTemplate.send( session -> {
            TextMessage text = session.createTextMessage("Spring和MQ的整合之队列  case1111");
            return text;
        });
        System.out.println("******** send task over");


    }


}
