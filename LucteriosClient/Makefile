#root makefile.  Delegate to source subdirs

export TOP_DIR = $(PWD)

PACKAGE = .

SUBDIRS = \
	org/lucterios/client/utils \
	org/lucterios/client/transport \
	org/lucterios/client/presentation \
	org/lucterios/client/application \
	org/lucterios/client/application/comp \
	org/lucterios/client/application/observer \
	org/lucterios/client 

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
	-rm ../LucteriosClient_Source.zip
	zip ../LucteriosClient_Source Makefile Makefile.include Manifest.txt org/lucterios/client/resources/*
	@@for p in $(SUBDIRS); do \
		zip LucteriosClient_Source $(PACKAGE)/$$p/Makefile;\
		zip LucteriosClient_Source $(PACKAGE)/$$p/*.java;\
	done

package:
ifneq ($(NOJAR), 1)	
	$(JAVA_HOME)/bin/jar cvfm LucteriosClient.jar Manifest.txt org
	cp LucteriosClient.jar ..
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
