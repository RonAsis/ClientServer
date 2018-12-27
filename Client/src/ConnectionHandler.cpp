#include "../include/ConnectionHandler.h"

#include <string>
#include <iostream>
#include <boost/asio.hpp>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_), delimiter('\0'){}

ConnectionHandler::ConnectionHandler(const ConnectionHandler& other): host_(other.host_), port_(other.port_), io_service_(), socket_(io_service_), delimiter(other.delimiter){

}

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

// ********************** Sending a message to the server **********************

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, this->delimiter);
}

bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    string::size_type indexOfSpace(frame.find(' ', 0)); // the first word is the message's type
    std::string messageTypeName(frame.substr(0, indexOfSpace-1)); // message's name
    short messageOpcode(messageTypeShort(messageTypeName)); // message's opcode
    std::string hexMessage;
    char *h = new char[2];
    shortToBytes(messageOpcode, h);
    std::string messageContent(frame.substr(indexOfSpace+1, frame.length()));

    hexMessage = hexMessage + changeMessageToHex(messageTypeName, messageContent); // the encoded message

    bool result=sendBytes(hexMessage.c_str(), hexMessage.length()); // sending the encoded message to the server
    if(!result) return false;
    return sendBytes(&delimiter,1);
}

/**
 * This method returns the opcode of the given messageTypeName.
 *
 * @param messageTypeName - the type of the message.
 * @return - the opcode of messageTypeName.
 */
short ConnectionHandler::messageTypeShort(std::string messageTypeName){;
    short messageTypeNum;
    if (messageTypeName == "REGISTER")
        messageTypeNum = 1;
    else if (messageTypeName == "LOGIN")
        messageTypeNum = 2;
    else if (messageTypeName == "LOGOUT")
        messageTypeNum = 3;
    else if (messageTypeName == "FOLLOW")
        messageTypeNum = 4;
    else if (messageTypeName == "POST")
        messageTypeNum = 5;
    else if (messageTypeName == "PM")
        messageTypeNum = 6;
    else if (messageTypeName == "USERLIST")
        messageTypeNum = 7;
    else // STAT
        messageTypeNum = 8;
    return messageTypeNum;
}
/**
 * This method changes sort to byte.
 * @param num
 * @param bytesArr
 */
