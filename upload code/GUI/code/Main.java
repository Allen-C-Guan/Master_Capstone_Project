package WasteXero_v2;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.swing.*;
import java.io.*;

/**
 * to execute
 */
public class Main {
    public static void main(String[] args) {
//        Process proc;
//        try {
//            proc = Runtime.getRuntime().exec("python E:\\WasteXero2.0\\download.py");// 执行py文件
//            //用输入输出流来截取结果
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
//            proc.waitFor();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        SwingUtilities.invokeLater(() -> new WasteXero_v2().setVisible(true));
    }
}