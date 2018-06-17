MQ为Message Queue
消息队列是：应用程序 和 应用程序 之间的通信方法。
AMQP：高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。
Erlang：是一种通用的面向并发的编程语言。
RabbitMQ是集中式消息队列，kafka是分布式消息队列。

RabbitMQ安装：
①先安装otp_win64_17.3.exe
	推荐使用默认安装路径
	系统用户名必须英文
	计算机名必须英文
	系统用户必须是管理员
②安装rabbitmq-server-3.4.1.exe 
	安装完成后，进入
	rabbitmq-server-3.4.1\sbin>rabbitmq-plugins enable rabbitmq_management
③进入127.0.0.1:15672
	guest\guest 超级管理员
④添加一个用户 users
	airui257/123123
⑤添加虚拟主机 virtual hosts
	/airui257  相当于MySQL中的一个数据库
⑥设置/airui257 权限，把airui257用户添加进去
安装完成

有三个端口：
5672：AMQP协议端口，和Java交互使用的端口
25672：dustering，集群的端口
15672：RabbitMQ管理工具的端口

channels：通道
exchanges：交换(机)
queues：队列

RabbitMQ的jar包
<dependency>
	<groupId>com.rabbitmq</groupId>
	<artifactId>amqp-client</artifactId>
	<version>5.2.0</version>
</dependency>

RabbitMQ的五种队列：
P：代表消息的生产者
红色框：代表队列
C：代表消息的消费者
X：交换机

①简单模式：一对一，(弊端：耦合度太高)
②work模式：(一种是：均衡模式，一种是：能者多劳。channel.basicQos(1)加上这个就是能者多劳);
	消费者1和消费者2获取到的消息内容是不同的，同一个消息只能被一个消费者获取。
	消费者1和消费者2获取到的消息的数量是相同的，一个奇数一个是偶数。
	其实，这样是不合理的，应该是消费者1要比消费者2获取到的消息多才对。
③订阅模式()
	1个生产者，多个消费者
	每一个消费者都有自己的一个队列
	生产者没有将消息直接发送到队列，而是发送到了交换机
	每个队列都要绑定到交换机
	生产者发送的消息，经过交换机，到达队列，实现，一个消息被多个消费者获取的目的。
④路由模式(有选择性的，根据key来判断是否接收消息)
⑤通配符模式()
	将路由键和某模式进行匹配。此时队列需要绑定在一个模式上。符号"#"匹配一个或多个词，符号"*"匹配不多不少一个词。

消息的确认模式：
// 监听队列
channel.basicConsume(QUEUE_NAME,true, consumer); // 第二个参数如果是true，则为自动确认模式。
消费者从队列中获取消息，服务端如何知道消息已经被消费呢？
	模式1：自动确认
		只要消息从队列中获取，无论消费者获取到消息后是否成功消费，都认为是消息已经成功消费。
	模式2：手动确认
		消费者从队列中获取消息后，服务器会将该消息标记为不可用状态，等待消费者的反馈，如果消费者一直没有反馈，那么消息将一直处于不可用状态。

使用RabbitMQ
创建连接：
public class ConnectionUtil {

    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("localhost");
        //端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("/airui257");
        factory.setUsername("airui257");
        factory.setPassword("123123");
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }

}
生产者：
public class Send {

    private final static String QUEUE_NAME = "test_queue";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息内容
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
消费者：
public class Recv {

    private final static String QUEUE_NAME = "test_queue";

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);

        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }
}