# Sensor

One Paragraph of project description goes here

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.
### Bro IDS

Define base path
```
BASE_PATH=/opt/mantix4/sensor
SENSOR_ID="YOUR_SENSOR_ID"
```

Update system

```
sudo apt-get update && sudo apt-get -y dist-upgrade && sudo apt-get autoremove -y
```

Install dependencies for DEB/Debian-based Linux
```
sudo apt-get install cmake make gcc g++ flex bison libpcap-dev libssl-dev python-dev swig zlib1g-dev
```

Clone bro repository

```
cd $BASE_PATH
git clone --recursive git://git.bro.org/bro bro-src
```

Compile Bro IDS from source

```
cd bro-src
./configure --prefix=$BASE_PATH/bro
make
make install
```

Export Bro path to our env file
```
"export PATH=$BASE_PATH/bro/bin:$PATH" >> ~/.profile
```

### Librdkafka

```
cd $BASE_PATH
curl -L https://github.com/edenhill/librdkafka/archive/0.8.6.tar.gz | tar xvz
cd librdkafka-0.8.6/
./configure
```
Remove the tag  "-Werror" insdide the file  Makefile.config, then we can compile it
```
make
sudo make install
```

### Metron Bro Plugin Kafka

```
cd $BASE_PATH
git clone https://github.com/apache/metron-bro-plugin-kafka.git
cd KafkaLogger
./configure --bro-dist=$BASE_PATH/bro-src
make
sudo make install
```

check if Bro-Kafka plugin was correctly installed on bro

```
$ bro -N Apache::Kafka
Apache::Kafka - Writes logs to Kafka (dynamic, version 0.2)
```

### Bro configuration

Add json-logs and track-all-assets plugins to bro
```
"@load ./json-logs.bro" >> $BASE_PATH/bro/share/bro/policy/tuning/__load__.bro
"@load ./track-all-assets.bro" >> $BASE_PATH/bro/share/bro/policy/tuning/__load__.bro
```

enable it editing the local.bro file with
```
nano $BASE_PATH/bro/share/bro/site/local.bro
```
and replace the line
```
@load tuning/defaults
```
for
```
@load tuning
```

### Bro-Kafka plugin configuration
```
mkdir $BASE_PATH/bro/share/bro/policy/custom
```
add the main file for custom plugins
```
nano $BASE_PATH/bro/share/bro/policy/custom/__load__.bro
```
and add next lines on it
```
@load ./bro-to-kafka.bro            // This script enable the plugin that sends logs from Bro to Kafka
@load ./producer-consumer-ratio.bro // This script enable PCR
```
To create both files just copy it from the "bro-scripts" folder
``` 
cp /path/to/bro-to-kafka.bro $BASE_PATH/bro/share/bro/policy/custom/bro-to-kafka.bro
cp /path/to/producer-consumer-ratio.bro $BASE_PATH/bro/share/bro/policy/custom/producer-consumer-ratio.bro
```
	
Enable custom scripts to local.bro

```
nano $BASE_PATH/bro/share/bro/site/local.bro
```
add next lines at the end of the file
```
# This will load custom bro scripts
@load custom
```

Config bro promisc interface in
```
nano $BASE_PATH/bro/etc/node.cfg
```

Config right network range in
```
nano $BASE_PATH/bro/etc/networks.cfg
```

### Deploy and Start Bro

First check bro config
```
broctl check
```
and thet set up Bro config and install it
```
broctl install
broctl start
```
or just do
```
broctl deploy
```