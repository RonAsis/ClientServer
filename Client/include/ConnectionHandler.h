#ifndef CONNECTION_HANDLER__
#define CONNECTION_HANDLER__

#include <string>
#include <iostream>
#include <boost/asio.hpp>

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
    char delimiter;
    const std::string host_;
    const short port_;
    boost::asio::io_service io_service_;   // Provides core I/O functionality
    tcp::socket socket_;
    short messageTypeShort(std::string messageTypeName);
    std::string messageTypeString(short messageTypeName);
    void shortToBytes(short num, char* bytesArr);
    std::string changeStringToMessage(std::string messageTypeName, std::string messageContent);
    short bytesToShort(char* bytesArr);
    std::string changeMessageToString(std::string answer, std::string messageContent, std::string messageType);
public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();

    // Connect to the remote machine
    bool connect();

    // Read a fixed number of bytes from the server - blocking.
    // Returns false in case the connection is closed before bytesToRead bytes can be read.
    bool getBytes(char bytes[], unsigned int bytesToRead);

    // Send a fixed number of bytes from the client - blocking.
    // Returns false in case the connection is closed before all the data is sent.
    bool sendBytes(const char bytes[], int bytesToWrite);

    // Read an ascii line from the server
    // Returns false in case connection closed before a newline can be read.
    bool getLine(std::string& line);

    // Send an ascii line from the server
    // Returns false in case connection closed before all the data is sent.
    bool sendLine(std::string& line);

    // Get Ascii data from the server until the delimiter character
    // Returns false in case connection closed before null can be read.
    bool getFrameAscii(std::string& frame, char delimiter);

    // Send a message to the remote host.
    // Returns false in case connection is closed before all the data is sent.
    bool sendFrameAscii(const std::string& frame, char delimiter);

    // Close down the connection properly.
    void close();

    const std::string getHost();

    short getPort();

   // ConnectionHandler & operator=(const ConnectionHandler & other); //copy assignment

    ConnectionHandler(const ConnectionHandler & other); //copy constructor

    // ConnectionHandler(ConnectionHandler && other); //Move constructor

   // ConnectionHandler & operator=(ConnectionHandler && other); //Move assignment
}; //class ConnectionHandler

#endif