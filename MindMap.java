
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListDataListener;
//MYSQL 5.1.48 connecter ,jdk 1.8

//-- MySQL dump 10.10
//--
//-- Host: localhost    Database: mm
//-- ------------------------------------------------------
//-- Server version	5.0.16
//
///*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
///*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
///*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
///*!40101 SET NAMES utf8 */;
///*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
///*!40103 SET TIME_ZONE='+00:00' */;
///*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
///*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
///*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
///*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
//
//--
//-- Table structure for table `mm`
//--
//
//CREATE DATABASE mm;
//
//DROP TABLE IF EXISTS `mm`;
//CREATE TABLE `mm` (
//  `id` int(11) default NULL,
//  `word` text,
//  `x` int(11) default NULL,
//  `y` int(11) default NULL
//) ENGINE=InnoDB DEFAULT CHARSET=latin1;
//
//--
//-- Dumping data for table `mm`
//--
//
//
///*!40000 ALTER TABLE `mm` DISABLE KEYS */;
//LOCK TABLES `mm` WRITE;
//INSERT INTO `mm` VALUES (1,'head',100,100),(1,'shoulder',140,100),(1,'parts',180,120),(1,'loin',280,190),(2,'help',200,100),(2,'save',170,150),(2,'aid',270,200),(3,'hair',486,4),(3,'hair',220,90),(3,'dandruff',146,264),(4,'Young',244,17),(4,'Old',48,291),(5,'dung',16,37),(5,'crap',270,297),(5,'food',104,170),(6,'trecool',130,211),(6,'billyjoe',127,269),(6,'curl',94,75),(6,'curl',151,174);
//UNLOCK TABLES;
///*!40000 ALTER TABLE `mm` ENABLE KEYS */;
///*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
//
///*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
///*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
///*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
///*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
///*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
///*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
///*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


public class MindMap {
    
    private JComboBox cb = new JComboBox();
        
    private JButton delS = new JButton("Delete");

    private int idWordMap = 0;

    private JFrame frame = new JFrame("Frank's Mind Map");

    private JPanel panel = new JPanel();

    private Graphics g = null;

    private Connection connection = null;

    private Statement stmt = null;
    
    private JList jlist = new JList();
    
    private boolean fass = false;
    
    private boolean rdr = false;
    
    private JButton rdrun = new JButton("Random Run");

    public MindMap() {
        setUI();

        connectToDataBase();
        drawComboBox();
        fillJList();
        drawNew();
        drawR();
        
        drawT();

        drawBrainNMap();
        refreshUI();
    }
    
    private void fillJList() {
        try {
            if(cb.getItemCount() > 0) {
                String sql = "select word from mm where id = "+idWordMap+";";
                ResultSet rs = stmt.executeQuery(sql);
                int ilast = 0;
                DefaultListModel listModel = new DefaultListModel();
                while (rs.next())
                {
                    listModel.addElement(rs.getString("word"));
                }
                jlist.setModel(listModel);
                jlist.setBorder(new BevelBorder(BevelBorder.LOWERED));
            }
        } catch(SQLException s) {
            s.printStackTrace();
        }
    }
    
