BUILD_DIR = ./src/build/

all: build

build:
	mkdir -p $(BUILD_DIR);
	javac -d $(BUILD_DIR) src/Email.java src/Acceptor.java src/Proposer.java


launch-proposer:
	java -cp src/build Acceptor $(port_number);

launch-acceptor:
	java -cp src/build Proposer $(port_number);
