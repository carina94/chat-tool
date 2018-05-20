package DirectAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import FormUI.ServerUI;
import ThreadPacek.ListenerClient;
/*������Ƿ������˵ĵȴ��ͻ�������*/
public class Server extends Thread {
    ServerUI ui;
    ServerSocket ss;
    BufferedReader reader;
    PrintWriter writer;

    public Server(ServerUI ui) {
        this.ui = ui;//��ȡUI������UI����
        this.start();//��ʼ�߳��е� ������Ϣ
    }

    public void run() {
        try {

            ss = new ServerSocket(9654);
            ui.clients=new ArrayList<Socket>();
            println("�����������ɹ����˿�9654");
            
            /**
             * һֱ���У��������տͻ��˷���������������
             * ÿ�õ�һ���������󣬾͵���accept��������һ�� socket
             * �� socket �����洢�� ur.clients��ȥ
             */
            while (true) {
                println("�ȴ��ͻ���");
                try{
                Socket client = ss.accept();
                //�ҿ��������ﴴ��һ�� ���� socket��
                ui.clients.add(client);
                println("���ӳɹ�" + client.toString());
                /*
                 * ÿ�εõ�һ�� socket ����һ�� �����ÿͻ��˵�ListenerClient
                 * ListenerClient �п���ʱʱ���� ��socket������Ŀͻ��� ��������Ϣ
                 * ����ʱʱ�ؽ���Ϣת����ÿ�� socket������Ŀͻ��� 
                 */
                new ListenerClient(ui, client);
                }
                catch(java.net.SocketException e){
                	System.out.println(e.toString());
                }
            }
        } catch (IOException e) {
            println("����������ʧ�ܣ��˿�1228");
            println(e.toString());
            e.printStackTrace();
        }

    }

    public synchronized void sendMsg(String msg) {
        try {
            for (int i = 0; i < ui.clients.size(); i++) {
                Socket client = ui.clients.get(i);
                writer = new PrintWriter(client.getOutputStream(), true);
                writer.println(msg);
            }

        } catch (Exception e) {
            println(e.toString());
        }
    }

    public void println(String s) {
        if (s != null) {
            this.ui.taShow.setText(this.ui.taShow.getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }

    public void closeServer() {
        try {
        	this.stop();
            if (ss != null)
                ss.close();
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
