

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBClose {
	/**
     *  �ر���������
     * @param con
     * @param sta
     * @param rs    //��Բ�ѯ
     */
   public static void closeQuery(Connection con,Statement sta,ResultSet rs){

        try {
            if(rs !=null)rs.close();
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }

        try {
            if(sta !=null)sta.close();
        } catch (Exception e) {
        	 System.out.println(e.getMessage());
        }

        try {
            if(con !=null)con.close();
        } catch (Exception e) {
        	 System.out.println(e.getMessage());
        }
    }
   public static void closeUpdate(Connection con,Statement sta){
       try {
           if(sta !=null)sta.close();
       } catch (Exception e) {
       	 System.out.println(e.getMessage());
       }

       try {
           if(con !=null)con.close();
       } catch (Exception e) {
       	 System.out.println(e.getMessage());
       }
   }
    
}
