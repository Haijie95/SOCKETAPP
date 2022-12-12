import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketApp {
    
    public static void main(String[] args) {
        
        String usage="""
            //Usage Server
            //<program><server><port><cookie-file.txt>
    
            //Usage Client
            //<program><client><host><port>
                """;

        
        // for (String arg:args){
        //     System.out.println((arg));
        // }

        if((args.length)!=3){
            System.out.println("Incorrect Inputs. Please check the following usage.");
            System.out.println(usage);
            return;
        }

        String type=args[0];
        if(type.equalsIgnoreCase("server")){
            int port=Integer.parseInt(args[1]);
            String filename=args[2];
            StartServer(port,filename);
        }else if(type.equalsIgnoreCase("thread.server")){
            int port = Integer.parseInt(args[1]);
            String fileName = args[2];
            StartMultiThreaderServer(port, fileName);
        } else if (type.equalsIgnoreCase("client")){
            String hostname=args[1];
            int port =Integer.parseInt(args[2]);
            StartClient(hostname,port);
        }else{
            System.out.println("Incorrect Argument!!!");
        }
    }

    public static void StartMultiThreaderServer(int port, String fileName) {
        ServerSocket srver;
        try {
            srver = new ServerSocket(port);

            while (true) {
                Socket socket = srver.accept();
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                Thread tsh = new ThreadSockethHandler(socket, dis, dos);
                tsh.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    public static void StartServer(int port,String filename){
        ServerSocket srver;
        try{
            srver = new ServerSocket(port);
            Socket socket = srver.accept();
            
            //In
            DataInputStream dis=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            //Out
            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            while(true){
                String fromClient=dis.readUTF();
                if(fromClient.equalsIgnoreCase("exit")){
                    //exit
                    break;
                }
                System.out.println("LOG: msg from client: " + fromClient);

                if(fromClient.equalsIgnoreCase("get-cookie")){
                    //send a random cookie from the file
                    dos.writeUTF("Dummy Cookie");
                    dos.flush();
                } else{
                    //invalid command
                    dos.writeUTF("From Server: Invalid Command");
                    dos.flush();
                }
            }
            socket.close();
        }   
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void StartClient(String host,int port){
        try{
            Socket socket=new Socket(host,port);

            //In
                DataInputStream dis=new DataInputStream((new BufferedInputStream(socket.getInputStream())));
            //Out
                DataOutputStream dos=new DataOutputStream((new BufferedOutputStream(socket.getOutputStream())));

                Scanner sc = new Scanner(System.in);
                boolean stop =false;
    
                while(!stop){
                    String line=sc.nextLine();
                    if(line.equalsIgnoreCase("exit")){
                        dos.writeUTF("exit");
                        dos.flush();
                        stop=true;
                        break;
                    }

                    dos.writeUTF(line);
                    dos.flush();

                    String fromServer=dis.readUTF();
                    System.out.println(("Resp from server! "+fromServer));
                    // if (line.equalsIgnoreCase("get-cookie")){
                    //     //send a req to server for a cookie
                    //     dos.writeUTF("get-cookie");
                    //     dos.flush();
                    // } else{
                    //     System.out.println(("Invalid Command: "+line));
                        //dos.writeUTF("Invalid Command from client");
                        //dos.flush();
                    //}
                    
                    //String fromServer=dis.readUTF();
                    //System.out.println("Response from server!\n"+fromServer);
                }

        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //javac -sourcepath src -d classes src/SocketApp.java   ||to compile
    //java -cp classes SocketApp server 12000 cookie.txt    ||to run server
    //java -cp classes SocketApp client localhost 12000     ||to run client
}