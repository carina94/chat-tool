package ThreadPacek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import FormUI.ClientUI;
import Test.Log;
import Utils.MsgUtils;

public class ClientThread extends Thread {

	// ÿ���ͻ��˶����Լ��� UI���Լ��� socket���Լ��� reader���Լ���writer
	// ÿ���û����кܶ�� ClientUI
	ClientUI ui;

	public String username;
	public static List<ClientUI> uis = new ArrayList<ClientUI>();
	public List<String> snames = new ArrayList<String>();
	Socket client;
	BufferedReader reader;
	PrintWriter writer;

	public static ClientUI uiss;



	/*
	 * ��ʼ���̣߳�����
	 */
	public ClientThread(String username) {
		// this.ui = ui;
		this.username = username;
		try {
			client = new Socket("127.0.0.1", 1228);// �����������ӷ������˵�IP�Ķ˿�
			println("���ӷ������ɹ����˿�1228");
			// ��ʼ�� �� ����
			reader = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			// ��ʼ�� д ����
			writer = new PrintWriter(client.getOutputStream(), true);
			// ���Ϊ true���� println��printf �� format ������ˢ�����������
		} catch (IOException e) {
			println("���ӷ�����ʧ�ܣ��˿�1228");
			println(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * һֱ�ڽ�����Ϣ
	 */
	public void run() {
		String msg = "";
		while (true) {
			try {
				msg = reader.readLine();
			} catch (IOException e) {
				println("�������Ͽ�����");
				break;
			}
			if (msg != null && msg.trim() != "") {
				println(msg);
			}
		}
	}

	/**
	 * ������Ϣ
	 * @param msg
	 */
	public void sendMsg(String msg) {
		try {
			writer.println(msg);
		} catch (Exception e) {
			println(e.toString());
		}
	}

	/**
	 * ����Ϣ��ӵ���Ӧ�Ŀͻ���
	 * 
	 * @param s
	 */
	public void println(String s) {
		if (s != null) {
			String uses[] = MsgUtils.splitMsg(s);
			int flag = 0;
			if (uses != null) {
				if (uiss != null && uses[0].equals("chat together")) {
					Log.writeLog("Ⱥ����Ϣ��"+uses[2]);
					if (uiss.dname.equals("chat together")
							&& !uses[1].equals(uiss.sname)) {
						uiss.taShow.append(uiss.cd.getTime().substring(0,19)+"\n"+uses[1] + " : " + uses[2] + "\n");
					}
				}
				if (uses[0].equals(username)) {
					for (ClientUI clu : ClientThread.uis) {
						if (clu.sname.equals(uses[0])
								&& clu.dname.equals(uses[1])) {
							flag = 1;
							clu.taShow.append(clu.cd.getTime().substring(0,19)+"\n"+uses[1] + " : " + uses[2] + "\n");
						}
					}
					if (flag == 0
							&& JOptionPane.showConfirmDialog(null, uses[1]+"��Ҫ�������죬���Ƿ����",
									"��ܰ��ʾ", JOptionPane.YES_NO_OPTION) == 0) {
						ClientUI cui = new ClientUI(uses[0], uses[1], this);
						ClientThread.uis.add(cui);
						cui.taShow.append(cui.cd.getTime().substring(0,19)+"\n"+uses[1] + ":" + uses[2] + "\n");
					}
				}

			}

			System.out.println(s + "\n");
		}
	}

}