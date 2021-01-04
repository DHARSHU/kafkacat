package com.kafka.testing;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import java.util.HashMap;

public class Imp_Jsch {

    public static void main(String args[])throws Exception {
    Jsch jsch_lib= new Jsch();
        HashMap<String, String> hm =
                new HashMap<String, String>();
        hm.put("payload_version", "1.0.0");
        hm.put("batch_number", "test");
        hm.put("action", "add");
       Session session= jsch_lib.Create_Session("u060speld802.kroger.com","speldply","wxCEhqrjW",22);
       jsch_lib.channel_sftp("C:\\Users\\kon8040\\Desktop\\","/home/speldply/payload/","heartbeat.json");
       jsch_lib.build_Kafkacat_Url(hm,"localhost:9092","recipe-ingredient","heartbeat.json");
      // jsch_lib.Channel_Execute();
       session.dicsconnect();

    } }


