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
 * 最简单的Java拖拽代码示例
 * 
 * @author 刘显安 2013年1月24日
 */
public class Main extends JFrame {

	JPanel panel;// 要接受拖拽的面板

	public Main() {
		panel = new JPanel();
		panel.setBackground(Color.YELLOW);
		getContentPane().add(panel, BorderLayout.CENTER);
		setSize(600, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(400, 200);
		setTitle("拖动到APK到黄框，可以查看里面是否内置火车票plugin -dcli,25.12.03");
		drag();// 启用拖拽
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
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");// 设置皮肤
		new Main().setVisible(true);
		;
	}

	public void drag()// 定义的拖拽方法
	{
		// panel表示要接受拖拽的控件
		new DropTarget(panel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde)// 重写适配器的drop方法
			{
				try {
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
					{
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
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
								temp += "存在火车票插件！！！" + file.getAbsolutePath();
							} else {
								temp += "不存在火车票插件！！！" + file.getAbsolutePath();
							}

						}
						JOptionPane.showMessageDialog(null, temp);
						dtde.dropComplete(true);// 指示拖拽操作已完成
					} else {
						dtde.rejectDrop();// 否则拒绝拖拽来的数据
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}