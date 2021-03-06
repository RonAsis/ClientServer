#ifndef CLIENT_CLIENT_H
#define CLIENT_CLIENT_H
#include <thread>
#include "ConnectionHandler.h"

class Client{
private:
    ConnectionHandler connectionHandler;
    bool stop;
public:
    Client(std::string host, short port);
    ~Client(); //destructor
    void runWriter();
    void runReader();
    ConnectionHandler& getConnectionHandler();
    bool getStop();
};

#endif //CLIENT_CLIENT_H
