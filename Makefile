LIB_DIR = `pwd`/lib
OJ_JAR = $(LIB_DIR)/OpenJava_1.1/openjava.jar
ETOC_JAR = $(LIB_DIR)/etoc/etoc.jar
JUNIT_JAR = $(LIB_DIR)/junit.jar
CP_LIB_FLAG = --class-path $(OJ_JAR):$(ETOC_JAR):$(JUNIT_JAR)
TEST_CLASS = CheckTriangle

all: compile run

compile:
	javac -d ./out -sourcepath ./src $(CP_LIB_FLAG) ./src/app/Main.java

run:
	java $(CP_LIB_FLAG):./out app.Main CheckTriangle

clean:
	rm -rf out/* CheckTriangle.path CheckTriangle.sign CheckTriangle.tgt
