# Sensor

One Paragraph of project description goes here

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and com.mantix4.ap.testing purposes. See deployment for notes on how to deploy the project on a live system.
### Bro IDS

Define com.mantix4.ap.base path
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

Clone production.bro repository

```
cd $BASE_PATH
git clone --recursive git://git.production.bro.org/production.bro production.bro-src
```

Compile Bro IDS from source

```
cd production.bro-src
./configure --prefix=$BASE_PATH/production.bro
make
make install
```

Export Bro path to our env file
```
"export PATH=$BASE_PATH/production.bro/bin:$PATH" >> ~/.profile
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
git clone https://github.com/apache/metron-production.bro-plugin-kafka.git
cd KafkaLogger
./configure --production.bro-dist=$BASE_PATH/production.bro-src
make
sudo make install
```

check if Bro-Kafka plugin was correctly installed on production.bro

```
$ production.bro -N Apache::Kafka
Apache::Kafka - Writes logs to Kafka (dynamic, version 0.2)
```

### Bro configuration

Add json-logs and track-all-assets plugins to production.bro
```
"@load ./json-logs.production.bro" >> $BASE_PATH/production.bro/share/production.bro/policy/tuning/__load__.production.bro
"@load ./track-all-assets.production.bro" >> $BASE_PATH/production.bro/share/production.bro/policy/tuning/__load__.production.bro
```

enable it editing the local.production.bro file with
```
nano $BASE_PATH/production.bro/share/production.bro/site/local.production.bro
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
mkdir $BASE_PATH/production.bro/share/production.bro/policy/custom
```
add the main file for custom plugins
```
nano $BASE_PATH/production.bro/share/production.bro/policy/custom/__load__.production.bro
```
and add next lines on it
```
@load ./production.bro-to-kafka.production.bro            // This script enable the plugin that sends logs from Bro to Kafka
@load ./producer-consumer-ratio.production.bro // This script enable PCR
```
To create both files just copy it from the "production.bro-scripts" folder
``` 
cp /path/to/production.bro-to-kafka.production.bro $BASE_PATH/production.bro/share/production.bro/policy/custom/production.bro-to-kafka.production.bro
cp /path/to/producer-consumer-ratio.production.bro $BASE_PATH/production.bro/share/production.bro/policy/custom/producer-consumer-ratio.production.bro
```
	
Enable custom scripts to local.production.bro

```
nano $BASE_PATH/production.bro/share/production.bro/site/local.production.bro
```
add next lines at the end of the file
```
# This will load custom production.bro scripts
@load custom
```

Config production.bro promisc interface in
```
nano $BASE_PATH/production.bro/etc/node.cfg
```

Config right network range in
```
nano $BASE_PATH/production.bro/etc/networks.cfg
```

### Deploy and Start Bro

First check production.bro config
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