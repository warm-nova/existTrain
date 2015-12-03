package existTrain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * ��򵥵�Java��ק����ʾ��
 * 
 * @author ���԰� 2013��1��24��
 */
public class Main extends JFrame {

	JPanel panel;// Ҫ������ק�����

	public Main() {
		panel = new JPanel();
		panel.setBackground(Color.YELLOW);
		getContentPane().add(panel, BorderLayout.CENTER);
		setSize(600, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(400, 200);
		setTitle("�϶���APK���ƿ򣬿��Բ鿴�����Ƿ����û�Ʊplugin -dcli,25.12.03");
		drag();// ������ק
	}

	public static int readZipFile(String file) throws Exception {
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		int haveplugin = 0;
		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				System.out.println(ze.getName() + " bytes");
			} else {
				if (ze.getName().equals("assets/qiangpiao.apk")) {
					haveplugin = 1;
				}
				System.err.println("file - " + ze.getName() + " : " + ze.getSize() + " bytes");
			}
		}
		zin.closeEntry();
		return haveplugin;
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");// ����Ƥ��
		new Main().setVisible(true);
		;
	}

	public void drag()// �������ק����
	{
		// panel��ʾҪ������ק�Ŀؼ�
		new DropTarget(panel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde)// ��д��������drop����
			{
				try {
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// ���������ļ���ʽ��֧��
					{
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// ������ק��������
						List<File> list = (List<File>) (dtde.getTransferable()
								.getTransferData(DataFlavor.javaFileListFlavor));
						String temp = "";
						for (File file : list) {

							/*
							 * String name = "tmp"; String filename =
							 * file.getAbsolutePath(); if (filename.indexOf(".")
							 * >= 0) { filename = filename.substring(0,
							 * filename.lastIndexOf(".")); } file.renameTo(new
							 * File(name + ".zip"));
							 */

							// temp += file.getAbsolutePath();
							int res = readZipFile(file.getAbsolutePath());
							if (res == 1) {
								temp += "���ڻ�Ʊ���������" + file.getAbsolutePath();
							} else {
								temp += "�����ڻ�Ʊ���������" + file.getAbsolutePath();
							}

						}
						JOptionPane.showMessageDialog(null, temp);
						dtde.dropComplete(true);// ָʾ��ק���������
					} else {
						dtde.rejectDrop();// ����ܾ���ק��������
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}