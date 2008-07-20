#root makefile.  Delegate to source subdirs

all: 
	cd ./LucteriosUtils && make all
	cd ./LucteriosPrint && make all
	cd ./LucteriosClient && make all
	cd ./LucteriosUpdate && make all

sources: 
	cd ./LucteriosUtils && make sources
	cd ./LucteriosPrint && make sources
	cd ./LucteriosClient && make sources
	cd ./LucteriosUpdate && make sources

clean:
	cd ./LucteriosUtils && make clean
	cd ./LucteriosPrint && make clean
	cd ./LucteriosClient && make clean
	cd ./LucteriosUpdate && make clean
