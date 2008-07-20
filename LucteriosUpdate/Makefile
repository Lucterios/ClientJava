#root makefile.  Delegate to source subdirs

export TOP_DIR = $(PWD)

PACKAGE = .

SUBDIRS = \
	org/lucterios/update \

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
	-rm ../LucteriosUpdate_Source.zip
	zip ../LucteriosUpdate_Source Makefile Makefile.include Manifest.txt org/lucterios/update/resources/*
	@@for p in $(SUBDIRS); do \
		zip LucteriosUpdate_Source $(PACKAGE)/$$p/Makefile;\
		zip LucteriosUpdate_Source $(PACKAGE)/$$p/*.java;\
	done

package:
ifneq ($(NOJAR), 1)	
	$(JAVA_HOME)/bin/jar cvfm LucteriosUpdate.jar Manifest.txt org
	cp LucteriosUpdate.jar ..
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
