package utilities

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import util.Random

class ActiveMq {
  
  def producer {
  val activeMqUrl: String = "tcp://localhost:61616"
 
 
    val connectionFactory = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = connectionFactory.createConnection
    connection.setClientID("ProducerSynchronous")
    connection.start
 
    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val sendQueue = session.createQueue("SendSynchronousMsgQueue")
    val replyQueue = session.createQueue("ReplyToSynchronousMsgQueue")
 
    val producer = session.createProducer(sendQueue)
    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT)
 
    val correlationId = new Random().nextString(32)
    val replyConsumer = session.createConsumer(replyQueue, "JMSCorrelationID = '%s'".format(correlationId))
 
    val textMessage = session.createTextMessage("Hello, please reply immediately to my message!")
    textMessage.setJMSReplyTo(replyQueue)
    textMessage.setJMSCorrelationID(correlationId)
 
    println("Sending message...")
 
    producer.send(textMessage)
 
    println("Waiting for reply...")
 
    val reply = replyConsumer.receive(1000)
    replyConsumer.close()
 
    reply match {
      case txt: TextMessage => println("Received reply: " + txt.getText())
      case _ => throw new Exception("Invalid Response:" + reply)
    }
 
    connection.close
  }
  
  
  
  def c ={
    
  val activeMqUrl: String = "tcp://localhost:61616"
  
    val connectionFactory  = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = connectionFactory.createConnection
    connection.setClientID("ConsumerSynchronous")
    connection.start
 
    println("Started")
 
    val session: Session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val queue  = session.createQueue("SendSynchronousMsgQueue")
    val consumer = session.createConsumer(queue)
 
    val listener = new MessageListener {
      def onMessage(message: Message) {
        message match {
          case text: TextMessage => {
          val replyProducer = session.createProducer(text.getJMSReplyTo())
          replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT)
 
          println("Received message: " + text.getText)
 
          val replyMessage = session.createTextMessage("Yes I received your message!")
          replyMessage.setJMSCorrelationID(text.getJMSCorrelationID())
 
          println("Reply sent!")
             
          replyProducer.send(replyMessage)
          }
          case _ => {
            throw new Exception("Unhandled Message Type: " + message.getClass.getSimpleName)
          }
        }
      }
    }
    consumer.setMessageListener(listener)    
  }
}