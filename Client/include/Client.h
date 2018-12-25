#ifndef CLIENT_CLIENT_H
#define CLIENT_CLIENT_H

class Client{
private:
    ConnectionHandler connectionHandler;
    bool stop;
public:
    Client(ConnectionHandler connectionHandler);
    void runWriter();
    void runReader();
    ConnectionHandler getConnectionHandler();
    bool getStop();
};



#endif //CLIENT_CLIENT_H
