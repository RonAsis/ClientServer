#include <connectionHandler.h>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_), delimiter('\0'){}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
            tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        string::size_type indexOfFirstSpace(line.find(' ', 0));
        std::string messageType(line.substr(0, indexOfSecondSpace-1));
        messageType(messageType);
        socket_.write_some(boost::asio::buffer(messageType, 2), error); //*************************
        if(error)
            throw boost::system::system_error(error);

        tmp = indexOfFirstSpace+1;
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, this.delimiter);
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, this.delimiter);
}

bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    int counter = 0;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            if (counter==2)
                frame.append(1, ' ');
            getBytes(&ch, 1);
            frame.append(1, ch);
            counter++;
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    bool result=sendBytes(frame.c_str(),frame.length());
    if(!result) return false;
    return sendBytes(&delimiter,1);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

std::string ConnectionHandler::messageType(std::string messageType){;
    std::string messageTypeNum;
    if (messageType == "REGISTER")
        messageTypeNum = "01";
    else if (messageType == "LOGIN")
        messageTypeNum = "02";
    else if (messageType == "LOGOUT")
        messageTypeNum = "03";
    else if (messageType == "FOLLOW")
        messageTypeNum = "04";
    else if (messageType == "POST")
        messageTypeNum = "05";
    else if (messageType == "PM")
        messageTypeNum = "06";
    else if (messageType == "USERLIST")
        messageTypeNum = "07";
    else // STAT
        messageTypeNum = "08";
    return messageTypeNum;
}