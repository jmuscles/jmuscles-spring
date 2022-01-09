## Introduction

“jmuscles-rabbitmq” is a solution to implement the event driven architecture integrating with rabbit-mq. It provides a mechanism to execute the backend rest services and db updates (sql query/procedure) with some configuration updates without any code changes. It also addresses some of the common challenges/issues for improved architecture -especially for resiliency to achieve eventual consistency.

#### Below are some of the highlights of the framework:
 1.  Rest, SQL query and procedure execution by configuration with no additional code.
 2.  One message can consist the data for multiple calls and they can be executed in parallel or sequential mode as needed.
 3.  Robust error handling and retry mechanism and ability to shelve the message if needed to execute after delay.
 4.  Can connect to several and various types of databases for sql query/procedure executions.
 5.  Can be easily enhanced/customized for special needs.

## Framework architecture

Framework has 2 main components: 
1. Rabbitmq producer library
2. Rabbitmq consumer library

#### End-to-end flow

![image2](https://user-images.githubusercontent.com/91483360/147899328-33136644-ad1a-4ff2-85eb-a981932bbae2.jpg)


Ideal implementation of the framework should have the below components:



1. Producer app integrated with producer library
2. Consumer app integrated with consumer library
3. DB 
    1. To store the message from abandoned queue
    2. Fallback to store the message at producer end 
4.  Batch to read message from DB and
    3. Send the message back to rabbitmq
    4. Or process directly reading them from DB


#### Understanding the Consumer

![image6](https://user-images.githubusercontent.com/91483360/147899336-2164fe9d-72af-4f75-ad3e-d1b483a22c02.jpg)


## Quick start guide 
Refer to [jmuscles-rabbitmq-consumer-demo](https://github.com/jmuscles/jmuscles-rabbitmq-consumer-demo/blob/main/README.md) project

## Understanding message structure

![image5](https://user-images.githubusercontent.com/91483360/147899062-720ecf91-a2dc-46ce-8a0a-ba7f2ee62dc3.jpg)

Below is the sample json of message body:

```
{
    "requestDataList": [
        {
            "@type": "rest",
            "method": "GET",
            "configKey": "getDemoUser",
            "urlSuffix": "/123",
            "body": null,
            "httpHeader": {
                "key1": "value1",
                "key2": "value2",
                "key3": "value3"
            }
        },
        {
            "@type": "sql-query",
            "params": {
                "gender": "Male",
                "user_id": "1",
                "last_name": "Goel",
                "first_name": "Manish",
                "email": "a@a.com"
            },
            "configKey": "sql-query-config-key"
        },
        {
            "@type": "sql-procedure",
            "params": {
                "gender": "Male",
                "user_id": "1",
                "last_name": "Goel",
                "first_name": "Manish",
                "email": "a@a.com"
            },
            "configKey": "sql-procedure-config-key"
        }		
    ]
}
```


#### "@type" value in requestDataList
1. DemoRequestData ->  "demo"
2. RestRequestData ->  "rest"
3. SequentialRequestData ->  "sequential"
4. SQLProcedureRequestData ->  "sql-procedure"
5. SQLQueryRequestData ->  "sql-query"


## Understanding configurations

Below sample configurations of demo project can provide the better understanding. Let's look in to it.

[https://github.com/jmuscles/jmuscles-rabbitmq-consumer-demo/tree/main/src/main/resources](https://github.com/jmuscles/jmuscles-rabbitmq-consumer-demo/tree/main/src/main/resources)

If you look in to the application.yml file it includes below other yml files:

* “application-rabbitmq-setup-props.yml”
* “application-executors-props.yml”
* “application-producer-props.yml”
* “application-datasource-props.yml”

I would recommend having a similar structure to segregate the properties for improved and easy maintenance.

Let’s understand these configuration files


### Rabbitmq configurations (application-rabbitmq-setup-props.yml):

Consumer app creates/updates the the rabbit-mq entities (queues, exchanges etc) during its startup


#### Setup configuration 
```
rabbitmq-config:
   exchanges:
   -  name: EXCHANGE_1
      type: direct
      parent: null
   -  name: EXCHANGE_2
      type: direct
      parent: EXCHANGE_1

   queueSetsConfig:
      demoQueueSetConfig:
         exchange: EXCHANGE_1
         name: DEMO_QUEUE
         retrySetupDisabled: false
         arguments:
            primary:
               -x-queue-type: quorum
            retry:
               -x-queue-type: quorum
            abandoned:
               -x-queue-type: quorum
      anotherDemoQueueSetConfig:
         exchange: EXCHANGE_2
         name: ANOTHER_DEMO_QUEUE
         retrySetupDisabled: true
         arguments:
            primary:
               -x-queue-type: quorum
            retry:
               -x-queue-type: quorum
            abandoned:
               -x-queue-type: quorum
```

“exchanges” is a list and multiple exchanges can be created by defining the above configuration.

“queueSetsConfig” is a Map and in above example “demoQueueSetConfig” is the key and it holds the configuration for one queue set. e.g. for the queue name DEMO_QUEUE, it will create 4 queues as 1. DEMO_QUEUE  2. DEMO_QUEUE_retry 3. DEMO_QUEUE_wait 4. DEMO_QUEUE_abandoned

Note: 



1. Retry & wait queues are optional and can be skipped from creation. Wait queue is attached to retry queue so if “retrySetupDisabled” is true, both of them are not created.
2. Setup can’t be altered at run time.


#### Processing configuration:

“queueSetsConfig” & “queueSetsProcessingConfig” both are Map and should have the common keys (e.g. "demoQueueSetConfig") to link the configurations.

```
  queueSetsProcessingConfig:
      demoQueueSetConfig:
         primary:
            disableProcessing: false
            listenerType: RequeueModifiedMessageListener
            concurrency: 1-5
            processor: asyncPayloadMessageProcessor
         retry:
            disableProcessing: false
            listenerType: RequeueModifiedMessageListener
            concurrency: 1-5
            processor: asyncPayloadMessageProcessor
            retryOnlyConfig:
               acceptingMessage: true
               retryAttempt: 2
               retryAfterDelay: true
               retryInterval:
                  - 300
                  - 600
                  - 1200
         abandoned:
            disableProcessing: true
            listenerType: RequeueModifiedMessageListener
            concurrency: 1-5
            processor: persistMessageToDBProcessor

```
See the sample configuration and below are few things to highlight:



1. Message processing in a queue can be stopped at any point by turning on the disableProcessing flag.
2. Currently it comes with 2 processor 
    1. asyncPayloadMessageProcessor : regular message processing
    2. persistMessageToDBProcessor : saving messages in to DB
3. In order to create the DB tables- db scripts are available in “jmuscles-rabbitmq-consumer-demo” project or “hibernate.hbm2ddl.auto: update” property in application-producer-props.yml file will create the required table once connected to defined DB.
4. Concurrency can be helpful in vertical and horizontal scaling of consumer applications. Will be covered in scaling section.
5. Change in processing configuration can be effective immediately at run time throuh actuator refresh.
    1. make a post call to  "/actuator/refresh" for runtime properties updates
6. Currently it provides only RequeueModifiedMessageListener and it can process the partial message and requeue the unprocessed part of message E.g. If a message has data for 5 calls and only 2 are processed then 3 will be requeued in the next stage. 


### Executor configurations (application-executors-props.yml)


#### Sample configurations:

```
jmuscles:
   executors-config:
      restConfig:
         connectionTimeout: 10000
         readTimeout: 10000
      restCalls:
         createDemoUser:
            url: https://gorest.co.in/public/v1/users
            successCodePatterns:
               200: null
               401: null
      sqlProcedures:
         demo_proc_1:
            dskey: null
            procedure: procedure_name
            successKey: P_RESP_CODE
            successValue: '000'
      sqlQueries:
         insert_user_testdb:
            dskey: testdb
            query: insert into test.user(user_id, first_name, last_name, email, gender) values(:user_id, :first_name, :last_name,  :email, :gender);
            successResponse: null
```

#### Rest

```
restCalls:
         createDemoUser:
            url: https://gorest.co.in/public/v1/users
            successCodePatterns:
               200: null
               401: null
	validator: SimpleRestValidator
```
      

“createDemoUser” is the value of configKey in the message for the rest call where "@type" value is  "rest".

“successCodePatterns” helps in validating the success- once the consumer executes the rest call. It is Map&lt;Integer, List&lt;String>> where keys (Integer) are the http response codes and value (List&lt;String>) are the string pattern against the response body to match the success scenarios.

Validator is optional with default value -SimpleRestValidator. It can be useful for specific needs and can be customized by inheriting the abstract RestValidator (check the "Extension and customization" section).


#### SQL procedure/query

```
      sqlProcedures:
         demo_proc_1:
            dskey: null
            procedure: procedure_name
            successKey: P_RESP_CODE
            successValue: '000'
      sqlQueries:
         insert_user_testdb:
            dskey: testdb
            query: insert into test.user(user_id, first_name, last_name, email, gender)  values(:user_id, :first_name, :last_name,  :email, :gender);
            successResponse: null
```
“dskey” is the datasource key and data sources are defined in application-datasource-props.yml

There are default validators SimpleSQLProcedureValidator/SimpleSQLQueryValidator and can be customized by inheriting the abstract SQLProcedureValidator/SQLQueryValidator.

### Async DB configurations (application-producer-props.yml)
Messages from the abandoned queue will be sent to the DB if the abandoned queue is linked with persistMessageToDBProcessor(check the rabbitmq processing configurations) and this processor requires the DB producer

```
async-producer-config:
   database: 
      datasourceKey: asyncdb
      jpaProperties:
         hibernate.dialect: org.hibernate.dialect.MySQL5Dialect
         hibernate.hbm2ddl.auto: update
         hibernate.show_sql: true
         hibernate.format_sql: true
```

### Datasource configurations (application-datasource-props.yml)
Please refer to [jmuscles-datasource project guide](https://github.com/jmuscles/jmuscles-spring/tree/main/jmuscles-spring-datasource-starter#readme)

## Processing-schema and rabbitmq-producer library


1. “jmuscles-processing-schema” is a jar which helps to create the message in java.
2. “jmuscles-rabbitmq-producer” is the spring boot starter library 
    1. And in addition to creating the message it also helps in posting the message into rabbitmq with other additional features. 
    2. It provides the configurable fall back mechanism to deal with the message e.g. sending it to the database or running it there itself.
3. Below is the sample configuration used by producer library:

```
async-producer-config:
   activeProducersInOrder: 
      - rabbitmq
      - database
      - syncProcessing
   rabbitmq: 
      defaultRoutingKey: DEMO_QUEUE
      defaultExchange: EXCHANGE_1
      nonPersistentDeliveryMode: false
   database: 
      datasourceKey: asyncdb
      jpaProperties:
         hibernate.dialect: org.hibernate.dialect.MySQL5Dialect
         hibernate.hbm2ddl.auto: update
         hibernate.show_sql: true
         hibernate.format_sql: true  
```

4. Producer library will create the AsyncPayloadDeliverer in the application, which provides below 2 methods to deal with messages.
    3. send(Payload asyncPayload, TrackingDetail trackingDetail)
    4. send(Payload asyncPayload, TrackingDetail trackingDetail, String routingKey, String exchange, ProducerConfigProperties asyncProducerConfig, boolean isNonPersistentDeliveryMode)



## Logs and monitoring


## Scalability


## Resiliency



1. Framework provides robust error handling and multiple fallback options to make sure the eventual consistency.
2. When a message is partially processed  in one go (if it has data for multiple calls). Only remainder of calls of message will be requeued in the next stage.


## Extension and customization
1. Framework can be extended for special requirements by adding the new data type and executor. Please check the [customization/demo](https://github.com/jmuscles/jmuscles-rabbitmq-consumer-demo/tree/main/src/main/java/com/jmuscles/async/consumer/customization/demo) package in the demo project 
2. Note 
    1. New data types need to be registered in the object mapper. Check the RegisterJacksonSubtype class in the above package.
    2. In case of successful execution of the execute method, Custom executor must return null.
    3. And In case of an unsuccessful scenario the returned request data will be requeued in the next stage. If it throws the exception then original request data will be requeued.
3.  Framework also allows adding a custom validator for existing request types and executors. It can be easily achieved by extending the RestValidator, SQLProcedureValidator/SQLQueryValidator or SelfRegisteredValidator and exposing it as a spring bean.
