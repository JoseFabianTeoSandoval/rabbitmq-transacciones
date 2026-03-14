Procesamiento de Transacciones Bancarias con RabbitMQ

Descripción

Este proyecto implementa un sistema distribuido en Java + Maven que procesa transacciones bancarias utilizando RabbitMQ aplicando el patrón Producer–Consumer.

El sistema obtiene transacciones desde una API externa, las distribuye automáticamente a colas según el banco destino y posteriormente un consumidor procesa cada transacción enviándola a una API para su almacenamiento.

Video
Link: https://drive.google.com/open?id=14KTZ7zoSsLVIv0z_RlP-HbzDfP27Cpjz&usp=drive_fs 

🏗 Arquitectura del Sistema
```
API GET
   ↓
Producer (Java)
   ↓
RabbitMQ
(Colas por banco)
   ↓
Consumer (Java)
   ↓
API POST
   ↓
Base de datos (DynamoDB)
```

📦 Tecnologías Utilizadas

Java 17

Maven

RabbitMQ

Jackson (JSON)

Java HttpClient

Postman (pruebas de API)

📁 Estructura de Proyectos
Producer
```
producer-transacciones
│
├── api
│   └── ApiClient.java
│
├── rabbit
│   └── RabbitProducer.java
│
├── model
│   ├── Transaction.java
│   ├── Detalle.java
│   └── Referencias.java
│
└── producer
    └── ProducerApp.java
```

Función:

Consume el endpoint GET /transacciones

Procesa el JSON recibido

Identifica el campo bancoDestino

Envía cada transacción a una cola en RabbitMQ

Consumer
```
consumer-transacciones
│
├── api
│   └── ApiSender.java
│
├── rabbit
│   └── RabbitConsumer.java
│
├── model
│   ├── Transaction.java
│   ├── Detalle.java
│   └── Referencias.java
│
└── consumer
    └── ConsumerApp.java
```

Función:

Escucha múltiples colas en RabbitMQ

Convierte el mensaje JSON a objeto Java

Envía la transacción a la API mediante POST

Confirma el mensaje usando ACK manual

📡 API Utilizadas
GET Transacciones
https://hly784ig9d.execute-api.us-east-1.amazonaws.com/default/transacciones

Devuelve un lote de transacciones simuladas.

POST Guardar Transacción
https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones

Guarda una transacción en la base de datos.

📨 Estructura de Transacción

Ejemplo de transacción procesada:

```
{
"idTransaccion":"TX-10000-JOSETEO",
"monto":2484.14,
"moneda":"GTQ",
"cuentaOrigen":"001-100000-7",
"bancoDestino":"BANRURAL",
"nombre":"Jose Teo",
"carnet":"0905-23-14938",
"correo":"jteos1@miumg.edu.gt",
"createdAt":"2026-03-13T17:18:36Z",
"detalle":{
   "nombreBeneficiario":"Cliente 1",
   "tipoTransferencia":"INTERBANCARIA",
   "descripcion":"Pago simulado",
   "referencias":{
      "factura":"F-80000",
      "codigoInterno":"REF1000"
   }
 }
}
```

⚙ Funcionamiento

1️⃣ Producer consume la API GET
2️⃣ Se obtienen 100 transacciones
3️⃣ Cada transacción se envía a una cola según el banco destino

Ejemplo:
```
BANRURAL
BAC
GYT
BI
```

4️⃣ El Consumer escucha las colas
5️⃣ Cada mensaje se envía a la API POST
6️⃣ Si la API responde correctamente se realiza ACK del mensaje

🛡 Manejo de Errores

Uso de ACK manual

Si el POST falla el mensaje no se confirma

RabbitMQ mantiene el mensaje en cola

Garantiza que no se pierdan transacciones
