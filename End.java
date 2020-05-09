package OS_experiment;

import java.io.*;
import java.net.*;

public class End {

    //用于建立tcp连接
    ServerSocket serverSocket;
    //用于udp通信
    DatagramSocket udp_socket;
    public End() {
    }
    public End(int port, String means) throws IOException {
        if(means.equals("udp")){
            udp_socket = new DatagramSocket(port);
        }
        else{
            serverSocket = new ServerSocket(port);
        }
    }
    public Socket waitConnect() throws IOException {
        Socket socket;
        return socket = serverSocket.accept();
    }

    public void send_tcp(String filePath, String IP, int port) throws IOException {
        Socket socket = new Socket(IP, port);
        OutputStream OS = socket.getOutputStream();
        FileInputStream FIS = new FileInputStream(filePath);
        byte[] tmp = new byte[1024];
        int length;
        OS.write(filePath.getBytes());
        long count = 0;
        while ((length = FIS.read(tmp)) != -1)
        {
            //tmp代表tcp包的内容
            count += length;
            OS.write(tmp,0,length);
        }
        System.out.println("文件传输大小——tcp：" + count);
        //传输完成 接受返回消息
        System.out.println("文件传输完成！——tcp");
        //关闭传输口
        OS.close();
        FIS.close();
        socket.close();
    }


    public void receive_tcp(Socket socket) throws IOException {
        InputStream IS = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int length;
        length = IS.read(bytes);
        String receiveFilePath = new String(bytes,0,length) +".backup";
        File des = new File(receiveFilePath);
        BufferedOutputStream BOS = new BufferedOutputStream(new FileOutputStream(des));
        //字节流得到图片，将图片写进写到文件夹中
        long count = 0;
        while ((length = IS.read(bytes)) != -1)
        {
            count += length;
            BOS.write(bytes,0,length);
        }
        System.out.println("接受文件的大小——tcp:" + count);
        //发送消息给客户端
        System.out.println("文件接受成功——tcp！");
        //关闭流资源
        socket.close();
        BOS.close();
    }


    public void send_udp(String filePath, String IP, int port) throws IOException {
        byte[] data = new byte[1024];
        FileInputStream FIS = new FileInputStream(filePath);
        byte[] name = filePath.getBytes();
        DatagramPacket packet = new DatagramPacket(name, name.length, InetAddress.getByName(IP), port);
        udp_socket.send(packet);
        int length;
        //packet = new DatagramPacket(data,1024, InetAddress.getByName(IP), port);
        long count = 0;
        while ((length = FIS.read(data)) != -1)
        {
            count = count + length;
            //packet.setLength(length);
            udp_socket.send(new DatagramPacket(data, length, InetAddress.getByName(IP), port));
        }
        System.out.println("文件上传大小——tcp：" + count);
        packet.setLength(0);
        udp_socket.send(packet);
        System.out.println("文件上传成功！——udp");
        FIS.close();
        udp_socket.close();
    }


    public void receive_udp() throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        udp_socket.receive(packet);
        String receiveFilePath = new String(packet.getData(),0,packet.getLength())+".backup";
        File des = new File(receiveFilePath);
        BufferedOutputStream BOS = new BufferedOutputStream(new FileOutputStream(des));
        long count = 0;
        while(true){
            udp_socket.receive(packet);
            if (packet.getLength()==0) break;
            count += packet.getLength();
            BOS.write(packet.getData(),0,packet.getLength());
        }
        System.out.println("文件接收大小——udp：" + count);
        System.out.println("文件接收成功——udp");
        //关闭流
        BOS.close();
        udp_socket.close();
    }
}
