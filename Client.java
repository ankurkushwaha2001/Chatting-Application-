import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener {
    JTextField text;
    static JPanel a;
    static Box vertical=Box.createVerticalBox();   //To align chat vertically one after the other

    static DataOutputStream d2;
    static JFrame f=new JFrame();
    Client(){
        f.setSize(450,700);
        f.setLocation(900,50);
        f.setLayout(null);
        f.setUndecorated(true);   //To remove the additional frame display

        JPanel p1=new JPanel();
        p1.setBounds(0,0,450,70);
        p1.setBackground(new Color(	32,92,84));
        f.add(p1);
        p1.setLayout(null);

        ImageIcon i1=new ImageIcon("1.png");
        Image i2=i1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
        ImageIcon i3=new ImageIcon(i2);
        JLabel img1=new JLabel(i3);
        img1.setBounds(0,10,50,50);
        p1.add(img1);

        img1.addMouseListener(new MouseAdapter() {   //To make image clickable by mouse
            @Override
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);   //To exit the complete project
            }
        });

        ImageIcon i4=new ImageIcon("6.png");
        Image i5=i4.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon i6=new ImageIcon(i5);
        JLabel img2=new JLabel(i6);
        img2.setBounds(50,10,50,50);
        p1.add(img2);

        ImageIcon i7=new ImageIcon("3.png");
        Image i8=i7.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon i9=new ImageIcon(i8);
        JLabel img3=new JLabel(i9);
        img3.setBounds(300,25,25,17);
        p1.add(img3);

        ImageIcon i10=new ImageIcon("4.png");
        Image i11=i10.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
        ImageIcon i12=new ImageIcon(i11);
        JLabel img4=new JLabel(i12);
        img4.setBounds(355,20,35,30);
        p1.add(img4);

        ImageIcon i13=new ImageIcon("5.png");
        Image i14=i13.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
        ImageIcon i15=new ImageIcon(i14);
        JLabel img5=new JLabel(i15);
        img5.setBounds(415,20,10,25);
        p1.add(img5);

        JLabel name=new JLabel("USER 2");
        name.setBounds(120,15,100,20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF",Font.BOLD,18));
        p1.add(name);

        JLabel status=new JLabel("Active Now");
        status.setBounds(120,35,100,20);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF",Font.PLAIN,12));
        p1.add(status);

        a=new JPanel();
        a.setBounds(5,80,440,570);
        f.add(a);

        text=new JTextField();
        text.setBounds(10,655,300,40);
        text.setFont(new Font("SAN_SERIF",Font.PLAIN,16));   //Font of input text entered by user
        f.add(text);

        JButton button=new JButton("SEND");
        button.setBounds(320,655,123,40);
        button.setFocusable(false);
        button.setBackground(new Color(	32,92,84));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SAN_SERIF",Font.BOLD,14));
        button.addActionListener(this);
        f.add(button);



        f.setVisible(true);
    }
    public void actionPerformed(ActionEvent ae){
        try{
            String output= text.getText();   //To store the text stored in JTextField

            JPanel p2=formatLabel(output);

            a.setLayout(new BorderLayout());

            JPanel right=new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);   //Display message at the line end or extreme right side
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));   //Space between the vertical boxes

            a.add(vertical,BorderLayout.PAGE_START);

            d2.writeUTF(output);

            text.setText("");   //To make the writing block empty after pressing the button

            f.repaint();
            f.validate();
            f.invalidate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static JPanel formatLabel(String output){
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JLabel out=new JLabel("<html><p style=\"width:150px\">" + output + "</p></html>");   //For fixed width of the chat box
        out.setFont(new Font("Tahoma",Font.PLAIN,16));   //For the displayed text in the box
        out.setBackground(new Color(32,92,84));
        out.setForeground(Color.WHITE);
        out.setBorder(new EmptyBorder(7,15,7,50));   //Basically padding
        out.setOpaque(true);

        panel.add(out);

        Calendar cal= Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");   //To set the format in which the time must be displayed

        JLabel time= new JLabel();
        time.setText(sdf.format(cal.getTime()));   //To get the current time in which the message is sent

        panel.add(time);


        return panel;
    }
    public static void main(String[] args){
        new Client();

        try{
            Socket s=new Socket("127.0.0.1",6001);   //Local Host,Server

            DataInputStream d1 = new DataInputStream(s.getInputStream());   //Message receive
            d2=new DataOutputStream(s.getOutputStream());   //Message send

            while(true){
                a.setLayout(new BorderLayout());

                String msg=d1.readUTF();   //To store the message coming from the client in string
                JPanel panel= formatLabel(msg);

                JPanel left=new JPanel(new BorderLayout());
                left.add(panel,BorderLayout.LINE_START);   //To print received message from the start or extreme left
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                a.add(vertical,BorderLayout.PAGE_START);

                f.validate();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
