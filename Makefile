# based on this answer: https://stackoverflow.com/a/29438033

JAVAC = javac
SOURCES = $(shell find . -type f -name '*.java')
CLASSES = $(SOURCES:.java=.class)

all: $(CLASSES)

clean:
	rm -f $(CLASSES)

$(CLASSES): $(SOURCES)
	$(JAVAC) $<