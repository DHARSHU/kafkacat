/webApps/kafka/bin/kafkacat -H payload_version=1.0.0 -H batch_number=test -H action=add  -P -l -b localhost:9092 -t recipe-ingredient heartbeat.json