
#include <Client.h>
#include <thread>

Client::Client(ConnectionHandler connectionHandler, int id): connectionHandler(connectionHandler), stop(false), id(id), clientName("CLIENT#"+id){}

void Client::runWriter(){
    while(this.stop==false)  {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize); // getting a new line from the user
        std::string line(buf);
        int len=line.length();



        line(line.substr(indexOfSecondSpace+1)); // removing the message's type
        if (!connectionHandler.sendLine(line)) { // if it wasn't possible to send the line from the user break;
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            this.stop = true;
            break;
        }
        std::cout << "Sent " << len+1 << " bytes to server" << std::endl;
    }
}

void Client::runReader(){
    while(this.stop==false)  {
        std::string answer;

        if (!connectionHandler.getLine(answer)) { // if it wasn't possible to get the answer from the server
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            this.stop = true;
            break;
        }

        len=answer.length();
        answer.resize(len-1);

        //   std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        int index(answer.indexOf(0));
        string::size_type indexOfSecondSpace(answer.find(' ', 1));
        std::string messageType(answer.substr(1, indexOfSecondSpace-1));
        //   boost::to_upper(messageType);
        if (messageType = "9") { //NOTIFICATION // **** do we need the space??????
            if (answer[3] == "5") // public
                std::cout << this.clientName+"> NOTIFICATION Public ****"+ answer.substr(4) << std::endl;
            else { // pm
                std::cout << this.clientName+"> NOTIFICATION PM ****"+ answer.substr(4) << std::endl;
            }
        }
        else if (messageType = "10"){ //ACK
            std::cout << this.clientName+"> ACK ****"+ answer.substr(3) << std::endl;
            if (answer[4] == "3")
                this.stop = true;
        }
        else { // ERROR
            std::cout << this.clientName+"> ERROR ****"+ answer.substr(3) << std::endl;
        }
    }
}

ConnectionHandler Client::getConnectionHandler(){
    return this.connectionHandler;
}

bool Client::getStop(){
    return this.stop;
}