    public void drawT() {
        delS.setBounds(10, 170, 100, 20);
        panel. add(delS);
        delS .addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String str = "delete from mm where id = " + ((String) cb.getSelectedItem()).substring(0, ((String) cb.getSelectedItem()).indexOf(" ")) + ";";
                    stmt.execute(str);
                    drawComboBox();
                    cb.setSelectedIndex(-1);
                    
                } catch(SQLException ss ) { }
            }
        });
    }

    public void drawR() {
        rdrun.setBounds(10, 200, 120, 20);
        panel. add(rdrun);
        rdrun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rdr = !rdr;
                try {
                    Thread.sleep(35000);
                } catch(InterruptedException ee) {
                    ee.printStackTrace() ;
                }
                if(!rdr) {
                    rdrun.setText("Random Run");
                    
                }
                else 
                    rdrun.setText("-Random Run");
                
                if(rdr) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true) {
                                if(!rdr)
                                    return;
                                try {
                                    if(cb.getSelectedIndex() == cb.getItemCount() - 1) {
                                        cb.setSelectedIndex(0);
                                    } else
                                        cb.setSelectedIndex(cb.getSelectedIndex() + 1);
                                    Thread.sleep(30000);
                                } catch(Exception e) {}
                            }
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    public void drawNew() {
        JTextField text = new JTextField();
        text.setBounds(120-100+100, 270, 150, 20);
        panel.add(text);
        JButton newWord = new JButton("Add");
        newWord.setBounds(180-100, 300, 100, 20);
        JButton newMap = new JButton("New Map");
        newMap.setBounds(300-100, 300, 90, 20);
        panel.add(newMap);
        newMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fass = !fass;
                if(fass)
                    newMap.setText("-new Map");
                else
                    newMap.setText("new Map");
            }
        });

        panel.add(jlist);
        jlist.setBackground(Color.RED);
        jlist.setBounds(150, 10, 120, 240);
        
        panel.add(newWord);
        newWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    int max = 0;
                    String str = "select max(id) as max from mm;";
                    ResultSet rs = stmt.executeQuery(str);
                    if(!fass) {
                        if (rs.next())
                        {
                            max = idWordMap;
                        } else {
                            max = 1;
                        }
                    }
                    if(fass)
                    {
                        if (rs.next() )
                        {
                            max = rs.getInt("max") + 1;
                        }
                        else
                            max = 1;
                        
                    }
                    String wor = text.getText();
                    Random rad = new Random();
                    int x = rad.nextInt(300);
                    int y = rad.nextInt(300);
                    String sql = "insert into mm (id, word, x, y) values ("+max+", '"+wor+"', "+x+", "+y+");";
                    stmt.execute(sql);
                    
                    String obj = max + " " + wor;
                    boolean fs = false;
                    for(int tt = 0; tt < cb.getItemCount(); tt++) {
                        if(max == Integer.parseInt(((String) cb.getItemAt(tt)).substring(0,((String) cb.getItemAt(tt)).indexOf(" ")))) {
                            fs = true;
                        }
                    }
                    if(!fs)
                        cb.addItem(obj);
                }
                catch 
                        (SQLException s)
                {
                    s.printStackTrace();
                }
            }
        });
    }
    
    public void drawBrainNMap() {
        Thread thread = new Thread(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (true)
                        {
                            try
                            {
                                drawBrain();

                                drawMap();
                        g.setColor(Color.RED);
                        g.setFont(new Font("arial", Font.BOLD, 40));
                        g.drawString("Mind Map", 10, 400);
                        g.setFont(new Font("arial", Font.BOLD, 30));
                        g.drawString("Word:", 10, 270);
                                Thread.sleep(15000);
                            }
                            catch 
                                    (InterruptedException ie)
                            {
                                ie.printStackTrace();
                            }
                    }
                }
            }
        );
        
        thread.start();
    }
    
    private void refreshUI() {
        panel.updateUI();
    }

    private void drawComboBox() {
        panel.remove(cb);
        cb = new JComboBox();
        
        try
        {
            Thread.sleep(1000);
            String sql = "select distinct id, word from mm order by id Asc;";
            ResultSet rs = stmt.executeQuery(sql);
            int ilast = 0;
            while (rs.next())
            {
                if(ilast != rs.getInt("id"))
                {
                    ilast = rs.getInt("id");
                    
                    String obj = rs.getString("id") + " " + rs.getString("word");
                    
                    cb.addItem(obj);
                }
            }
        }
        catch 
                (Exception s)
        {
            s.printStackTrace();
        }
        cb.setBounds(10, 10, 120, 30);

        panel.add(cb);

        idWordMap = 1;
        cb.setSelectedIndex(0);
                
        cb.addItemListener(
                new ItemListener()
                {
                    @Override
                    public void itemStateChanged(ItemEvent e)
                    {
                        
                        try {
                            idWordMap = Integer.parseInt(cb.getSelectedItem().toString().substring(0, 

                                    cb.getSelectedItem().toString().indexOf(" ")));

                            String sql = "select word from mm where id = " + idWordMap + ";";
                            ResultSet rs = stmt.executeQuery(sql);
                            int ilast = 0;
                            DefaultListModel listModel = new DefaultListModel();
                            while (rs.next())
                            {
                                listModel.addElement(rs.getString("word"));
                            }
                            jlist.setModel(listModel);
                            jlist.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        } catch(SQLException s) {
                            s.printStackTrace();
                        }
                    }
                }
        );
        
        
    }
    
    private void drawMap() {
        try
        {

            String sql = "select id, word, x, y from mm where id = " + idWordMap + ";";

            ResultSet rs = stmt.executeQuery(sql);

            ArrayList lines = new ArrayList();
            while (rs.next())
            {
                
                g.setColor(Color.YELLOW);
                g.setFont(new Font("arial", Font.BOLD, 20));
                g.drawString(rs.getString("word"), rs.getInt("x")+400, rs.getInt("y"));
                int obj[] = new int[2];
                
                obj[0] = rs.getInt("x")+400;
                obj[1] = rs.getInt("y");
                lines.add(obj);
            }

            
            Thread thread = new Thread() {
                public void run() {
                    int i = 0;

                    while(i < lines.size() - 1)
                    {
                        try
                        {
                            Thread.sleep(1500);

                            int b = 0;
                            while(b < lines.size())
                            {
                                g.setColor(Color.YELLOW);
                                g.drawOval(( (int[]) lines.get(b) )[0], ( (int[]) lines.get(b) )[1], 20, 20);
                                b++;
                            }
                            g.setColor(Color.GREEN);
                            g.drawOval(( (int[]) lines.get(i) )[0], ( (int[]) lines.get(i) )[1], 20, 20);

                            g.setColor(Color.BLUE);

                            if(i < lines.size() - 2)
                                g.drawLine(( (int[]) lines.get(i) )[0], ( (int[]) lines.get(i) )[1], 
                                    ( (int[]) lines.get(i+1) )[0], ( (int[]) lines.get(
                                            i+1) )[1]);

                            ++i ;
                        }
                        catch 
                                (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(500);
                        g.setColor(Color.YELLOW);
                        g.drawOval(( (int[]) lines.get(i-1) )[0], ( (int[]) lines.get(i-1) )[1], 20, 20);
                        g.setColor(Color.GREEN);
                        g.drawOval(( (int[]) lines.get(i) )[0], ( (int[]) lines.get(i) )[1], 20, 20);

                        g.setColor(Color.BLUE);
                        Random r = new Random();
                        if(lines.size() > 1) {
                            int ii = r.nextInt(lines.size() - 2);
                            g.drawLine(( (int[]) lines.get(i) )[0], ( (int[]) lines.get(i) )[1], 
                                        ( (int[]) lines.get(ii) )[0], ( (int[]) lines.get(
                                                ii) )[1]);
                        }

                    } catch(ArrayIndexOutOfBoundsException ee) {
                        
                    } catch(InterruptedException ii) {
                        
                    }
                }
            };
            thread.start();
        }
        catch 
                (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    private void createConnectionToDataBase() {
        try {

            /*
             * declare and set credentials for the database
             * hostName or server name, the database name,
             * the user name, and the corresponding password
             */
            String hostName = "localhost";

            String dbName = "mm";

            String userName = "root";

            String passWord = "";
            /*
             * build the connection URL for the connection instance
             */
            String url = "jdbc:mysql://" + hostName + ":3320/" + dbName + "?user=" + userName + "&password=" + passWord;
            /*
             * get a connection from the database server and
             * create a corresponding statement instance to be reused
             * throughout the program invoiceMom
             */
            connection = DriverManager.getConnection(url);

            stmt = connection.createStatement();

        } catch(SQLException sqle) {
            /*
             * If any error occurs corresponding or related to the server connection or statement creation
             * print the error here
             */
            sqle.printStackTrace();
        }
    }
 
     private void connectToDataBase() {
         /*
          * lower level call to make server database connection and statement instance 
          * to be reused throughout the program invoiceMom
          */
        createConnectionToDataBase();
 
    }

    private void drawBrain() {
        Thread thread = new Thread(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Image image = ImageIO.read(getClass().getResourceAsStream("bg.jpg"));
                            g.drawImage(image, 300, 0, 500, 400, null);
                        }
                        catch
                                (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        );
        
        thread.start();
        
        try
        {
            Thread.sleep(1000);
        }
        catch
                (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void setUI() {
        frame.setLayout(null);

        frame.setBounds(0, 0, 840, 500);

        panel.setBounds(frame.getBounds());
        panel.setLayout(null);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        g = panel.getGraphics();
    }
    
    public static void main(String[] args) {
        try
        {
            SwingUtilities.invokeAndWait(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        JFrame j = new JFrame("Mind Map");
                        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                        j.setBounds(0, 0, 300, 200);
                        JPanel p = new JPanel();
                        p.setBounds(j.getBounds());
                        j.add(p);
                        j.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        j.setLocation(dim.width/2-j.getSize().width/2, dim.height/2-j.getSize().height/2);                        j.setLayout(null);
                        j.setLocationRelativeTo(null);
                        j.setUndecorated(true);
                        JLabel lable = new JLabel("Mind Map");
                        lable.setText("Mind Map");
                        lable.setFont(new Font("arial", Font.BOLD, 20));
                        lable.setBounds(120, 80, 190, 40);
                        lable.setForeground(Color.BLACK);
                        p.setLayout(null);
                        ///p.add(lable);
                        j.setVisible(true);
                        Graphics gr = p.getGraphics();
                        gr.setColor(Color.WHITE);
                        gr.fillRect(0, 0, 300, 200);
                        gr.setColor(lable.getForeground());
                        gr.setFont(lable.getFont());
                        try {
                            Image iiiii = ImageIO.read(getClass().getResourceAsStream("bg.jpg"));
                            gr.drawImage(iiiii, 0, 0, 300, 200, j);
                            gr.drawString("Mind Map", 120, 80);
                            Thread.sleep(3000);
                            j.dispose();
                        } catch(Exception e) {e.printStackTrace();}
                        new MindMap();
                    }
                }
            );
        }
        catch
                (InterruptedException ie)
        {
            ie.printStackTrace();
            
        }
        catch
                (InvocationTargetException ite)
        {
            ite.printStackTrace();
        }
    }
}