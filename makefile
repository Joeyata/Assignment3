BUILD_DIR = ./src/build/

all: build

build:
	mkdir -p $(BUILD_DIR);
	javac -d $(BUILD_DIR) src/Email.java src/Acceptor.java src/Proposer.java src/Learner.java


launch-learner:
	java -cp src/build Learner;

launch-acceptor:
	java -cp src/build Acceptor $(member);

launch-proposer:
	java -cp src/build Proposer $(member);
