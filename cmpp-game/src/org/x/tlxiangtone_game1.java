package org.x;
/**

*Copyright 2003 Xiamen Xiangtone Co. Ltd.

*All right reserved.

*/



public class tlxiangtone_game1

{

	public static void main(String args[])

	{

		 try

		 {

		  	System.out.print("通联全网短信游戏线程开始....");

		  	VCPServer server = new VCPServer(7800);

          	server.start();

          

          	//Thread.currentThread().sleep(2000);

          

          	//System.out.println("游戏定时触发发送线程开始...."); 

         	 //GameServer game = new GameServer();

          	//new Thread(game).start();

        }

        catch(Exception e)

        {

        	System.out.println("系统出错...");

        	e.printStackTrace();

        	System.exit(0);

        }

	}

}

