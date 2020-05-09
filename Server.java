package OS_experiment;
import sun.jvm.hotspot.debugger.ThreadAccess;

import java.io.IOException;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        End server_tcp = new End(10002, "tcp");
        End server_udp = new End(8888, "udp");

        Socket socket = server_tcp.waitConnect();
        server_tcp.receive_tcp(socket);

        server_udp.receive_udp();

    }
}