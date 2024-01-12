$(shell mkdir -p bin)

build: bin/Water.class bin/Terrain.class bin/WaterClickListener.class \
    bin/FlowPanel.class bin/Control.class bin/Flow.class

bin/%.class: src/%.java
	javac -d bin src/*.java

docs:
	javadoc -classpath bin/ -d doc/ src/*.java

clean:
	rm bin/*.class

cleandocs:
	rm doc/*

run:
	@java -cp ./bin Flow $(myvar)
