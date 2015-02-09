program_NAME := mooninvaders
program_SRCS := $(wildcard src/*.java)

MKDIR := mkdir -p
RM := rm -rf
JAVAC := javac
JAR := jar

.PHONY: all clean install

all: $(program_NAME)

$(program_NAME): $(program_SRCS)
	$(JAVAC) -Xlint:unchecked $(program_SRCS) -d ./
	cp -r resources/cursors/ mooninvaders/cursors
	cp -r resources/images/ mooninvaders/images
	cp -r resources/sounds/ mooninvaders/sounds
	jar cvfm mooninvaders.jar src/manifest.txt mooninvaders/*.class mooninvaders/cursors mooninvaders/images mooninvaders/sounds

clean:
	-@ $(RM) "$(program_NAME).jar"
	-@ $(RM) -rf "mooninvaders"
