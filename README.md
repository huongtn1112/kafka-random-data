# kafka-random-data

* Use java faker library  to random data and push data into kafka.
* Register avro schema in schema registry
```php

curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
 --data '{"schema": "{  \"name\": \"MyClass\",  \"type\": \"record\",  \"namespace\": \"com.acme.avro\",  \"fields\": [    {      \"name\": \"id\",      \"type\": \"int\"    },    {      \"name\": \"title\",      \"type\": \"string\"    },    {      \"name\": \"description\",      \"type\": \"string\"    },    {      \"name\": \"price\",      \"type\": \"int\"    },    {      \"name\": \"discountPercentage\",      \"type\": \"int\"    },    {      \"name\": \"rating\",      \"type\": \"int\"    },    {      \"name\": \"stock\",      \"type\": \"int\"    },    {      \"name\": \"brand\",      \"type\": \"string\"    },    {      \"name\": \"thumbnail\",      \"type\": \"string\"    },    {      \"name\": \"images\",      \"type\": {        \"type\": \"array\",        \"items\": \"string\"      }    }  ]}"}' \ 
http://10.110.81.178:8081/subjects/productinformationdev-value/versio
```
