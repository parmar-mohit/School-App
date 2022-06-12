package TeacherDesktop;

import TeacherDesktop.Frames.LoginInterface;
import TeacherDesktop.Frames.NoConnectionInterface;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;

import java.net.Socket;

public class Application {
    public static void main(String[] args) {
        Socket socket;
        try{
            socket = new Socket(Constant.IP_ADDRESS,Constant.PORT);
            ServerConnection serverConnection = new ServerConnection(socket);
            new LoginInterface(serverConnection);
        }catch(Exception e){
            e.printStackTrace();
            new NoConnectionInterface();
        }
    }
}
