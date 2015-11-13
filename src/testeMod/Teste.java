package testeMod;

//ffffdssffdgfhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
import java.net.*;
import java.io.*;
import net.wimpi.modbus.*;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.io.*;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.util.*;
import net.wimpi.modbus.procimg.*;

public class Teste {
	public static void main(String args[]) throws UnknownHostException, Exception {
		TCPMasterConnection con = null; //the connection
		ModbusTCPTransaction trans = null; //the transaction
		ReadInputRegistersRequest req = null; //the request
		ReadInputRegistersResponse res = null; //the response
		
		/* Variables for storing the parameters */
		InetAddress addr = InetAddress.getByName("127.0.0.1");
		int port = 505;
		int ref = 0; //the reference; offset where to start reading from
		int count = 3; //the number of DI's to read
		int repeat = 1; //a loop for repeating the transaction
		
		con = new TCPMasterConnection(addr);
		con.setPort(port);
		con.connect();
		
		//LER DO PLC
		//3. Prepare the request
		req = new ReadInputRegistersRequest(ref,count);

		//4. Prepare the transaction
		trans = new ModbusTCPTransaction(con);
		trans.setRequest(req);
		
		int k = 0;
		int a=0;
		do {
		  trans.execute();
		  res = (ReadInputRegistersResponse) trans.getResponse();
		  if(a!=res.getRegisterValue(0))//nao imprimir repetido
		  {
		  a = res.getRegisterValue(0);
		  System.out.println("Digital Inputs Status=" +Integer.toBinaryString((a & 0xFF) + 0x100).substring(1));
		 
		  
		  //ESCREVER NO PLC
		  SimpleRegister regis = new SimpleRegister(5);//prepara registo a enviar		
			WriteSingleRegisterRequest req2 = new WriteSingleRegisterRequest(0,regis);//cria pedido
			ModbusTCPTransaction trans2 = new ModbusTCPTransaction(con);//cria transação
			trans2.setRequest(req2);//faz pedido
			trans2.execute();//executa transação
			WriteSingleRegisterResponse res2=(WriteSingleRegisterResponse) trans2.getResponse();//obtem resposta
			int c=res2.getRegisterValue();
			int d=res2.getReference();
			System.out.println("escrito no registo" +d +c);
		  
		  }
		  k++;
		 
		} while (repeat==1);//se estiver ==1 é ciclo infinito
		
		
		/*//ESCREVER NO PLC
		SimpleRegister regis = new SimpleRegister(5);//prepara registo a enviar		
		WriteSingleRegisterRequest req2 = new WriteSingleRegisterRequest(0,regis);//cria pedido
		ModbusTCPTransaction trans2 = new ModbusTCPTransaction(con);//cria transação
		trans2.setRequest(req2);//faz pedido
		trans2.execute();//executa transação
		WriteSingleRegisterResponse res2=(WriteSingleRegisterResponse) trans2.getResponse();//obtem resposta
		int c=res2.getRegisterValue();
		int d=res2.getReference();
		System.out.println("escrito no registo" +d +c);
		
		//TESTE GITHUB
		Igual ao qu esté dentro do ciclo*/
		 
		 
		 //6. Close the connection
		 con.close();
	}
}