void ConnectionHandler::shortToBytes(short num, char* bytesArr){
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

/**
 * Changing the given string messageContent to hex.
 *
 * @param messageTypeName - the type of the message.
 * @param messageContent - the content of the message.
 * @return the given message encoded to hex.
 */
std::string ConnectionHandler::changeMessageToHex(std::string messageTypeName, std::string messageContent){
    std::string hexMessage;

    if (messageTypeName == "REGISTER" || messageTypeName == "LOGIN" || messageTypeName == "PM") {
        string::size_type indexOfSpace(messageContent.find(' ', 0)); // where the username ends
        for (int i=0; i<indexOfSpace; i++) // changing the userName to hex
            hexMessage = hexMessage + charToHex(messageContent[i]);

        hexMessage = hexMessage +"0";

        indexOfSpace = messageContent.find(' ', 1);
        for (int i=indexOfSpace+1; i<messageContent.length(); i++) // changing the password//content to hex
            hexMessage = hexMessage + charToHex(messageContent[i]);
        return hexMessage;
    }

    else if (messageTypeName == "LOGOUT" || messageTypeName == "USERLIST")  {
        hexMessage = "";
        return hexMessage;
    }

    else if (messageTypeName == "FOLLOW"){
        hexMessage = messageContent[0]; // follow or unfollow
        string::size_type indexOfSpace(messageContent.find(' ', 0)); // number of users to follow/unfollow
        for (int i=0; i<indexOfSpace; i++) // changing the number of users to follow/unfollow to hex
            hexMessage = hexMessage + charToHex(messageContent[i]);

        hexMessage = hexMessage +"0";

        indexOfSpace = messageContent.find(' ', 1);
        for (int i=indexOfSpace+1; i<messageContent.length(); i++) // changing the usernames to hex
            hexMessage = hexMessage + charToHex(messageContent[i]);
        return hexMessage;
    }

    else { // if (messageTypeName == "POST" || messageTypeName == "STAT")
        for (int i=0; i<messageContent.length(); i++) // changing the content/username to hex
            hexMessage = hexMessage + charToHex(messageContent[i]);
        return hexMessage;
    }
}

/**
 * Changing a given char to hex
 *
 * @param letter - the char that needs to be encoded to hex.
 * @return - the encoded char.
 */
std::string ConnectionHandler::charToHex(char letter){
    constexpr char hexmap[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                               '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    std::string letterHex( 2, ' ');
    letterHex[0] = hexmap[(letter & 0xF0) >> 4];
    letterHex[1] = hexmap[letter & 0x0F];

    return letterHex;
}

/**
 * sending the message to the server
 * @param bytes
 * @param bytesToWrite
 * @return
 */
bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error){
            throw boost::system::system_error(error);
        }

    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

// ********************** Reading a message from the server **********************

bool ConnectionHandler::getLine(std::string& line) {
    if(!getFrameAscii(line, this->delimiter)) // saving the message from the server
        return false;

    char* messageTypeByte = new char [2];
    messageTypeByte[0] = line[1];
    messageTypeByte[1] = line[2];
    short messageOpcode = bytesToShort(messageTypeByte);
    std::string messageTypeName = messageTypeString(messageOpcode);

    std::string answer = messageTypeName;
    changeMessageToChar(answer, line.substr(2), messageTypeName);
    return true;
}

bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            getBytes(&ch, 1);
            frame.append(1, ch);
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
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
        if(error) {
            throw boost::system::system_error(error);
        }
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

short ConnectionHandler::bytesToShort(char* bytesArr){
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

std::string ConnectionHandler::messageTypeString(short messageTypeNum){
    std::string messageTypeName;
    if (messageTypeNum == 9)
        messageTypeName = "NOTIFICATION";
    else if (messageTypeNum == 10)
        messageTypeName = "ACK";
    else // 11
        messageTypeName = "ERROR";
    return messageTypeName;
}

std::string ConnectionHandler::changeMessageToChar(std::string answer, std::string messageContent, std::string messageType){
    int len=messageContent.length();
    messageContent.resize(len-1);

    if (messageType == "NOTIFICATION") {
        if (hexToChar(messageContent[0]) == "5") { // public
            messageContent = messageContent.substr(1);
            std::string::size_type index(answer.find('0', 0));
            for (int i=0; i<index; i++) // posting user
                answer = answer + hexToChar(messageContent[i]);
            answer = answer + " ";
            for (int i=index; i<messageContent.length(); i++) // content
                answer = answer + hexToChar(messageContent[i]);
            answer = "> NOTIFICATION Public " + answer;
        }
        else { // pm
            messageContent = messageContent.substr(1);
            std::string::size_type index(answer.find('0', 0));
            for (int i=0; i<index; i++) // posting user
                answer = answer + hexToChar(messageContent[i]);
            answer = answer + " ";
            for (int i=index; i<messageContent.length(); i++) // content
                answer = answer + hexToChar(messageContent[i]);
            answer = "> NOTIFICATION PM "+ answer;
        }
    }
    else if (messageType == "ACK"){
        char messageTypeShort = messageContent[0];
        answer = bytesToShort(&messageTypeShort);
        for (int i=1; i<messageContent.length(); i++) // content
            answer = answer + hexToChar(messageContent[i]);
        answer = "> ACK "+ answer;
    }
    else {
        char messageTypeShort = messageContent[0];
        answer = "> ERROR "+ bytesToShort(&messageTypeShort);
    }
}

std::string ConnectionHandler::hexToChar(char letter){
return "n";
}


//*****************************************************************

// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}


const std::string ConnectionHandler::getHost(){
    return this->host_;
}

short ConnectionHandler::getPort(){
    return this->port_;
}


/**
 * Move constructor - this method makes a copy of this ConnectionHandler, saves it in the given ConnectionHandler "other"
 * and deletes this ConnectionHandler.
 *
 * @param other - the ConnectionHandler in which the copy of this ConnectionHandler will be saved.

ConnectionHandler :: Client(ConnectionHandler&& other): orderPrint(other.orderPrint),capacity(other.capacity),numberTable(other.numberTable),open(other.open),customersList(), orderList(){
    customersList = std:: move(other.customersList);
    orderList = std:: move(other.orderList);
    other.open = false;
} */

/**
 * Copy assignment - this method makes a copy of the given ConnectionHandler "other" and saves it in this ConnectionHandler.
 *
 * @param other - the ConnectionHandler that this ConnectionHandler will be identical to.

ConnectionHandler& ConnectionHandler :: operator=(const ConnectionHandler &other) {
    if (this != &other) {
        this->port_(other.getPort())_;
        this->host(other.getHost()_);
        this->close(other.close());
        }
    }
    return *this;

}
*/
/**
 * Move assignment - this method makes a copy of the given Client "other", saves it in this Client
 * and deletes the given Client "other".
 *
 * @param other - the Client that this Client will be identical to.

Client &Client::operator=(Client &&other) {
    if (this != &other) {
        clear();
        customersList = std::move(other.customersList);
        orderList = std::move(other.orderList);
        open = other.open;
        capacity = other.capacity;
        numberTable = other.numberTable;
    }
    return *this;
}
*/