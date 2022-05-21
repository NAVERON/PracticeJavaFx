package org.practicefx.utils;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.practicefx.CommonConstant;
import org.practicefx.models.TrackView;



/**
 * kafka 生产者方法类 
 * 对外提供队列发送消息的服务 
 * @author eron
 *
 */
public class KafkaUtils {
    
    private static final Logger LOGGER = Logger.getLogger(KafkaUtils.class.getName());
    
    private static KafkaProducer<String, String> kafkaProducer;
    private static KafkaConsumer<String, String> kafkaConsumer;
    
    public static KafkaProducer<String, String> getKafkaProducer() {
        if(kafkaProducer == null) {
            synchronized (KafkaUtils.class) {
                if(kafkaProducer == null) {
                    // 初始化 
                    Properties properties = new Properties();
                    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CommonConstant.KAFKA_CONNECTION_CONFIG);
                    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                    properties.put(ProducerConfig.ACKS_CONFIG, "all");
                    
                    kafkaProducer = new KafkaProducer<String, String>(properties);
                }
            }
        }
        
        return kafkaProducer;  // 县城安全, 不需要单例模式也可以直接使用 
    }
    
    public static KafkaConsumer<String, String> getKafkaConsumer() {
        if(kafkaConsumer == null) {
            synchronized (KafkaUtils.class) {
                if(kafkaConsumer == null) {
                    // 初始化 
                    Properties properties = new Properties();
                    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, CommonConstant.KAFKA_CONNECTION_CONFIG);
                    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "xxx");
                    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
                    
                    kafkaConsumer = new KafkaConsumer<String, String>(properties);
                    kafkaConsumer.subscribe(Collections.singleton(CommonConstant.KAFKA_TRACK_TOPIC));
                }
            }
        }
        
        return kafkaConsumer;
    }
    
    // 将船舶轨迹点推送到kafka队列中 回调函数由外部实现 
    public static void pushTracks(TrackView track, Callback callback) {
        // 推送轨迹点序列到 云存储 
        // String trackJson = GsonUtils.toJsonString(track);
        String trackJson = JsonUtil.formatTrackToString(track);
        LOGGER.info("轨迹点转换成json  准备发送队列 ==> " + trackJson);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(CommonConstant.KAFKA_TRACK_TOPIC, trackJson);  
        LOGGER.info("发送内容 ==" + record.toString());
        // kafka调用必须这中央是获取 保证单例
        Future<RecordMetadata> sendStatus = callback == null 
                ? getKafkaProducer().send(record)  // 类方法内部调用方法可以省略 this/className
                : getKafkaProducer().send(record, callback);
        
        try {
            LOGGER.info("方法内部结果状态 = " + sendStatus.get().offset());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    public static void consumTrackForTest() {
        // 消费队列内容 
        LOGGER.info("消费段获取数据");
        ConsumerRecords<String,String> records = getKafkaConsumer().poll(Duration.ofMillis(100));
        records.forEach((x) -> {
            LOGGER.info("消费段获取到 -> " + x.value());
        });
    }

    @Override
    protected void finalize() throws Throwable {  // 销毁连接 
        getKafkaProducer().close();
    }
    
}









