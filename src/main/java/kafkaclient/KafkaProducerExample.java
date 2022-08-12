package kafkaclient;

import com.github.javafaker.Faker;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class KafkaProducerExample {
    private final static String TOPIC = "productinformationdev";
    private final static String BOOTSTRAP_SERVERS = "10.110.81.178:9092,10.110.81.179:9092,10.110.81.180:9092";
    private final static String SCHEMA_REGISTRY_URL = "http://10.110.81.178:8081";
    private final static String LOCAL_SCHEMA_PATH = "src/main/resources/product.avsc";
    private final static Schema schema;


    private final static int nProduct = 10000;

    static {
        try {
            schema = new Schema.Parser().parse(new File(LOCAL_SCHEMA_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Producer<String, GenericRecord> createProducer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);

        return new KafkaProducer<>(props);
    }

    static void runProducer() {
        final Producer<String, GenericRecord> producer = createProducer();
        Faker faker = new Faker();

        for (int i = 0; i < nProduct; i ++){
            int id = faker.number().randomDigit();
            String title = faker.name().title();
            String description = faker.commerce().color();
            int price = faker.number().numberBetween(1, 10000);
            float discountPercentage = faker.number().numberBetween(0, 100);
            float rating = faker.number().numberBetween(1, 5);
            float stock = faker.number().numberBetween(100, 10000);
            String brand = faker.company().name();
            String thumbnail = faker.internet().image();

            ArrayList<String> images = new ArrayList<String>();
            int nImages = 3;
            for(int k = 0; k < nImages; k++){
               images.add(faker.internet().image());
            }

            GenericRecord product = new GenericData.Record(schema);
            product.put("id", id);
            product.put("title", title);
            product.put("description", description);
            product.put("price", price);
            product.put("discountPercentage", discountPercentage);
            product.put("rating", rating);
            product.put("stock", stock);
            product.put("brand", brand);
            product.put("thumbnail", thumbnail);
            product.put("images", images);


            ProducerRecord<String, GenericRecord> data = new ProducerRecord<String, GenericRecord>(TOPIC, String.format("%s %s", title, brand), product);
            producer.send(data);
            System.out.println("Send successfully!!!");
            try {
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            runProducer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
