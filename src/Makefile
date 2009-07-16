CLASSPATH=/usr/lib/eclipse/plugins
all:
	while read path; do \
		echo -n \:$$path;\
	done < JARS > /tmp/jars
	javac -cp .`cat /tmp/jars` `find . -name '*.java'`
