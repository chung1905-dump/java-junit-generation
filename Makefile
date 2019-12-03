LIB_DIR = `pwd`/lib

compile:
	javac -d ./out ./src/app/*.java \
# --module-path /usr/App/lib/javafx-sdk-11.0.2/lib -Xlint:unchecked \
# --add-modules=javafx.base,javafx.controls,javafx.fxml \
# -sourcepath ./src ./src/*.java ./src/model/*.java

test:
	java -cp ./out app.CheckTriangle

gen_path_oj:
	java --class-path $(LIB_DIR)/OpenJava_1.1/classes:$(LIB_DIR)/etoc/etoc.jar:`pwd`/src openjava.ojc.Main -d ./out ./src/app/CheckTriangle.oj

gen_chromosomes:
	java --class-path $(LIB_DIR)/OpenJava_1.1/classes:$(LIB_DIR)/etoc/etoc.jar:`pwd`/src
