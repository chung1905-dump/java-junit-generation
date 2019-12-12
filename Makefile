LIB_DIR = `pwd`/lib
OJ_JAR = $(LIB_DIR)/OpenJava_1.1/openjava.jar
ETOC_JAR = $(LIB_DIR)/etoc/etoc.jar
CP_LIB_FLAG = --class-path $(OJ_JAR):$(ETOC_JAR)
TEST_CLASS = CheckTriangle

compile:
	javac -d ./out -sourcepath ./src $(CP_LIB_FLAG) ./src/app/Main.java

run:
	java $(CP_LIB_FLAG):./out app.Main CheckTriangle

clean:
	rm -rf out/* CheckTriangle.path CheckTriangle.sign CheckTriangle.tgt
