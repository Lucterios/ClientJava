#root makefile.  Delegate to source subdirs

export TOP_DIR = $(PWD)

PACKAGE = .

SUBDIRS = \
	org/lucterios/Print/Data \
	org/lucterios/Print/Observer \
	org/lucterios/Print/GUI \
	org/lucterios/Print 

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
	-rm ../LucteriosPrint_Source.zip
	zip ../LucteriosPrint_Source Makefile Makefile.include Manifest.txt org/lucterios/Print/resources/*
	@@for p in $(SUBDIRS); do \
		zip LucteriosPrint_Source $(PACKAGE)/$$p/Makefile;\
		zip LucteriosPrint_Source $(PACKAGE)/$$p/*.java;\
	done

package:
ifneq ($(NOJAR), 1)	
	$(JAVA_HOME)/bin/jar cvfm LucteriosPrint.jar Manifest.txt org
	cp -vf LucteriosPrint.jar ../lib
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
