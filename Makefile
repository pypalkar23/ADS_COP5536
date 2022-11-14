JCFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JCFLAGS) *.java

CLASSES = *.java

default: clean classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class