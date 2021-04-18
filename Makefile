# based on this answer: https://stackoverflow.com/a/29438033

JAVAC = javac
SOURCES = comp3100/Client.java
CLASSES = $(SOURCES:.java=.class)

all: $(CLASSES)

clean:
	rm -f $(shell find . -type f -name "*.class")

$(CLASSES): $(SOURCES)
	$(JAVAC) $<
