//#include <thread>
#include "../include/ConnectionHandler.h"
#include "../include/Client.h"
#include <boost/asio.hpp>

/**
 * This class represents a client, that sends messages to the server and recives messages from it.
 */

/**
 * Client's constructor.
 *
 * @param host -  the connection handler's host.
 * @param port - the connection handler's port.
 */
Client::Client(std::string host, short port): connectionHandler(host, port), stop(false){}

/**
 * The method that the thread threadWrite is responsible for.
 */
void Client::runWriter(){
    while(!this->stop){
        if (this->connectionHandler.getIsLoggedOut()==false) {
            const short bufsize(1024);
            char buf[bufsize];
            std::cin.getline(buf, bufsize); // getting a new line from the user
            std::string line(buf);

            connectionHandler.sendLine(line); // if it wasn't possible to send the line from the user break;
        }
    }
}

/**
 * The method that the thread threadRead is responsible for.
 */
void Client::runReader(){
    while(!this->stop && this->connectionHandler.getIsLoggedOut()==false) {
        if (this->connectionHandler.getIsLoggedOut()==false){
            std::string answer;

            connectionHandler.getLine(answer); // if it wasn't possible to get the answer from the server

            if (answer!=""){
                std::cout <<answer <<std::endl;

                std::string::size_type index(answer.find('A', 0));
                if (index<answer.length()){
                    std::string s = answer.substr(index, index+5);
                    if (s.compare("ACK 3") == 0){
                        this->stop = true;
                        this->getConnectionHandler().close();
                    }
                }
            }
        }
    }
}

/**
 * This method returns a pointer to the client's connection handler.
 *
 * @return - a pointer to the client's connection handler.
 */
ConnectionHandler& Client::getConnectionHandler(){
    return this->connectionHandler;
}

/**
 * This method returns whether the client's threads need to stop or not.
 *
 * @return - whether the client's threads need to stop or not.
 */
bool Client::getStop(){
    return this->stop;
}


/**
 * Destructor - this method destructs this client.
 */
Client :: ~Client() {
    this->connectionHandler.close();
}
