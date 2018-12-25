#include <stdlib.h>
#include <connectionHandler.h>

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    Client client(connectionHandler, id); // creating a new client

    std::thread threadWrite (&Client::runWriter, &client);
    std::thread threadRead (&Client::runRead, &client);
    threadWrite.join();
    threadRead.join();

    if (client.getStop == true) {
        threadWrite.stop();
        threadRead.stop();
        client.getConnectionHandler().close();
    }

    return 0;
}