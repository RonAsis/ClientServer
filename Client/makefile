# All Targets
all: BGSclient

# Tool invocations
# Executable "Client" depends on the files inside bin folder
BGSclient: bin/Client.o bin/ConnectionHandler.o bin/ClientMain.o
	@echo 'Building target: BGSclient'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/BGSclient bin/Client.o bin/ConnectionHandler.o bin/ClientMain.o -lboost_system -lpthread
	@echo 'Finished building target: BGSclient'
	@echo ' '

# Depends on the source and header files
bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

# Depends on the source and header files
bin/Client.o: src/Client.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Client.o src/Client.cpp

# Depends on the source and header files
bin/ClientMain.o: src/ClientMain.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/ClientMain.o src/ClientMain.cpp

#Clean the build directory
clean: 
	rm -f bin/*