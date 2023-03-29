import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class TransactionTest {
    public static void main(String args[]){
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demoproject","userid","password");

            con.setAutoCommit(false);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter From Account id:");
            int f_id = Integer.parseInt(br.readLine());

            System.out.println("Enter to Account id:");
            int t_id = Integer.parseInt(br.readLine());

            System.out.println("Enter Amount:");
            int amt = Integer.parseInt(br.readLine());

            PreparedStatement pstmt1 = con.prepareStatement("update amount set salary = salary+? where id=?");
            PreparedStatement pstmt2 = con.prepareStatement("update amount set salary = salary-? where id=?");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select salary from amount where id="+f_id);

            int avlAmt =0;
            if (rs.next()){
                avlAmt = rs.getInt("salary");

            }


            if(avlAmt>amt){
                pstmt1.setInt(1,amt);
                pstmt1.setInt(2,t_id);
                pstmt1.executeUpdate();

                pstmt2.setInt(1,amt);
                pstmt2.setInt(2,f_id);
                pstmt2.executeUpdate();

                con.commit();
            }
            else {
                System.out.println("you do not have sufficient balanced ");
            }


        }catch (Exception e){
                try{
                    System.out.println(e);
                    System.out.println("Rolling Back");
                    con.rollback();

                }catch (Exception f){
                    System.out.println(f);
                }

        }

        finally {
            try {
                con.close();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
