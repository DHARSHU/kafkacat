package com.kafka.testing;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class Jsch {

    private Session session;
    private Channel channel;
    private String host, user, password,command;
    private int portnumber;
    private ChannelSftp channelSftp;
    private ChannelExec channelexec;

    public Session Create_Session(String host, String user, String password, int portnumber) throws Exception {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        this.user = user;
        this.password = password;
        this.host = host;
        this.portnumber = portnumber;
        this.session = jsch.getSession(user, host, portnumber);
        session.setPassword(password);
        session.setConfig(config);
        session.connect();
        return session;
    }


    public void channel_sftp(String from, String to, String filename) throws Exception {
        this.channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp channelSftp = (ChannelSftp) channel;
        channelSftp.cd(to);
        File file= new File(from+filename);
        if (file.isFile()) {
            InputStream inputStream = new FileInputStream(file);
            File f = new File(file.getName());
            channelSftp.put(inputStream, to+f.getName());
        }

    }

    public void Channel_Execute(String Command) throws Exception {

            this.channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(Command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream in=channel.getInputStream();
            channel.connect();
            byte[] tmp=new byte[1024];
            while(true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                                        break;
                }

            }
    }

    public void build_Kafkacat_Url ( Map<String,String> headers,String broker,String topic,String filename)throws Exception{
        this.command= "/webApps/kafka/bin/kafkacat";
        Iterator<Map.Entry<String,String>> header=headers.entrySet().iterator();
        while(header.hasNext())
        {
            Map.Entry Element = (Map.Entry)header.next();
            command+= " -H "+Element.getKey()+"="+Element.getValue();
        }
        command+="  -P -l -b "+broker+" -t "+topic+" "+filename;
        System.out.println(command);
        create_Shell_file();
    }

    public void create_Shell_file() throws Exception{
        String script_local_path="C:\\projects\\utilities\\src\\main\\resources\\";
        String Script_filename="Kafkacat.sh";
        String script_Remote_path="/home/speldply/payload/";

        FileOutputStream file = new FileOutputStream(script_local_path+Script_filename);

        file.write(command.getBytes());
        file.close();
        channel_sftp(script_local_path,script_Remote_path,Script_filename);
        Channel_Execute("cd payload && sh Kafkacat.sh");
        



    }
}
