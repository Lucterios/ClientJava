#root makefile.  Delegate to source subdirs

export TOP_DIR = $(PWD)

PACKAGE = .

SUBDIRS = \
	org/lucterios/utils \
	org/lucterios/utils/graphic \

ifdef clear

ifdef simple
all: clean compileclass
else
all: clean compileclass package
endif

else

ifdef simple
all: compileclass
else
all: compileclass package
endif

endif

compileclass:
	@@for p in $(SUBDIRS); do \
		echo ''; \
		echo ' +++++++++ building ' $(PACKAGE)/$$p; \
		make -C $(PACKAGE)/$$p --no-print-directory all; \
	done

sources:
	-rm ../LucteriosUtils_Source.zip
	zip ../LucteriosUtils_Source Makefile Makefile.include Manifest.txt org/lucterios/utils/resources/*
	@@for p in $(SUBDIRS); do \
		zip LucteriosUtils_Source $(PACKAGE)/$$p/Makefile;\
		zip LucteriosUtils_Source $(PACKAGE)/$$p/*.java;\
	done

package:
ifneq ($(NOJAR), 1)	
	$(JAVA_HOME)/bin/jar cvfm LucteriosUtils.jar Manifest.txt org
	cp -vf LucteriosUtils.jar ../lib
else
	@echo 'no jar'
endif

clean:
	@@for p in $(SUBDIRS); do \
		echo ''; \
		echo ' --------- clearing ' $(PACKAGE)/$$p; \
		echo 'cleaning ' $(PACKAGE)/$$p; \
		make -C $(PACKAGE)/$$p --no-print-directory clean; \
	done
