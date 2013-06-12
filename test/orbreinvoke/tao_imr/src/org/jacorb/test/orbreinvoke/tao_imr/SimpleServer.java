package org.jacorb.test.orbreinvoke.tao_imr;

import java.util.Properties;
import java.io.*;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.jacorb.orb.util.*;
import org.jacorb.util.ObjectUtil;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.Servant;

import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.jacorb.test.listenendpoints.echo_corbaloc.*;

public class SimpleServer
{
   public static void main(String[] args)
   {
      try
      {
         MyCmdArgs cmdArgs = new MyCmdArgs("Server", args);
         boolean cmdArgsStatus = cmdArgs.processArgs();

         // translate any properties set on the commandline but after the
         // class name to a properties
         java.util.Properties props = ObjectUtil.argsToProps(args);
         String implName = props.getProperty("jacorb.implname", "EchoServerX");
         if (implName.equals("EchoServerX"))
         {
             props.setProperty("jacorb.implname", implName);
         }
         System.out.println("Server: jacorb.implname: <" + implName + ">");

         String poaBaseName = cmdArgs.getPoaBaseName();
         if (poaBaseName == null)
         {
             poaBaseName = "EchoServer";
         }
         System.out.println("Server: poaBaseName: <" + poaBaseName + ">");

         String objectId = poaBaseName + "-ID";
         String poaName = poaBaseName + "-POA";

         //init ORB
         ORB orb = ORB.init(args, props);

         //init POA
         POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

         //init new POA
         Policy[] policies = new Policy[2];
         policies[0] = rootPOA.create_lifespan_policy(LifespanPolicyValue.PERSISTENT);
         policies[1] = rootPOA.create_id_assignment_policy(IdAssignmentPolicyValue.USER_ID);

         for (int i=0; i<policies.length; i++)
         {
            policies[i].destroy();
         }

         POA parent_poa = rootPOA.create_POA (poaBaseName + "-POA", rootPOA.the_POAManager(), policies);
         parent_poa.the_POAManager().activate();

         // create servant object
         EchoMessageImpl echoServant = new EchoMessageImpl(implName + "." + poaBaseName + "." + objectId);

         parent_poa.activate_object_with_id(objectId.getBytes(), echoServant);
         final org.omg.CORBA.Object ref = parent_poa.servant_to_reference(echoServant);
         String ior = orb.object_to_string(ref);
         System.out.println("SERVER IOR: " + ior);
         System.out.flush();

         if (cmdArgs.getIORFile() != null)
         {
            PrintWriter ps = new PrintWriter(new FileOutputStream(
            new File( cmdArgs.getIORFile())));
            ps.println(ior);
            ps.close();
         }


         // wait for requests
         orb.run();

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }


}
